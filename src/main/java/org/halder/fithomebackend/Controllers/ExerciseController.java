package org.halder.fithomebackend.Controllers;

import lombok.AllArgsConstructor;
import org.halder.fithomebackend.DTOs.ExerciseRequest;
import org.halder.fithomebackend.DTOs.ExerciseResponse;
import org.halder.fithomebackend.Services.ExerciseService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping(value = "exercise", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ExerciseResponse> createExercise(
            @RequestPart("exercise") ExerciseRequest exercise,
            @RequestPart("image") final MultipartFile image) {
        return exerciseService.saveExercise(exercise, image);
    }

    @PutMapping(value = "exercise/{exerciseId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> updateExercise(
            @PathVariable Long exerciseId,
            @RequestPart("exercise") ExerciseRequest exercise,
            @RequestPart("image") final MultipartFile image) {
        return exerciseService.updateExerciseById(exerciseId, exercise, image);
    }

    @DeleteMapping(value = "exercise/{exerciseId}")
    public ResponseEntity<String> deleteExercise(
            @PathVariable Long exerciseId) {
        return exerciseService.removeExerciseById(exerciseId);
    }

    @GetMapping(value = "exercises")
    public ResponseEntity<List<ExerciseResponse>> getAllExercises() {
        return exerciseService.getAllExercises();
    }

    @GetMapping(value = "exercise/{exerciseId}")
    public ResponseEntity<ExerciseResponse> getExerciseById(
            @PathVariable Long exerciseId) {
        return exerciseService.getExerciseById(exerciseId);
    }

    @GetMapping(value = "exercises/routine/{routineId}")
    public ResponseEntity<List<ExerciseResponse>> getExercisesByRoutineId(
            @PathVariable Long routineId) {
        return exerciseService.getExercisesByRoutineId(routineId);
    }

    @GetMapping(value = "exercises/muscle-groups")
    public ResponseEntity<List<String>> getMuscleGroups() {
        return exerciseService.getMuscleGroups();
    }

    @GetMapping(value = "exercise/{exerciseId}/routine/{routineId}")
    public ResponseEntity<String> addExerciseToRoutine(
            @PathVariable Long routineId,
            @PathVariable Long exerciseId) {
        return exerciseService.addExerciseToRoutine(routineId, exerciseId);
    }

    @DeleteMapping(value = "exercise/{exerciseId}/routine/{routineId}")
    public ResponseEntity<String> removeExerciseFromRoutine(
            @PathVariable Long routineId,
            @PathVariable Long exerciseId) {
        return exerciseService.removeExerciseFromRoutine(routineId, exerciseId);
    }

    @GetMapping(value="exercises/image/{exerciseId}")
    public ResponseEntity<Resource> getFile(@PathVariable Long exerciseId) {
        return exerciseService.getFile(exerciseId);
    }
}
