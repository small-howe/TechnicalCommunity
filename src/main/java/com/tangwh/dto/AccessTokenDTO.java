package com.tangwh.dto;


import lombok.Data;

/**
 * Created by codedrinker on 2019/4/24.
 */
// alt + fn+f12 get set 方法（全选属性  shift+向下）  和 创建类
//导包   alt+ 回车
//提取变量  ctrl  + alt +v
// ctrl +e  切换临时的目录
//shift +f12 当前页面最大化
// 双击 shift  查找页面并进入
// ctrl +alt +L 格式化代码
//shift + Enter  换行

@Data
public class AccessTokenDTO {
    //必填。您在注册时从GitHub收到的客户ID 。
    private String client_id;
    // 需要。您从GitHub收到的GitHub应用程序的客户机密
    private String client_secret;
    //需要。您收到的代码作为对第1步的回复。
    private String code;
    //	应用程序中的URL，用于在授权后发送用户
    private String redirect_uri;
    //您在步骤1中提供的不可思议的随机字符串。
    private String state;
}
