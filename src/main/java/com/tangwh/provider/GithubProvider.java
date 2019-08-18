package com.tangwh.provider;

import com.alibaba.fastjson.JSON;
import com.tangwh.dto.AccessTokenDTO;
import com.tangwh.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

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
@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            System.out.println("Token:"+token);
            return token;
        } catch (Exception e) {

        }
        return null;
    }


    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (Exception e) {

        }
        return null;
    }

}
