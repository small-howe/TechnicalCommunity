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
public class GithubUser {
    // 得到用户信息 的姓名 id 描述等
    private String name;
    private Long id;
    private String bio;
    private String avatarUrl;

}
