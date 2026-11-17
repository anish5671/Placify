package com.placify.user;

import com.placify.exception.ResourceNotFoundException;
import com.placify.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new ResourceNotFoundException("Authenticated user not found");
        }
        return userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", principal.getId()));
    }

    @Transactional(readOnly = true)
    public UserDto getCurrentUserProfile() {
        return UserDto.fromEntity(getCurrentAuthenticatedUser());
    }

    @Transactional
    public UserDto updateProfile(UserProfileUpdateDto updateDto) {
        User user = getCurrentAuthenticatedUser();
        if (updateDto.getFullName() != null) user.setFullName(updateDto.getFullName());
        if (updateDto.getTargetCompany() != null) user.setTargetCompany(updateDto.getTargetCompany());
        if (updateDto.getTargetRole() != null) user.setTargetRole(updateDto.getTargetRole());
        if (updateDto.getGraduationYear() != null) user.setGraduationYear(updateDto.getGraduationYear());

        User updated = userRepository.save(user);
        return UserDto.fromEntity(updated);
    }
}
