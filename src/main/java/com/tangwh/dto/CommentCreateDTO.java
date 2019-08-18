package com.tangwh.dto;

import lombok.Data;

@Data
public class CommentCreateDTO {
    private Long parentId;
    private Integer type; // 判断是回复  还是 回复评论
    private String content; //评论

}
