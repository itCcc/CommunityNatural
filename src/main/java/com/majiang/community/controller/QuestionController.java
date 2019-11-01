package com.majiang.community.controller;

import com.majiang.community.dto.CommentDTO;
import com.majiang.community.dto.QuestionDTO;
import com.majiang.community.enums.CommentTypeEnum;
import com.majiang.community.mapper.IQuestionDao;
import com.majiang.community.model.Question;
import com.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id, Model model){
        questionService.incView(id);
        QuestionDTO questionDTO = questionService.finById(id);
        List<CommentDTO> comments = questionService.findByQuestionId(id,CommentTypeEnum.QUESTION.getType());
        List<QuestionDTO> relatedQuestionDTO = questionService.selectRelated(questionDTO);
        model.addAttribute("questionDTO",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestionDTO",relatedQuestionDTO);
        return "question";
    }
}
