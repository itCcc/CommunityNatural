package com.majiang.community.controller;

import com.majiang.community.dto.PaginationDTO;
import com.majiang.community.model.User;
import com.majiang.community.service.NotificationServer;
import com.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationServer notificationServer;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @RequestParam(name="page",defaultValue = "1") Integer page,
                          @RequestParam(name="size",defaultValue = "5") Integer size,
                          @PathVariable(name="action") String action,
                          Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

            if ("questions".equals(action)) {
                model.addAttribute("section", "questions");
                model.addAttribute("sectionName", "我的话题");
                PaginationDTO paginationDTO = questionService.listUser(user.getId(), page, size);

                model.addAttribute("profilePaginationDTO", paginationDTO);
            }
            if ("replies".equals(action)) {
                PaginationDTO paginationDTO = notificationServer.list(user.getId(), page, size);
                if (paginationDTO == null) {
                    model.addAttribute("message", "你现在没有任何消息哦，尽快与别人互动吧！");
                }
                model.addAttribute("section", "replies");
                model.addAttribute("sectionName", "最新回复");
                model.addAttribute("paginationDTO",paginationDTO);
            }
            return "profile";
        }

}

