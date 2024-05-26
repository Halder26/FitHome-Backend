package org.halder.fithomebackend.Services;

import org.halder.fithomebackend.DTOs.ExerciseRequest;
import org.halder.fithomebackend.DTOs.ExerciseResponse;
import org.halder.fithomebackend.Entities.Exercise;
import org.halder.fithomebackend.Entities.MuscleGroup;
import org.halder.fithomebackend.Entities.Routine;
import org.halder.fithomebackend.Repositories.ExerciseRepository;
import org.halder.fithomebackend.Repositories.RoutineRepository;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final FileStorageService fileStorageService;
    private final RoutineRepository routineRepository;

    public ExerciseService(ExerciseRepository exerciseRepository, FileStorageService fileStorageService, RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
        this.fileStorageService = fileStorageService;
        this.exerciseRepository = exerciseRepository;
    }

    public ResponseEntity<List<ExerciseResponse>> getAllExercises() {
        try {
            List<ExerciseResponse> exercises = new ArrayList<>();
            exerciseRepository.findAll().forEach(exercise -> {
                exercises.add(new ExerciseResponse(exercise.getId(), exercise.getName(), exercise.getDescription(), exercise.getImage_url(), exercise.getEquipmentRequired(), exercise.getMuscleGroup()));
            });
            return ResponseEntity.ok(exercises);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<List<String>> getMuscleGroups() {
        List<String> muscleGroups = new ArrayList<>();
        for (MuscleGroup muscleGroup : MuscleGroup.values()) {
            muscleGroups.add(muscleGroup.toString());
        }
        return ResponseEntity.ok(muscleGroups);
    }

    public ResponseEntity<ExerciseResponse> getExerciseById(Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId).orElse(null);
        if (exercise == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ExerciseResponse(exercise.getId(), exercise.getName(), exercise.getDescription(), exercise.getImage_url(), exercise.getEquipmentRequired(), exercise.getMuscleGroup()));
    }

    public ResponseEntity<String> removeExerciseById(Long exerciseId) {
        try {
            exerciseRepository.deleteById(exerciseId);
            return ResponseEntity.ok("\"Exercise deleted successfully with ID: " + exerciseId + "\"");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("\"Error deleting exercise: " + e.getMessage() + "\"");
        }
    }

    public ResponseEntity<String> updateExerciseById(Long exerciseId, ExerciseRequest exerciseRequest, MultipartFile image) {
        try {
            Exercise exerciseToUpdate = exerciseRepository.findById(exerciseId).orElse(null);
            if (exerciseToUpdate == null) {
                return ResponseEntity.notFound().build();
            }
            exerciseToUpdate.setName(exerciseRequest.getName());
            exerciseToUpdate.setDescription(exerciseRequest.getDescription());
            exerciseToUpdate.setEquipmentRequired(exerciseRequest.getEquipmentRequired());
            exerciseToUpdate.setMuscleGroup(exerciseRequest.getMuscleGroup());
            if(image != null){
               if(!image.isEmpty())
               {
                   if(exerciseToUpdate.getImage_url()!=null && !exerciseToUpdate.getImage_url().isEmpty())
                   {
                       fileStorageService.deleteFile(exerciseToUpdate.getImage_url());
                   }
                   String filePath = fileStorageService.store(image, "exercise");
                   exerciseToUpdate.setImage_url(filePath);
               }
            }
            exerciseRepository.save(exerciseToUpdate);
            return ResponseEntity.ok("\"Exercise updated successfully with ID: " + exerciseId + "\"");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("\"Error updating exercise: " + e.getMessage() + "\"");
        }
    }

    public ResponseEntity<ExerciseResponse> saveExercise(ExerciseRequest exerciseRequest, MultipartFile image) {
        try {
            Exercise exercise = new Exercise();
            exercise.setName(exerciseRequest.getName());
            exercise.setDescription(exerciseRequest.getDescription());
            exercise.setEquipmentRequired(exerciseRequest.getEquipmentRequired());
            exercise.setMuscleGroup(exerciseRequest.getMuscleGroup());
            if(image != null){
                if(!image.isEmpty()) {
                    String filePath = fileStorageService.store(image, "exercise");
                    exercise.setImage_url(filePath);
                }else {
                    exercise.setImage_url(null);
                }
            }
            exerciseRepository.save(exercise);
            return ResponseEntity.ok(new ExerciseResponse(exercise.getId(), exercise.getName(), exercise.getDescription(), exercise.getImage_url(), exercise.getEquipmentRequired(), exercise.getMuscleGroup()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<List<ExerciseResponse>> getExercisesByRoutineId(Long routineId) {
        try {
            List<ExerciseResponse> exercises = new ArrayList<>();
            Routine routine = routineRepository.findById(routineId).orElse(null);
            if (routine == null) {
                return ResponseEntity.notFound().build();
            }
            routine.getExercises().forEach(exercise -> {
                exercises.add(new ExerciseResponse(exercise.getId(), exercise.getName(), exercise.getDescription(), exercise.getImage_url(), exercise.getEquipmentRequired(), exercise.getMuscleGroup()));
            });
            return ResponseEntity.ok(exercises);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<String> addExerciseToRoutine(Long routineId, Long exerciseId) {
        try {
            Routine routine = routineRepository.findById(routineId).orElse(null);
            if (routine == null) {
                return ResponseEntity.notFound().build();
            }
            Exercise exercise = exerciseRepository.findById(exerciseId).orElse(null);
            if (exercise == null) {
                return ResponseEntity.notFound().build();
            }
            routine.getExercises().add(exercise);
            routineRepository.save(routine);
            return ResponseEntity.ok("\"Exercise added to routine successfully\"");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("\"Error adding exercise to routine: " + e.getMessage() + "\"");
        }
    }

    public ResponseEntity<String> removeExerciseFromRoutine(Long routineId, Long exerciseId) {
        try {
            Routine routine = routineRepository.findById(routineId).orElse(null);
            if (routine == null) {
                return ResponseEntity.notFound().build();
            }
            Exercise exercise = exerciseRepository.findById(exerciseId).orElse(null);
            if (exercise == null) {
                return ResponseEntity.notFound().build();
            }
            routine.getExercises().remove(exercise);
            routineRepository.save(routine);
            return ResponseEntity.ok("\"Exercise removed from routine successfully\"");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("\"Error removing exercise from routine: " + e.getMessage() + "\"");
        }
    }

    public ResponseEntity<Resource> getFile(Long exerciseId) {
        try {
            Exercise exercise = exerciseRepository.findById(exerciseId).orElse(null);
            if (exercise == null) {
                return ResponseEntity.notFound().build();
            }
            Resource file = fileStorageService.loadAsResource(exercise.getImage_url());
            String contentType = Files.probeContentType(file.getFile().toPath());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
