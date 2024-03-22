package com.moggi.quizmini.controller;


import com.moggi.quizmini.entity.Folder;
import com.moggi.quizmini.service.FolderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 * 页面跳转 前端控制器
 *
 * @author wechiwin
 * @since 2024-02-07
 */
@Controller
public class PortalController {

    @Autowired
    private FolderService folderService;

    @ApiOperation(value = "跳转首页")
    @GetMapping("/")
    public String portal() {
        return "index";
    }

    @ApiOperation(value = "跳转管理页面")
    @GetMapping("/portal/manage")
    public String manage() {
        return "manage";
    }

    @ApiOperation(value = "首页跳转card页面时，帮前端记住folder信息")
    @GetMapping("/portal/getFolder/{foPkid}")
    public ModelAndView getFolder(@PathVariable(value = "foPkid") Integer foPkid) {
        ModelAndView mav = new ModelAndView("card");
        Folder folder = folderService.getById(foPkid);
        mav.addObject("folder", folder);
        return mav;
    }

}
