package com.tangwh.controller;

import com.tangwh.dto.CommentDTO;
import com.tangwh.dto.QuesstionDTO;
import com.tangwh.enums.CommentTypeEnum;
import com.tangwh.service.CommentService;
import com.tangwh.service.QuesstionServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuesstionServcie quesstionServcie;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id,
                           Model model) {

        QuesstionDTO quesstionDTO = quesstionServcie.ById(id);

        List<QuesstionDTO>  relatedQuestions = quesstionServcie.selectRelated(quesstionDTO);

        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
//        增加阅读数
        quesstionServcie.incView(id);
        model.addAttribute("question", quesstionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }

}
