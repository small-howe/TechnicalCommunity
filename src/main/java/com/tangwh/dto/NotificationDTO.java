package com.tangwh.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    // 通知的人
    private Long notifier;
    private String notifierName;
    private String outerTitle;
    private long outerid;
    // 判断是 评论  还是回复评论
    private String typeName;
    private Integer type;

}
