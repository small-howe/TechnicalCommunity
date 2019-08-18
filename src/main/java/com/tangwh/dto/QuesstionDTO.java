package com.tangwh.dto;

import com.tangwh.entity.User;
import lombok.Data;

@Data
public class QuesstionDTO {

    private Integer id;

    private String title;


    private Long create;

    private Long gmtModified;


    private Integer creator;


    private Integer commentCount;

    private Integer vieweCount;

    private Integer likeCount;


    private String tag;

    private String description;


    private User user;

}
