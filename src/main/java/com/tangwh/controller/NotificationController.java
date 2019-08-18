package com.tangwh.controller;

import com.tangwh.dto.NotificationDTO;
import com.tangwh.entity.User;
import com.tangwh.enums.NotificationEnum;
import com.tangwh.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profiles(HttpServletRequest request,
                           @PathVariable(name = "id") Long id) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        NotificationDTO notificationDTO = notificationService.read(id, user);
        if (NotificationEnum.REPLY_COMMENT.getType() == notificationDTO.getType() || NotificationEnum.REPLY_QUESTION.getType() == notificationDTO.getType()) {

            return "redirect:/question/"+notificationDTO.getOuterid();
        }else {


        }

        return null;
    }


}


