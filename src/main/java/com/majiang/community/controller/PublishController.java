package com.majiang.community.controller;

import com.majiang.community.dto.QuestionDTO;
import com.majiang.community.model.Question;
import com.majiang.community.model.User;
import com.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
public class PublishController {


    @Autowired
    private QuestionService questionService;

    /**
     * 跳转到publish
     * @return
     */
    @GetMapping("/publish")
    public String Publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam(value="title",required = false) String title,
                            @RequestParam(value="description",required = false) String description,
                            @RequestParam(value = "tag",required = false) String tag,
                            @RequestParam(value = "id",required = false) Long id,
                            HttpServletRequest request,
                            Model model){
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if (title == null || title == ""){
            model.addAttribute("error","标题不能为空");
            System.out.println("标题不能为空");
            return "publish";
        }
        if (description == null || description == ""){
            model.addAttribute("error","补充不能为空");
            System.out.println("补充不能为空");
            return "publish";
        }
        if (tag == null || tag == ""){
            model.addAttribute("error","标签不能为空");
            System.out.println("标签不能为空");
            return "publish";
        }
        User user = (User) request.getSession().getAttribute("user");
        Question question=new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);
       return "redirect:/";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable("id") Long id,
                       Model model){
        QuestionDTO questionDTO = questionService.finById(id);
        model.addAttribute("title",questionDTO.getTitle());
        model.addAttribute("description",questionDTO.getDescription());
        model.addAttribute("tag",questionDTO.getTag());
        model.addAttribute("id",questionDTO.getId());
        return "publish";
    }
}
