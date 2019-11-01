package com.majiang.community.controller;

import com.majiang.community.dto.NotificationDTO;
import com.majiang.community.dto.ResultDTO;
import com.majiang.community.enums.NotificationTypeEnum;
import com.majiang.community.exception.CustomizeErrorCode;
import com.majiang.community.model.User;
import com.majiang.community.service.NotificationServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;


@Controller
public class NotificationController {

    @Autowired
    private NotificationServer notificationServer;

    @GetMapping("/notification/{id}")
    public String profile(@PathVariable("id") Long id,
                          HttpServletRequest request){
        User user =(User) request.getSession().getAttribute("user");
        if (user==null){
            return "redirect:/";
        }
        NotificationDTO notificationDTO = notificationServer.read(id, user);
        if (NotificationTypeEnum.REPLY_QUESTION.getType()==notificationDTO.getType() ||
        NotificationTypeEnum.REPLY_COMMENT.getType()==notificationDTO.getType()){
            return "redirect:/question/"+notificationDTO.getOuter_id();
        }else{
            return "redirect:/";
        }
    }
}
