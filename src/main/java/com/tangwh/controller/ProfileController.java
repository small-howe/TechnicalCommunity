package com.tangwh.controller;

import com.tangwh.dto.PageinationDTO;
import com.tangwh.entity.User;
import com.tangwh.service.NotificationService;
import com.tangwh.service.QuesstionServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {


    @Autowired
    private QuesstionServcie quesstionServcie;

    @Autowired
    NotificationService notificationService;
    // 点击我的问题   动态获取页面
    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          @RequestParam(name ="page",defaultValue ="1") Integer page,
                          @RequestParam(name = "size",defaultValue ="5") Integer size,
                          Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            return "redirect:/";
        }
        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的问题");

            PageinationDTO pageinationDTO = quesstionServcie.getqusetionList(user.getId(), page, size);
            model.addAttribute("pageination",pageinationDTO);
        } else if ("replies".equals(action)) {
            PageinationDTO pageinationDTO=notificationService.list(user.getId(), page, size);
            model.addAttribute("pageination",pageinationDTO);
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }
        return "profile";
    }


}
