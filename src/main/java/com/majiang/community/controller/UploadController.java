package com.majiang.community.controller;

import com.majiang.community.dto.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UploadController {

    @ResponseBody
    @RequestMapping("/file/upload")
    public FileDTO questionUpload() {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/images/upload/t01a58e5c813365a87b.jpg");
        return fileDTO;
    }


}
