package org.halder.fithomebackend.DTOs.Converter;

import org.halder.fithomebackend.DTOs.CommentResponse;
import org.halder.fithomebackend.Entities.Comment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommentConverter {
    public static List<CommentResponse> ListEntityToListDTO(List<Comment> comments) {
        if (comments == null) {
            return new ArrayList<>();
        }
        List<CommentResponse> commentsDTO = new ArrayList<>();
        for (Comment comment : comments) {
            commentsDTO.add(new CommentResponse(
                    comment.getId(),
                    comment.getText(),
                    comment.getCreatorUsername(),
                    comment.getCreatedAt(),
                    comment.getUpdatedAt()
            ));
        }
        return commentsDTO;
    }
}
