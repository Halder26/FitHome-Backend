package org.halder.fithomebackend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutineResponse {
    private Long id;
    private String name;
    private Boolean isPublic;
    private String creatorUsername;
    private List<ExerciseResponse> exercises;
    private String image_url;
}
