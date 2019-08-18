package com.tangwh.controller;

import com.tangwh.dto.PageinationDTO;
import com.tangwh.service.QuesstionServcie;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

// alt + fn+f12 get set 方法（全选属性  shift+向下）  和 创建类
//导包   alt+ 回车
//提取变量  ctrl  + alt +v
// ctrl +e  切换临时的目录
//shift +f12 当前页面最大化
// 双击 shift  查找页面并进入
// ctrl +alt +L 格式化代码
//shift + Enter  换行
@Controller
public class IndexController {



    @Autowired
    private QuesstionServcie quesstionServcie;

    @GetMapping("/")
    public String hello(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "7") Integer size,
                        @RequestParam(name = "search",required = false) String search
                        ) {
        request.getCookies();
        PageinationDTO pageination = quesstionServcie.getqusetionList(search,page, size);
        model.addAttribute("pageination",pageination);

        model.addAttribute("search",search);

        return "index";
    }
}
