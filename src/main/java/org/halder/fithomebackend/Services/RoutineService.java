package org.halder.fithomebackend.Services;

import org.halder.fithomebackend.DTOs.Converter.ExerciseConverter;
import org.halder.fithomebackend.DTOs.Converter.RoutineConverter;
import org.halder.fithomebackend.DTOs.RoutineRequest;
import org.halder.fithomebackend.DTOs.RoutineResponse;
import org.halder.fithomebackend.Entities.Exercise;
import org.halder.fithomebackend.Entities.Routine;
import org.halder.fithomebackend.Entities.User;
import org.halder.fithomebackend.Entities.UserRoutine;
import org.halder.fithomebackend.Repositories.RoutineRepository;
import org.halder.fithomebackend.Repositories.UserRepository;
import org.halder.fithomebackend.Repositories.UserRoutineRepository;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class RoutineService {
    private final UserRepository userRepository;
    private final RoutineRepository routineRepository;
    private final UserRoutineRepository userRoutineRepository;
    private final ExerciseConverter exerciseConverter;
    private final FileStorageService fileStorageService;

    public RoutineService(FileStorageService fileStorageService,UserRepository userRepository, RoutineRepository routineRepository, UserRoutineRepository userRoutineRepository, ExerciseConverter exerciseConverter) {
        this.userRepository = userRepository;
        this.routineRepository = routineRepository;
        this.userRoutineRepository = userRoutineRepository;
        this.exerciseConverter = exerciseConverter;
        this.fileStorageService = fileStorageService;
    }

    public ResponseEntity<RoutineResponse> saveRoutine(RoutineRequest routineRequest, MultipartFile image){
        try {
            User user = userRepository.findById(routineRequest.getUserId()).orElse(null);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            Routine routine = new Routine();
            routine.setName(routineRequest.getName());
            routine.setIsPublic(routineRequest.getIsPublic());
            routine.setCreator(user);
            routine.setExercises(new HashSet<>());
            routine.setComments(new ArrayList<>());
            if(image != null){
                if(!image.isEmpty()){
                    String filePath = fileStorageService.store(image, "routine");
                    routine.setImage_url(filePath);
                }
                else {
                    routine.setImage_url(null);
                }
            }
            else {
                routine.setImage_url(null);
            }
            routineRepository.save(routine);

            return ResponseEntity.ok(new RoutineResponse(routine.getId(), routine.getName(), routine.getIsPublic(), routine.getCreator().getRealUsername(),exerciseConverter.convertToResponseList(routine.getExercises()), routine.getImage_url()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new RoutineResponse());
        }
    }

    public ResponseEntity<RoutineResponse> updateRoutineById(Long routineId, RoutineRequest routineRequest, MultipartFile image) {
        try {
            Routine routineToUpdate = routineRepository.findById(routineId).orElse(null);
            if (routineToUpdate == null) {
                return ResponseEntity.notFound().build();
            }

            routineToUpdate.setName(routineRequest.getName());
            routineToUpdate.setIsPublic(routineRequest.getIsPublic());
            if (image != null) {
                if(!image.isEmpty()){
                    if(routineToUpdate.getImage_url()!=null && !routineToUpdate.getImage_url().isEmpty())
                    {
                        fileStorageService.deleteFile(routineToUpdate.getImage_url());
                    }
                    String filename = fileStorageService.store(image, "routine");
                    routineToUpdate.setImage_url(filename);
                }
            }
            routineRepository.save(routineToUpdate);

            return ResponseEntity.ok(new RoutineResponse(routineToUpdate.getId(), routineToUpdate.getName(), routineToUpdate.getIsPublic(), routineToUpdate.getCreator().getRealUsername(),exerciseConverter.convertToResponseList(routineToUpdate.getExercises()), routineToUpdate.getImage_url()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new RoutineResponse());
        }
    }

    public ResponseEntity<String> removeRoutineById(Long routineId) {
        try {
            Routine routine = routineRepository.findById(routineId).orElse(null);
            if (routine == null) {
                return ResponseEntity.notFound().build();
            }
            routine.getExercises().forEach(exercise -> {
                exercise.getRoutines().remove(routine);
            });
            userRoutineRepository.deleteAllByRoutineId(routineId);
            routineRepository.delete(routine);
            return ResponseEntity.ok("\"Routine deleted successfully\"");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("\"Error deleting routine: " + e.getMessage() + "\"");
        }
    }

    public ResponseEntity<List<RoutineResponse>> getAllRoutines() {
        try {
            List<RoutineResponse> routines = new ArrayList<>();
            routineRepository.findAll().forEach(routine -> {
                RoutineResponse routineResponse = new RoutineResponse();
                routineResponse.setId(routine.getId());
                routineResponse.setName(routine.getName());
                routineResponse.setIsPublic(routine.getIsPublic());
                routineResponse.setCreatorUsername(routine.getCreator().getRealUsername());
                routineResponse.setImage_url(routine.getImage_url());
                routineResponse.setExercises(exerciseConverter.convertToResponseList(routine.getExercises()));
                routines.add(routineResponse);
            });
            return ResponseEntity.ok(routines);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<RoutineResponse> getRoutineById(Long routineId) {
        Routine routine = routineRepository.findById(routineId).orElse(null);
        if (routine == null) {
            return ResponseEntity.notFound().build();
        }
        RoutineResponse routineResponse = new RoutineResponse();
        routineResponse.setId(routine.getId());
        routineResponse.setName(routine.getName());
        routineResponse.setIsPublic(routine.getIsPublic());
        routineResponse.setCreatorUsername(routine.getCreator().getRealUsername());
        routineResponse.setImage_url(routine.getImage_url());
        routineResponse.setExercises(exerciseConverter.convertToResponseList(routine.getExercises()));
        return ResponseEntity.ok(routineResponse);
    }

    public ResponseEntity<String> saveRoutineToUser(Long userId, Long routineId) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            Routine routine = routineRepository.findById(routineId).orElse(null);
            if (user == null || routine == null || !routine.getIsPublic()) {
                return ResponseEntity.notFound().build();
            }

            UserRoutine userRoutine = new UserRoutine();
            userRoutine.setUser(user);
            userRoutine.setRoutine(routine);
            userRoutineRepository.save(userRoutine);

            return ResponseEntity.ok("\"Routine saved to user successfully\"");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("\"Error saving routine to user: " + e.getMessage() + "\"");
        }
    }

    public ResponseEntity<String> removeRoutineFromUser(Long userId, Long routineId) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            Routine routine = routineRepository.findById(routineId).orElse(null);
            if (user == null || routine == null) {
                return ResponseEntity.notFound().build();
            }
            UserRoutine registerToDelete = userRoutineRepository.findByUserIdAndRoutineId(userId, routineId);
            userRoutineRepository.deleteById(registerToDelete.getId());
            return ResponseEntity.ok("\"Routine removed from user successfully\"");
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body("\"Error removing routine from user: " + e.getMessage() + "\"");
        }
    }

    public ResponseEntity<List<RoutineResponse>> getRoutinesCreatedByUserId(Long userId) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            List<RoutineResponse> routines = new ArrayList<>();

            routineRepository.findByCreator_Id(userId).forEach(routine -> {
                RoutineResponse routineResponse = new RoutineResponse();
                routineResponse.setId(routine.getId());
                routineResponse.setName(routine.getName());
                routineResponse.setIsPublic(routine.getIsPublic());
                routineResponse.setCreatorUsername(routine.getCreator().getRealUsername());
                routineResponse.setImage_url(routine.getImage_url());
                routineResponse.setExercises(exerciseConverter.convertToResponseList(routine.getExercises()));
                routines.add(routineResponse);
            });
            return ResponseEntity.ok(routines);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<List<RoutineResponse>> getSavedRoutinesByUser(Long userId) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            List<RoutineResponse> routines = new ArrayList<>();

            user.getRoutines().forEach(userRoutine -> {
                Routine routine = userRoutine.getRoutine();
                RoutineResponse routineResponse = new RoutineResponse();
                routineResponse.setId(routine.getId());
                routineResponse.setName(routine.getName());
                routineResponse.setIsPublic(routine.getIsPublic());
                routineResponse.setCreatorUsername(routine.getCreator().getRealUsername());
                routineResponse.setImage_url(routine.getImage_url());
                routineResponse.setExercises(exerciseConverter.convertToResponseList(routine.getExercises()));
                routines.add(routineResponse);
            });

            return ResponseEntity.ok(routines);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<Resource> getFile(Long routineId) {
        try {
            Routine routine = routineRepository.findById(routineId).orElse(null);
            if (routine == null) {
                return ResponseEntity.notFound().build();
            }
            Resource file = fileStorageService.loadAsResource(routine.getImage_url());
            String contentType = Files.probeContentType(file.getFile().toPath());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<List<RoutineResponse>> getPublicRoutines() {
        try {
            List<RoutineResponse> routines = new ArrayList<>();
            if(routineRepository.findByIsPublicTrue().isEmpty()){
                return ResponseEntity.notFound().build();
            }else {
                routineRepository.findByIsPublicTrue().forEach(routine -> {
                    RoutineResponse routineResponse = new RoutineResponse();
                    routineResponse.setId(routine.getId());
                    routineResponse.setName(routine.getName());
                    routineResponse.setIsPublic(routine.getIsPublic());
                    routineResponse.setCreatorUsername(routine.getCreator().getRealUsername());
                    routineResponse.setImage_url(routine.getImage_url());
                    routineResponse.setExercises(exerciseConverter.convertToResponseList(routine.getExercises()));
                    routines.add(routineResponse);
                });
                return ResponseEntity.ok(routines);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
