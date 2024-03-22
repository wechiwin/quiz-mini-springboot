package com.moggi.quizmini.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moggi.quizmini.dto.CardDTO;
import com.moggi.quizmini.dto.CardExcelDTO;
import com.moggi.quizmini.dto.CardQueryDTO;
import com.moggi.quizmini.entity.Card;
import com.moggi.quizmini.excel.ExcelExportHandler;
import com.moggi.quizmini.excel.ExcelReadListener;
import com.moggi.quizmini.service.CardService;
import com.moggi.quizmini.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @ResponseBody
    public boolean upload(@RequestParam("file") MultipartFile file) throws Exception {
        ExcelReadListener<CardExcelDTO> readListener = new ExcelReadListener<>();
        ExcelReader excelReader = EasyExcel.read(file.getInputStream(), CardExcelDTO.class, readListener).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        List<CardExcelDTO> cacheList = readListener.getCacheList();
        excelReader.finish();

        return service.upload(cacheList);
    }

    @PostMapping("downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) {
        excelExportHandler.downloadTemplate(response, "template", CardExcelDTO.class);
    }

    @PostMapping("submit")
    @ResponseBody
    public List<CardDTO> submit(@RequestBody List<CardDTO> cardList) {
        return service.submit(cardList);
    }

    @PostMapping("searchList")
    @ResponseBody
    public List<CardDTO> searchList(@RequestBody CardQueryDTO query) {
        return service.searchList(query);
    }

    @PostMapping("searchPage")
    @ResponseBody
    public Page<CardDTO> searchPage(@RequestBody CardQueryDTO query) {
        return service.searchPage(query);
    }

    @PostMapping("add")
    @ResponseBody
    public boolean add(@RequestBody Card card) {
        boolean save = service.save(card);
        return save;
    }

    @PostMapping("update")
    @ResponseBody
    public boolean update(@RequestBody Card card) {
        boolean update = service.updateById(card);
        return update;
    }

    @PostMapping("delete")
    @ResponseBody
    public boolean delete(@RequestBody Card card) {
        boolean delete = service.removeById(card);
        return delete;
    }

    @PostMapping("listToStudy")
    @ResponseBody
    public List<CardDTO> listToStudy(@RequestBody CardQueryDTO query) {
        return service.listToStudy(query);
    }
}
