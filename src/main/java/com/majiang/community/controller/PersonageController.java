package com.majiang.community.controller;

import com.majiang.community.dto.PaginationDTO;
import com.majiang.community.model.User;
import com.majiang.community.service.QuestionService;
import com.majiang.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class PersonageController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/personage/{id}")
    public String personage(@PathVariable("id") Long id,
                            @RequestParam(name = "page", defaultValue = "1") Integer page,
                            @RequestParam(name = "size", defaultValue = "5") Integer size,
                            Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        PaginationDTO paginationDTO = questionService.listUser(user.getId(), page, size);
        model.addAttribute("PaginationDTO", paginationDTO);
        return "personage";
    }
}
