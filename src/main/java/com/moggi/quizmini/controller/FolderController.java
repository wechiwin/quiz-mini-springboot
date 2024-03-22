package com.moggi.quizmini.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moggi.quizmini.constant.YesOrNoEnum;
import com.moggi.quizmini.dto.CardDTO;
import com.moggi.quizmini.dto.CardQueryDTO;
import com.moggi.quizmini.dto.FolderDTO;
import com.moggi.quizmini.dto.FolderQueryDTO;
import com.moggi.quizmini.entity.Card;
import com.moggi.quizmini.entity.Folder;
import com.moggi.quizmini.framework.pojo.Converter;
import com.moggi.quizmini.service.CardService;
import com.moggi.quizmini.service.FolderService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private CardService cardService;

    private Converter<FolderDTO, Folder> folderConverter = new Converter<>(FolderDTO.class, Folder.class);

    @PostMapping("searchList")
    public List<FolderDTO> searchList(@RequestBody FolderQueryDTO query) {
        List<Folder> folderList;

        if (StringUtils.isNotBlank(query.getFoName())) {
            folderList = service.list(
                    new LambdaQueryWrapper<Folder>().like(Folder::getFoName, query.getFoName())
            );
        } else {
            folderList = service.list();
        }

        List<FolderDTO> folderDTOList = folderConverter.toDtoList(folderList);

        List<CardDTO> cardList = cardService.searchList(new CardQueryDTO());
        Map<Integer, List<CardDTO>> foPkidAndCardListMap = cardList.stream().collect(Collectors.groupingBy(CardDTO::getFoPkid));

        folderDTOList = folderDTOList.stream()
                .peek(folderDTO -> {
                    List<CardDTO> cards = foPkidAndCardListMap.get(folderDTO.getFoPkid());
                    if (CollectionUtils.isEmpty(cards)) {
                        folderDTO.setIfEmptyCards(YesOrNoEnum.Yes.getVal());
                    }
                })
                .sorted(Comparator.comparing(FolderDTO::getFoName))
                .collect(Collectors.toList());

        return folderDTOList;
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
        // 查询相关cardList
        List<Card> cards = cardService.listByFoPkid(folder.getFoPkid());
        if (CollectionUtils.isNotEmpty(cards)) {
            throw new RuntimeException("存在相关单词，无法删除");
        }
        boolean delete = service.removeById(folder);
        return delete;
    }
}
