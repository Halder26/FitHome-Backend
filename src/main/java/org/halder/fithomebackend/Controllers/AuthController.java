package org.halder.fithomebackend.Controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.halder.fithomebackend.DTOs.*;
import org.halder.fithomebackend.Services.AuthService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/auth/")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping(value = "register", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<AuthResponse> register(@Valid @RequestPart("user") UserRequest request, @RequestPart("image") final MultipartFile image)
    {
        return authService.register(request, image);
    }

    @PutMapping(value = "update/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserResponse> update(@Valid @RequestPart("user") UserUpdateRequest request, @RequestPart("image") final MultipartFile image, @PathVariable @NotNull Long userId)
    {
        return authService.updateProfile(request, image, userId);
    }

}
