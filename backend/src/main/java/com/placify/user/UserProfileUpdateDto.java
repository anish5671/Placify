package com.placify.user;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserProfileUpdateDto {
    @Size(max = 100)
    private String fullName;

    @Size(max = 100)
    private String targetCompany;

    @Size(max = 100)
    private String targetRole;

    private Integer graduationYear;
}
