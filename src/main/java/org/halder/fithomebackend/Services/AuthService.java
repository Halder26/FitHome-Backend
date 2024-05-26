package org.halder.fithomebackend.Services;

import lombok.RequiredArgsConstructor;
import org.halder.fithomebackend.DTOs.*;
import org.halder.fithomebackend.Entities.ActivityLevel;
import org.halder.fithomebackend.Entities.Gender;
import org.halder.fithomebackend.Entities.Role;
import org.halder.fithomebackend.Entities.User;
import org.halder.fithomebackend.Repositories.RoleRepository;
import org.halder.fithomebackend.Repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final FileStorageService fileStorageService;

    public ResponseEntity<AuthResponse> login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user=userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token=jwtService.getToken(user);
        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .build());

    }

    public ResponseEntity<AuthResponse> register(UserRequest request, MultipartFile image) {
        try {
            System.out.println(request);
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));

            Set<Role> roles = new HashSet<>();
            roles.add(userRole);

            User user = User.builder()
                    .email(request.getEmail())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .roles(roles)
                    .age(request.getAge())
                    .height(request.getHeight())
                    .weight(request.getWeight())
                    .activity_level(ActivityLevel.valueOf(request.getActivityLevel()))
                    .createdRoutines(new HashSet<>())
                    .routines(new HashSet<>())
                    .gender(Gender.valueOf(request.getGender()))
                    .build();

            if (image != null) {
                if(!image.isEmpty()) {
                    String filename = fileStorageService.store(image, "user");
                    user.setImagePath(filename);
                }else {
                    user.setImagePath(null);
                }
            }

            userRepository.save(user);

            return ResponseEntity.ok(AuthResponse.builder()
                    .token(jwtService.getToken(user))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<UserResponse> updateProfile(UserUpdateRequest request, MultipartFile image, Long userId) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            user.setUsername(request.getUsername());
            user.setAge(request.getAge());
            user.setWeight(request.getWeight());
            user.setHeight(request.getHeight());
            user.setActivity_level(ActivityLevel.valueOf(request.getActivityLevel()));
            if (image != null) {
                if (!image.isEmpty()) {
                    if (user.getImagePath() != null && !user.getImagePath().isEmpty()) {
                        fileStorageService.deleteFile(user.getImagePath());
                    }
                    String filePath = fileStorageService.store(image, "user");
                    user.setImagePath(filePath);
                }
            }
            userRepository.save(user);
            return ResponseEntity.ok(new UserResponse(user.getId(),user.getRealUsername(), user.getEmail(),user.getActivity_level(),user.getGender(), user.getAge(), user.getWeight(), user.getHeight(), user.getImagePath()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
