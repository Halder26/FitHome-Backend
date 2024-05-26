package org.halder.fithomebackend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutineRequest {
    private String name;
    private Boolean isPublic;
    private Long userId;
    private MultipartFile image;
}
