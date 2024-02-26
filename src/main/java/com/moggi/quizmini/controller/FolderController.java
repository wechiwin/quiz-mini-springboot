package com.moggi.quizmini.controller;


import com.moggi.quizmini.entity.Folder;
import com.moggi.quizmini.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 文件夹 前端控制器
 *
 * @author wechiwin
 * @since 2024-02-07
 */
@RestController
@RequestMapping("/folder")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @PostMapping("searchList")
    public List<Folder> searchList() {
        List<Folder> folderList = folderService.list();
        // List<Folder> folderList = new ArrayList<>();
        // for (int i = 0; i < 20; i++) {
        //     Folder folder = new Folder();
        //     folder.setFoName("asdf" + i);
        //     folderList.add(folder);
        // }

        // model.addAttribute("folderList", folderList);
        // return "index";
        return folderList;
    }

    // @PostMapping("add")
    // public String add(String folderName) {
    //     folderService.add(folderName);
    //     return "redirect:/folder/list"; // 重定向到list接口
    // }

}
