package org.halder.fithomebackend.Controllers;

import lombok.AllArgsConstructor;
import org.halder.fithomebackend.DTOs.RoutineRequest;
import org.halder.fithomebackend.DTOs.RoutineResponse;
import org.halder.fithomebackend.Services.RoutineService;
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
public class RoutineController {

    private final RoutineService routineService;

    @PostMapping(value = "routine", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<RoutineResponse> createRoutine(
            @RequestPart("routine") RoutineRequest routine,
            @RequestPart("image") final MultipartFile image) {
        return routineService.saveRoutine(routine,image);
    }

    @PutMapping(value = "routine/{routineId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<RoutineResponse> updateRoutine(
            @PathVariable Long routineId,
            @RequestPart("routine") RoutineRequest routine,
            @RequestPart("image") final MultipartFile image) {
        return routineService.updateRoutineById(routineId, routine, image);
    }

    @DeleteMapping(value = "routine/{routineId}")
    public ResponseEntity<String> deleteRoutine(
            @PathVariable Long routineId) {
        return routineService.removeRoutineById(routineId);
    }

    @GetMapping(value = "routines")
    public ResponseEntity<List<RoutineResponse>> getAllRoutines() {
        return routineService.getAllRoutines();
    }

    @GetMapping(value = "routine/{routineId}")
    public ResponseEntity<RoutineResponse> getRoutineById(
            @PathVariable Long routineId) {
        return routineService.getRoutineById(routineId);
    }

    @GetMapping(value = "routine/{routineId}/user/{userId}/add")
    public ResponseEntity<String> saveRoutineToUser(
            @PathVariable Long userId,
            @PathVariable Long routineId) {
        return routineService.saveRoutineToUser(userId,routineId);
    }

    @GetMapping(value = "routine/{routineId}/user/{userId}/remove")
    public ResponseEntity<String> removeRoutineFromUser(
            @PathVariable Long userId,
            @PathVariable Long routineId) {
        return routineService.removeRoutineFromUser(userId,routineId);
    }

    @GetMapping(value = "routines/user/{userId}/created")
    public ResponseEntity<List<RoutineResponse>> getRoutinesCreatedByUserId(
            @PathVariable Long userId) {
        return routineService.getRoutinesCreatedByUserId(userId);
    }

    @GetMapping(value = "routines/user/{userId}/saved")
    public ResponseEntity<List<RoutineResponse>> getRoutinesSavedByUserId(
            @PathVariable Long userId) {
        return routineService.getSavedRoutinesByUser(userId);
    }

    @GetMapping(value="routines/image/{routineId}")
    public ResponseEntity<Resource> getFile(@PathVariable Long routineId) {
        return routineService.getFile(routineId);
    }

    @GetMapping(value = "routines/public")
    public ResponseEntity<List<RoutineResponse>> getPublicRoutines() {
        return routineService.getPublicRoutines();
    }
}
