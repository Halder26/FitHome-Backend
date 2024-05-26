package org.halder.fithomebackend.Services;

import org.halder.fithomebackend.DTOs.CommentRequest;
import org.halder.fithomebackend.DTOs.CommentResponse;
import org.halder.fithomebackend.DTOs.Converter.CommentConverter;
import org.halder.fithomebackend.Entities.Routine;
import org.halder.fithomebackend.Repositories.CommentRepository;
import org.halder.fithomebackend.Repositories.RoutineRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.halder.fithomebackend.Entities.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final RoutineRepository routineRepository;

    public CommentService(CommentRepository commentRepository, RoutineRepository routineRepository) {
        this.commentRepository = commentRepository;
        this.routineRepository = routineRepository;
    }

    public ResponseEntity<CommentResponse> createComment(CommentRequest commentRequest, Long courseId) {
        try {
            Routine routine = routineRepository.findById(courseId).orElse(null);
            if (routine == null) {
                return ResponseEntity.notFound().build();
            }
            Comment comment = new Comment();
            comment.setText(commentRequest.getText());
            comment.setCreatorUsername(commentRequest.getCreatorUsername());
            comment.setCreatedAt(LocalDateTime.now());
            comment.setUpdatedAt(null);
            comment.setRoutine(routine);
            comment = commentRepository.save(comment);
            return ResponseEntity.ok(new CommentResponse(comment.getId(), comment.getText(), comment.getCreatorUsername(), comment.getCreatedAt(), comment.getUpdatedAt()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<String> deleteCommentById(Long commentId) {
        try {
            commentRepository.deleteById(commentId);
            return ResponseEntity.ok("\"Comentario borrado con éxito con ID: " + commentId + "\"");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("\"Error al eliminar el comentario: " + e.getMessage() + "\"");
        }
    }

    public ResponseEntity<String> updateCommentById(CommentRequest commentRequest, Long commentId) {
        try {
            Comment comment = commentRepository.findById(commentId).orElse(null);
            if (comment == null) {
                return ResponseEntity.notFound().build();
            }
            comment.setText(commentRequest.getText());
            comment.setUpdatedAt(LocalDateTime.now());
            commentRepository.save(comment);
            return ResponseEntity.ok("\"Comentario actualizado con éxito con ID: " + comment.getId() + "\"");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("\"Error al actualizar el comentario: " + e.getMessage() + "\"");
        }
    }

    public ResponseEntity<CommentResponse> getCommentById(Long commentId) {
        try {
            Comment comment = commentRepository.findById(commentId).orElse(null);
            if (comment == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(new CommentResponse(comment.getId(), comment.getText(), comment.getCreatorUsername(), comment.getCreatedAt(), comment.getUpdatedAt()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<List<CommentResponse>> getCommentsByRoutineId(Long routineId) {
        try {
            List<Comment> comments = commentRepository.findByRoutineId(routineId);
            if (comments.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(CommentConverter.ListEntityToListDTO(comments));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
