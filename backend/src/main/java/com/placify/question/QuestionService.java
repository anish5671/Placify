package com.placify.question;

import com.placify.exception.ResourceNotFoundException;
import com.placify.progress.UserProgress;
import com.placify.progress.UserProgressRepository;
import com.placify.security.UserPrincipal;
import com.placify.user.User;
import com.placify.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final TopicRepository topicRepository;
    private final BookmarkRepository bookmarkRepository;
    private final UserProgressRepository userProgressRepository;
    private final UserRepository userRepository;

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal principal) {
            return principal.getId();
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<QuestionDto> getQuestions(String category, Long topicId, String difficulty, String search, String company) {
        List<Question> questions = questionRepository.findQuestionsWithFilters(
                (category != null && !category.isBlank()) ? category : "",
                topicId,
                (difficulty != null && !difficulty.isBlank()) ? difficulty : "",
                (search != null && !search.isBlank()) ? search : "",
                (company != null && !company.isBlank()) ? company : ""
        );

        Long currentUserId = getCurrentUserId();
        Set<Long> bookmarkedIds = new HashSet<>();
        Set<Long> solvedIds = new HashSet<>();

        if (currentUserId != null) {
            bookmarkedIds = bookmarkRepository.findByUserIdOrderByCreatedAtDesc(currentUserId)
                    .stream().map(b -> b.getQuestion().getId()).collect(Collectors.toSet());
            solvedIds = userProgressRepository.findByUserIdAndIsSolvedTrue(currentUserId)
                    .stream().map(p -> p.getQuestion().getId()).collect(Collectors.toSet());
        }

        final Set<Long> finalBookmarked = bookmarkedIds;
        final Set<Long> finalSolved = solvedIds;

        return questions.stream()
                .map(q -> QuestionDto.fromEntity(q, finalBookmarked.contains(q.getId()), finalSolved.contains(q.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public QuestionDto getQuestionById(Long id) {
        Question q = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));

        Long currentUserId = getCurrentUserId();
        boolean bookmarked = currentUserId != null && bookmarkRepository.existsByUserIdAndQuestionId(currentUserId, id);
        boolean solved = currentUserId != null && userProgressRepository.existsByUserIdAndQuestionIdAndIsSolvedTrue(currentUserId, id);

        return QuestionDto.fromEntity(q, bookmarked, solved);
    }

    @Transactional
    public boolean toggleBookmark(Long questionId) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new ResourceNotFoundException("Authentication required to bookmark questions");
        }

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUserId));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", questionId));

        Optional<Bookmark> existing = bookmarkRepository.findByUserIdAndQuestionId(currentUserId, questionId);
        if (existing.isPresent()) {
            bookmarkRepository.delete(existing.get());
            return false;
        } else {
            Bookmark bookmark = Bookmark.builder()
                    .user(user)
                    .question(question)
                    .build();
            bookmarkRepository.save(bookmark);
            return true;
        }
    }

    @Transactional
    public boolean toggleSolved(Long questionId) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new ResourceNotFoundException("Authentication required to update progress");
        }

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUserId));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", questionId));

        UserProgress progress = userProgressRepository.findByUserIdAndQuestionId(currentUserId, questionId)
                .orElseGet(() -> UserProgress.builder()
                        .user(user)
                        .question(question)
                        .attemptsCount(0)
                        .isSolved(false)
                        .build());

        boolean newStatus = !progress.isSolved();
        progress.setSolved(newStatus);
        if (newStatus) {
            progress.setSolvedAt(LocalDateTime.now());
            progress.setAttemptsCount(progress.getAttemptsCount() + 1);
        }
        userProgressRepository.save(progress);
        return newStatus;
    }

    @Transactional(readOnly = true)
    public List<QuestionDto> getBookmarkedQuestions() {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new ResourceNotFoundException("Authentication required");
        }

        List<Bookmark> bookmarks = bookmarkRepository.findByUserIdOrderByCreatedAtDesc(currentUserId);
        Set<Long> solvedIds = userProgressRepository.findByUserIdAndIsSolvedTrue(currentUserId)
                .stream().map(p -> p.getQuestion().getId()).collect(Collectors.toSet());

        return bookmarks.stream()
                .map(b -> QuestionDto.fromEntity(b.getQuestion(), true, solvedIds.contains(b.getQuestion().getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<QuestionDto> getDsaQuestions(String topicSlug, String difficulty) {
        Long currentUserId = getCurrentUserId();
        Set<Long> bookmarkedIds = new HashSet<>();
        Set<Long> solvedIds = new HashSet<>();

        if (currentUserId != null) {
            bookmarkedIds = bookmarkRepository.findByUserIdOrderByCreatedAtDesc(currentUserId)
                    .stream().map(b -> b.getQuestion().getId()).collect(Collectors.toSet());
            solvedIds = userProgressRepository.findByUserIdAndIsSolvedTrue(currentUserId)
                    .stream().map(p -> p.getQuestion().getId()).collect(Collectors.toSet());
        }

        List<Question> dsaQuestions = questionRepository.findByCategoryIgnoreCase("DSA");

        if (topicSlug != null && !topicSlug.isBlank()) {
            dsaQuestions = dsaQuestions.stream()
                    .filter(q -> q.getTopic() != null && q.getTopic().getSlug().equalsIgnoreCase(topicSlug))
                    .collect(Collectors.toList());
        }

        if (difficulty != null && !difficulty.isBlank()) {
            dsaQuestions = dsaQuestions.stream()
                    .filter(q -> q.getDifficulty().equalsIgnoreCase(difficulty))
                    .collect(Collectors.toList());
        }

        final Set<Long> finalBookmarked = bookmarkedIds;
        final Set<Long> finalSolved = solvedIds;

        return dsaQuestions.stream()
                .map(q -> QuestionDto.fromEntity(q, finalBookmarked.contains(q.getId()), finalSolved.contains(q.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TopicDto> getAllTopics() {
        Long currentUserId = getCurrentUserId();
        return topicRepository.findAll().stream().map(topic -> {
            long total = questionRepository.countByTopicId(topic.getId());
            long solved = currentUserId != null ? userProgressRepository.countSolvedByUserIdAndTopicId(currentUserId, topic.getId()) : 0;
            return TopicDto.fromEntity(topic, total, solved);
        }).collect(Collectors.toList());
    }
}
