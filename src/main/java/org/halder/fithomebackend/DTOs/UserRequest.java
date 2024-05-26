package org.halder.fithomebackend.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String email;
    private String activityLevel;
    private String gender;
    @NotNull
    private Integer age;
    private Double weight;
    private Integer height;
    private MultipartFile image;
}
