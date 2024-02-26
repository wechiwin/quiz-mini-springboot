package com.moggi.quizmini.controller;


import com.moggi.quizmini.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <p>
 * 主页 前端控制器
 * </p>
 *
 * @author wechiwin
 * @since 2024-02-07
 */
@Controller
public class PortalController {

    @Autowired
    private FolderService folderService;

    @GetMapping("/")
    public String portal() {
        // model.addAttribute("folderList", folderList);
        // return "index";
        return "index";
    }


}
