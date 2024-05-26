package org.halder.fithomebackend.Controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.halder.fithomebackend.DTOs.UserRequest;
import org.halder.fithomebackend.DTOs.UserResponse;
import org.halder.fithomebackend.Services.UserService;
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
public class UserController {

    private final UserService userService;

    @GetMapping(value = "users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value= "users/genders")
    public ResponseEntity<List<String>> getGenders(){
        return userService.getGenders();
    }

    @GetMapping(value = "user/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping(value = "user/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> updateUserById(
            @PathVariable Long userId,
            @Valid @RequestPart("user") UserRequest user,
            @RequestPart("image") final MultipartFile image){
        return userService.updateUserById(userId, user, image);
    }

    @DeleteMapping(value = "user/{userId}")
    public ResponseEntity<String> removeUserById(
            @PathVariable Long userId) {
        return userService.removeUserById(userId);
    }

    @GetMapping(value="user/image/{userId}")
    public ResponseEntity<Resource> getFile(@PathVariable Long userId) {
        return userService.getUserProfilePhoto(userId);
    }
}
