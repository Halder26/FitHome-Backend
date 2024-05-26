package org.halder.fithomebackend.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.halder.fithomebackend.Entities.MuscleGroup;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseRequest {
    @NotNull
    private String name;
    @NotNull
    private String description;
    private MultipartFile image;
    @NotNull
    private Boolean equipmentRequired;
    @NotNull
    private MuscleGroup muscleGroup;
}
