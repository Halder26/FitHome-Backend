package org.halder.fithomebackend.DTOs.Converter;

import org.halder.fithomebackend.DTOs.RoutineResponse;
import org.halder.fithomebackend.Entities.Routine;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoutineConverter {
    public List<RoutineResponse> convertToResponseList(List<Routine> routines) {
        return routines.stream().map(routine -> {
            RoutineResponse routineResponse = new RoutineResponse();
            routineResponse.setId(routine.getId());
            routineResponse.setName(routine.getName());
            routineResponse.setIsPublic(routine.getIsPublic());
            routineResponse.setCreatorUsername(routine.getCreator().getRealUsername());
            return routineResponse;
        }).collect(Collectors.toList());
    }
}
