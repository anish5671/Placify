package com.placify.resume;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ResumeAnalysisService {
    private static final int MAX_JOB_DESCRIPTION_LENGTH = 12_000;

    private final ResumeFileValidator fileValidator;
    private final ResumeTextExtractor textExtractor;
    private final ResumeScoringService scoringService;
    private final ResumeJobMatcher jobMatcher;
    private final GeminiAnalysisService geminiAnalysisService;

    public ResumeAnalysisResult analyze(MultipartFile file, String jobDescription) {
        String extension = fileValidator.validate(file);
        if (jobDescription != null && jobDescription.length() > MAX_JOB_DESCRIPTION_LENGTH) {
            throw new com.placify.exception.BadRequestException("The job description must be 12,000 characters or fewer");
        }

        String resumeText = textExtractor.extract(file, extension);
        ResumeScoringService.Score score = scoringService.score(resumeText);
        var skills = jobMatcher.detectSkills(resumeText);
        var roleMatches = jobMatcher.predictRoles(skills);
        var jobDescriptionMatch = jobMatcher.matchJobDescription(resumeText, jobDescription);
        var aiFeedback = geminiAnalysisService.analyze(resumeText, skills);

        return new ResumeAnalysisResult(
                score.overallScore(),
                score.sectionScores(),
                skills,
                score.missingSections(),
                roleMatches,
                jobDescriptionMatch,
                aiFeedback,
                "The uploaded file is processed in memory and is not stored. AI feedback sends extracted resume text to Google Gemini."
        );
    }
}
// "style(ui): refine button hover states and card spacing"