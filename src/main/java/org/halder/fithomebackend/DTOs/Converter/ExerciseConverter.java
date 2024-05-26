package org.halder.fithomebackend.DTOs.Converter;

import org.halder.fithomebackend.DTOs.ExerciseResponse;
import org.halder.fithomebackend.Entities.Exercise;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ExerciseConverter {
    public List<ExerciseResponse> convertToResponseList(Set<Exercise> exercises) {
        return exercises.stream().map(exercise -> {
            ExerciseResponse exerciseResponse = new ExerciseResponse();
            exerciseResponse.setId(exercise.getId());
            exerciseResponse.setName(exercise.getName());
            exerciseResponse.setDescription(exercise.getDescription());
            exerciseResponse.setImage_url(exercise.getImage_url());
            exerciseResponse.setEquipmentRequired(exercise.getEquipmentRequired());
            exerciseResponse.setMuscleGroup(exercise.getMuscleGroup());
            return exerciseResponse;
        }).collect(Collectors.toList());
    }
}
