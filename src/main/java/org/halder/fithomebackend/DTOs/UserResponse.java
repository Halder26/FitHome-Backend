package org.halder.fithomebackend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.halder.fithomebackend.Entities.ActivityLevel;
import org.halder.fithomebackend.Entities.Gender;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private ActivityLevel activityLevel;
    private Gender gender;
    private Integer age;
    private Double weight;
    private Integer height;
    private String imagePath;
}
