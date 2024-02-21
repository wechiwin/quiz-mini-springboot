package com.moggi.quizmini.controller;


import com.moggi.quizmini.entity.Folder;
import com.moggi.quizmini.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 文件夹 前端控制器
 * </p>
 *
 * @author wechiwin
 * @since 2024-02-07
 */
@Controller
public class FolderController {

    @Autowired
    private FolderService folderService;

    @RequestMapping("/")
    public String home(Model model) {
        List<Folder> folderList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Folder folder = new Folder();
            folder.setFoName("asdf" + i);
            folderList.add(folder);
        }

        model.addAttribute("folderList", folderList);
        return "index";
    }

    @RequestMapping("/folder/add")
    public String add(String folderName) {
        folderService.add(folderName);
        return "redirect:/folder/list"; // 重定向到list接口
    }

    @RequestMapping("/folder/list")
    public String list(Model model, List<Folder> list) {
        List<Folder> folderList = folderService.list();
        model.addAttribute("folderList", folderList);
        return "Quiz.js"; // 返回xx页面
    }

}
