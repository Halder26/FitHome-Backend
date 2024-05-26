package org.halder.fithomebackend.Controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.halder.fithomebackend.DTOs.CommentRequest;
import org.halder.fithomebackend.DTOs.CommentResponse;
import org.halder.fithomebackend.Services.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class CommentController {
    private CommentService commentService;

    @PostMapping("comment/routine/{courseId}")
    public ResponseEntity<CommentResponse> createCourseComment(@Valid @RequestBody CommentRequest commentRequest, @PathVariable Long courseId) {
        return commentService.createComment(commentRequest, courseId);
    }

    @GetMapping("comment/{commentId}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long commentId) {
        return commentService.getCommentById(commentId);
    }

    @DeleteMapping("comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        return commentService.deleteCommentById(commentId);
    }

    @PutMapping("comment/{commentId}")
    public ResponseEntity<String> updateComment(@Valid @RequestBody CommentRequest commentRequest,@PathVariable Long commentId) {
        return commentService.updateCommentById(commentRequest,commentId);
    }

    @GetMapping("comment/routine/{routineId}")
    public ResponseEntity<List<CommentResponse>> getCourseComments(@PathVariable Long routineId) {
        return commentService.getCommentsByRoutineId(routineId);
    }
}
