package com.moggi.quizmini.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.moggi.quizmini.dto.CardDTO;
import com.moggi.quizmini.dto.CardExcelDTO;
import com.moggi.quizmini.dto.CardQueryDTO;
import com.moggi.quizmini.entity.Card;
import com.moggi.quizmini.entity.Folder;
import com.moggi.quizmini.excel.ExcelExportHandler;
import com.moggi.quizmini.excel.ExcelReadListener;
import com.moggi.quizmini.service.CardService;
import com.moggi.quizmini.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 卡片 前端控制器
 *
 * @author wechiwin
 * @since 2024-02-07
 */
@Controller
@RequestMapping("/card")
public class CardController {
    @Autowired
    private CardService service;
    @Autowired
    private FolderService folderService;

    @Autowired
    private ExcelExportHandler excelExportHandler;

    @PostMapping("upload")
    public void upload(@RequestParam("file") MultipartFile file) throws Exception {
        ExcelReadListener<CardExcelDTO> readListener = new ExcelReadListener<>();
        ExcelReader excelReader = EasyExcel.read(file.getInputStream(), CardExcelDTO.class, readListener).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        List<CardExcelDTO> cacheList = readListener.getCacheList();
        excelReader.finish();

        boolean flag = service.upload(cacheList);
        // todo 上传成功后是否需要跳转页面
        // boolean flag = cardService.upload(bytes);
        // return "redirect:/folder/list"; // 重定向到list接口
    }

    @PostMapping("downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) {
        excelExportHandler.downloadTemplate(response, "template", Card.class);
    }

    @GetMapping("/listByFoPkid/{foPkid}")
    public ModelAndView listByFoPkid(@PathVariable(value = "foPkid") Integer foPkid) {
        ModelAndView mav = new ModelAndView("card");
        Folder folder = folderService.getById(foPkid);
        mav.addObject("folder", folder);
        return mav;
    }

    @PostMapping("submit")
    @ResponseBody
    public void submit(@RequestBody List<CardDTO> cardList) {
        Boolean flag = service.submit(cardList);
    }

    @PostMapping("searchList")
    @ResponseBody
    public List<CardDTO> searchList(@RequestBody CardQueryDTO query) {
        return service.searchList(query);
    }
}
