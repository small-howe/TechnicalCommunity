package com.tangwh.dto;

import com.tangwh.entity.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Integer commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private String content;
    private Long likeCount;
    private Integer commentCount;
    private User user;
}
