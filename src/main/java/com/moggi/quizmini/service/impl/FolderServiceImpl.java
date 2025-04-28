package com.moggi.quizmini.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moggi.quizmini.constant.YesOrNoEnum;
import com.moggi.quizmini.dto.FolderDTO;
import com.moggi.quizmini.dto.FolderQueryDTO;
import com.moggi.quizmini.entity.Card;
import com.moggi.quizmini.entity.Folder;
import com.moggi.quizmini.framework.dao.EnhanceService;
import com.moggi.quizmini.framework.dao.query.WrapperBuilderUtil;
import com.moggi.quizmini.mapper.CardMapper;
import com.moggi.quizmini.mapper.FolderMapper;
import com.moggi.quizmini.service.CardService;
import com.moggi.quizmini.service.FolderService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wechiwin
 * @since 2024-02-07
 */
@Service
// public class FolderServiceImpl extends ServiceImpl<FolderMapper, Folder> implements FolderService {
public class FolderServiceImpl extends EnhanceService<FolderMapper, FolderDTO, Folder> implements FolderService {
    @Autowired
    private FolderMapper mapper;
    // private Converter<FolderDTO, Folder> folderConverter = new Converter<>(FolderDTO.class, Folder.class);
    @Autowired
    private CardService cardService;
    @Autowired
    private CardMapper cardMapper;

    @Override
    public boolean add(String folderName) {
        Folder folder = new Folder();
        folder.setFoName(folderName);
        return mapper.insert(folder) > 0;
    }

    @Override
    public List<FolderDTO> searchList(FolderQueryDTO query) {
        QueryWrapper<Folder> folderQueryWrapper = WrapperBuilderUtil.buildQueryWrapper(Folder.class, query);
        List<Folder> folders = mapper.selectList(folderQueryWrapper);
        List<FolderDTO> folderDTOList = toDtoList(folders);

        List<Integer> foPkids = folderDTOList.stream().map(FolderDTO::getFoPkid).collect(Collectors.toList());
        List<Card> cardList = cardMapper.selectList(new LambdaQueryWrapper<Card>().in(Card::getFoPkid, foPkids));
        // List<CardDTO> cardList = cardService.searchList(new CardQueryDTO().setFoPkidList(foPkids));
        Map<Integer, List<Card>> foPkidAndCardListMap = cardList.stream().collect(Collectors.groupingBy(Card::getFoPkid));

        folderDTOList = folderDTOList.stream()
                // 判断当日是否学完
                .peek(folderDTO -> {
                    List<Card> cards = foPkidAndCardListMap.get(folderDTO.getFoPkid());
                    if (CollectionUtils.isEmpty(cards)) return;
                    List<Card> doneCards = cards.stream().filter(item -> item.getIfDone() == YesOrNoEnum.Yes.getVal()).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(doneCards)) {
                        folderDTO.setPercentage((double) doneCards.size() / (double) cards.size());
                    }
                    // List<Card> allUnDoneCards = cards.stream().filter(item -> item.getIfDone() != 1).collect(Collectors.toList());
                    List<Card> notDoneCards = cards.stream()
                            .filter(item -> item.getIfDone() != 1 && !item.getReviewTime().isAfter(LocalDate.now()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(notDoneCards)) {
                        folderDTO.setIfEmptyCards(YesOrNoEnum.Yes.getVal());
                    }
                })
                // 排序
                .sorted(Comparator.comparing(FolderDTO::getFoName))
                .collect(Collectors.toList());

        return folderDTOList;
    }
}
