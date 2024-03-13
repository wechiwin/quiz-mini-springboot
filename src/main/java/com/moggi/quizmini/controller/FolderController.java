package com.moggi.quizmini.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moggi.quizmini.dto.FolderQueryDTO;
import com.moggi.quizmini.entity.Folder;
import com.moggi.quizmini.service.FolderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    private FolderService service;

    @PostMapping("searchList")
    public List<Folder> searchList(@RequestBody FolderQueryDTO query) {
        List<Folder> folderList;

        if (StringUtils.isNotBlank(query.getFoName())) {
            folderList = service.list(
                    new LambdaQueryWrapper<Folder>().like(Folder::getFoName, query.getFoName())
            );
        } else {
            folderList = service.list();
        }

        folderList = folderList.stream().sorted(Comparator.comparing(Folder::getFoName)).collect(Collectors.toList());
        return folderList;
    }

    @PostMapping("add")
    public boolean add(@RequestBody Folder folder) {
        boolean save = service.save(folder);
        return save;
    }

    @PostMapping("update")
    public boolean update(@RequestBody Folder folder) {
        boolean update = service.updateById(folder);
        return update;
    }

    @PostMapping("delete")
    public boolean delete(@RequestBody Folder folder) {
        boolean delete = service.removeById(folder);
        return delete;
    }
}
