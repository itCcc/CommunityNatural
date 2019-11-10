package com.majiang.community.controller;


import com.majiang.community.dto.PaginationDTO;
import com.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
public class indexController {

    @Autowired
    private QuestionService questionService;


    /**
     * 跳转到首页
     * @param
     * @return
     */
    @GetMapping("/")
    public String hello(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "8") Integer size,
            @RequestParam(name = "search", required = false) String search,
            Model model) {
        PaginationDTO paginationDTO = questionService.list(search, page, size);
        if (paginationDTO == null) {
            model.addAttribute("indexPaginationDTO", paginationDTO);
            model.addAttribute("message", "您搜索的问题不存在哦，还请您重新输入关键字！");
            return "index";
        } else {
            model.addAttribute("indexPaginationDTO", paginationDTO);
            model.addAttribute("search", search);
            return "index";
        }
    }


}