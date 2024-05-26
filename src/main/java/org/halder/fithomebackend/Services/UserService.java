package org.halder.fithomebackend.Services;

import org.halder.fithomebackend.DTOs.UserRequest;
import org.halder.fithomebackend.DTOs.UserResponse;
import org.halder.fithomebackend.Entities.*;
import org.halder.fithomebackend.Repositories.UserRepository;
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
public class UserService {
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public UserService(UserRepository userRepository, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    public ResponseEntity<List<UserResponse>> getAllUsers() {
        try{
            List<UserResponse> users = new ArrayList<>();
            userRepository.findAll().forEach(user -> {
                users.add(new UserResponse(user.getId(),user.getRealUsername(), user.getEmail(), user.getActivity_level(), user.getGender(), user.getAge(), user.getWeight(), user.getHeight(), user.getImagePath()));
            });
            return ResponseEntity.ok(users);
        }catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<List<String>> getGenders(){
        List<String> genders = new ArrayList<>();
        for (Gender gender : Gender.values()) {
            genders.add(gender.toString());
        }
        return ResponseEntity.ok(genders);
    }

    public ResponseEntity<UserResponse> getUserById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new UserResponse(user.getId(),user.getRealUsername(), user.getEmail(), user.getActivity_level(), user.getGender(), user.getAge(), user.getWeight(), user.getHeight(), user.getImagePath()));
    }

    public ResponseEntity<String> removeUserById(Long userId) {
        try {
            userRepository.deleteById(userId);
            return ResponseEntity.ok("\"Usuario eliminado con éxito con ID: " + userId + "\"");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("\"Error al eliminar el usuario: " + e.getMessage() + "\"");
        }
    }

    public ResponseEntity<String> updateUserById(Long userId, UserRequest user, MultipartFile image) {
        try{
            User userToUpdate = userRepository.findById(userId).orElse(null);
            if (userToUpdate == null) {
                return ResponseEntity.notFound().build();
            }
            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setActivity_level(ActivityLevel.valueOf(user.getActivityLevel()));
            userToUpdate.setGender(Gender.valueOf(user.getGender()));
            userToUpdate.setAge(user.getAge());
            userToUpdate.setWeight(user.getWeight());
            userToUpdate.setHeight(user.getHeight());
            if(image != null){
                if(userToUpdate.getImagePath()!=null && !userToUpdate.getImagePath().isEmpty())
                {
                    fileStorageService.deleteFile(userToUpdate.getImagePath());
                }
                String filePath = fileStorageService.store(image, "user");
                userToUpdate.setImagePath(filePath);
            }
            userRepository.save(userToUpdate);
            return ResponseEntity.ok("\"Usuario actualizado con éxito con ID: " + userId + "\"");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body("\"Error al actualizar el usuario: " + e.getMessage() + "\"");
        }
    }

    public ResponseEntity<Resource> getUserProfilePhoto(Long userId) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            Resource file = fileStorageService.loadAsResource(user.getImagePath());
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
