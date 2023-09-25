package com.randps.randomdefence.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateRequest {
    private Long commentId;

    private Long boardId;

    private String bojHandle;

    private Long parentCommentId;

    private String content;
}
