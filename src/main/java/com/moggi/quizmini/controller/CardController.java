package com.moggi.quizmini.controller;


import com.moggi.quizmini.entity.Card;
import com.moggi.quizmini.entity.Folder;
import com.moggi.quizmini.excel.ExcelExportHandler;
import com.moggi.quizmini.service.CardService;
import com.moggi.quizmini.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 卡片 前端控制器
 * </p>
 *
 * @author wechiwin
 * @since 2024-02-07
 */
@Controller
@RequestMapping("/card")
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private FolderService folderService;

    @Autowired
    private ExcelExportHandler excelExportHandler;

    @RequestMapping("upload")
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {
        byte[] bytes = file.getBytes();
        boolean flag = cardService.upload(bytes);
        return "redirect:/folder/list"; // 重定向到list接口
    }

    @RequestMapping("downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) {
        excelExportHandler.downloadTemplate(response, "人员表", Card.class);
    }

    @GetMapping("listByFoPkid")
    @ResponseBody
    public ModelAndView listByFoPkid(@RequestParam(value = "foPkid") String foPkid, Model model) {
        ModelAndView mav = new ModelAndView("card");
        List<Card> list = cardService.listByFoPkid(foPkid);
        Folder folder = folderService.getById(foPkid);
        mav.addObject("folder", folder);
        mav.addObject("cardList", list);
        return mav; // 返回xx页面
    }
}
