package org.halder.fithomebackend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.halder.fithomebackend.Entities.MuscleGroup;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseResponse {
    private Long id;
    private String name;
    private String description;
    private String image_url;
    private Boolean equipmentRequired;
    private MuscleGroup muscleGroup;
}
