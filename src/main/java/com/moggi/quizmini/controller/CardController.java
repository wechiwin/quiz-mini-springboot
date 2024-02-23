package com.moggi.quizmini.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.moggi.quizmini.dto.CardDTO;
import com.moggi.quizmini.dto.CardExcelDTO;
import com.moggi.quizmini.entity.Card;
import com.moggi.quizmini.entity.Folder;
import com.moggi.quizmini.excel.ExcelExportHandler;
import com.moggi.quizmini.excel.ExcelReadListener;
import com.moggi.quizmini.service.CardService;
import com.moggi.quizmini.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
@RestController
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

    @PostMapping("listByFoPkid")
    public ModelAndView listByFoPkid(@RequestParam(value = "foPkid") String foPkid, Model model) {
        ModelAndView mav = new ModelAndView("card");
        List<Card> list = service.listByFoPkid(foPkid);
        Folder folder = folderService.getById(foPkid);
        mav.addObject("folder", folder);
        mav.addObject("cardList", list);
        return mav;
    }

    @PostMapping("completeBatch")
    public void completeBatch(List<CardDTO> cardDTOList) {
        Boolean flag = service.completeBatch(cardDTOList);
    }
}
