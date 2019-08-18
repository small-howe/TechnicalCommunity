package com.tangwh.controller;

import com.tangwh.dto.AccessTokenDTO;
import com.tangwh.dto.GithubUser;
import com.tangwh.entity.User;
import com.tangwh.mapper.UserMapper;
import com.tangwh.provider.GithubProvider;
import com.tangwh.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


/**
 * Created by codedrinker on 2019/4/24.
 */
// alt + fn+f12 get set 方法（全选属性  shift+向下）  和 创建类
//导包   alt+ 回车  取掉没用的代码
//提取变量  ctrl  + alt +v
// ctrl +e  切换临时的目录
//shift +f12 当前页面最大化
// 双击 shift  查找页面并进入
// ctrl +alt +L 格式化代码
//shift + Enter  换行
// atrl +alt +o  去掉没用的包
@Controller
// 打日志
@Slf4j
public class AuthorizeController {

    /**
     *
     */
    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private  UserService userService;

//    // Value 读取配置文件 的内容

    @Value("${github.client_id}")
    private String Client_id;
    @Value("${github.client_secret}")
    private String Client_secret;
    @Value("${github.Redirect_uri}")
    private String Redirect_uri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                             HttpServletResponse response
                           ) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(Client_id);
        accessTokenDTO.setClient_secret(Client_secret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(Redirect_uri);
        accessTokenDTO.setState(state);
        // access_toke  用户的身份证ID
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);

        GithubUser githubUser = githubProvider.getUser(accessToken);
        // 判断 登录成功  是否为空 从而获取用户信息
        if (githubUser != null && githubUser.getId() !=null) {
            User user = new User();
            // 使用UUID 生成一个token  保存在用户数据库信息中
            String tokens = UUID.randomUUID().toString();
            user.setToken(tokens);
            user.setName(githubUser.getName());
            // 转换成Stirng 类型
           // user.setAccount_id(String.valueOf(githubUser.getId()));
            user.setAccountId(String.valueOf(githubUser.getId()));
            // 保存用户头像
          //  user.setAvatar_url(githubUser.getAvatarUrl());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            // 创建或者更新用户
            userService.createUpdate(user);
            //  使用UUID 生成的token  保存在Cookie
            response.addCookie(new Cookie("token", tokens));
            //Redirect方法重定向的访问过程结束后，浏览器地址栏中显示的URL会发生改变，由初始的URL地址变成重定向的目标URL；
            return "redirect:/";
        } else {
            // 将githubUser 打印 在{}中
            log.error("callback get github err,{}",githubUser);
            // 登录失败重新登录
            return "redirect:/";
        }
    }

    @GetMapping("/logOut")
    public  String logOut(HttpServletRequest request,
                          HttpServletResponse response){
           request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
// Session 销毁  session.invalidate;
    }



}
