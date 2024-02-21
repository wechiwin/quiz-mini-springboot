package com.moggi.quizmini.controller;


import com.moggi.quizmini.entity.Card;
import com.moggi.quizmini.excel.ExcelExportHandler;
import com.moggi.quizmini.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

    @RequestMapping("listByFoPkid")
    public String listByFoPkid(Model model, String foPkid) {
        List<Card> list = cardService.listByFoPkid(foPkid);
        model.addAttribute("cardList", list);
        return "card"; // 返回xx页面
    }
}
