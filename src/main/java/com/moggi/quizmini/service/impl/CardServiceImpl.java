package com.moggi.quizmini.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moggi.quizmini.constant.ForgettingCurveEnum;
import com.moggi.quizmini.constant.YesOrNoEnum;
import com.moggi.quizmini.dto.CardDTO;
import com.moggi.quizmini.dto.CardExcelDTO;
import com.moggi.quizmini.dto.CardQueryDTO;
import com.moggi.quizmini.entity.Card;
import com.moggi.quizmini.entity.Folder;
import com.moggi.quizmini.framework.pojo.Converter;
import com.moggi.quizmini.mapper.CardMapper;
import com.moggi.quizmini.mapper.FolderMapper;
import com.moggi.quizmini.service.CardService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
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
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements CardService {

    @Autowired
    private CardMapper mapper;

    @Autowired
    private FolderMapper folderMapper;

    // @Override
    // @Transactional
    // public boolean upload(byte[] bytes) {
    //     EasyExcel.read(new ByteArrayInputStream(bytes))
    //             .head(Card.class)
    //             .registerReadListener(new ExcelListener<Card>() {
    //                 @Override
    //                 protected EnhanceMapper<Card> getMapper() {
    //                     return mapper;
    //                 }
    //             }).sheet().doRead();
    //     return true;
    // }

    private Converter<CardDTO, Card> cardConverter = new Converter<>(CardDTO.class, Card.class);

    @Override
    @Transactional
    public boolean upload(List<CardExcelDTO> excelDTOList) {
        // 查询folder信息
        Map<String, List<CardExcelDTO>> groupByFoName = excelDTOList.stream().collect(Collectors.groupingBy(CardExcelDTO::getFoName));
        Set<String> foNames = groupByFoName.keySet();
        // Set<String> foNames = excelDTOList.stream().map(CardExcelDTO::getFoName).collect(Collectors.toSet());
        List<Folder> folderList = folderMapper.selectList(new LambdaQueryWrapper<Folder>().in(Folder::getFoName, foNames));
        if (CollectionUtils.isEmpty(folderList)) return false;
        Map<String, Integer> foNameAndFoPkidMap = folderList.stream().collect(Collectors.toMap(Folder::getFoName, Folder::getFoPkid, (v1, v2) -> v2));

        // 查询foName相关card
        List<Card> relatedCards = mapper.selectList(
                new LambdaQueryWrapper<Card>()
                        .in(Card::getFoPkid, foNameAndFoPkidMap.values())
        );
        Map<Integer, List<Card>> foPkidAndRelatedMap = relatedCards.stream().collect(Collectors.groupingBy(Card::getFoPkid));

        for (Map.Entry<String, List<CardExcelDTO>> entry : groupByFoName.entrySet()) {
            String foName = entry.getKey();
            List<CardExcelDTO> dtoList = entry.getValue();

            Integer foPkid = foNameAndFoPkidMap.get(foName);
            List<String> existCardNames = Optional.ofNullable(foPkidAndRelatedMap.get(foPkid)).orElse(Collections.emptyList())
                    .stream()
                    .map(card -> card.getGrammaticalPerson() + "-" + card.getVerb())
                    .collect(Collectors.toList());

            List<Card> cards = new ArrayList<>(dtoList.size());
            for (CardExcelDTO excelDTO : dtoList) {
                // 检查数据是否重复
                if (existCardNames.contains(excelDTO.getGrammaticalPerson() + "-" + excelDTO.getVerb())) {
                    continue;
                }
                Card card = new Card();
                BeanUtils.copyProperties(excelDTO, card);
                card.setFoPkid(foPkid);
                card.setReviewTime(LocalDate.now().plusDays(1));
                card.setIfDone(YesOrNoEnum.Yes.getVal());
                cards.add(card);
            }
            if (CollectionUtils.isNotEmpty(cards)) {
                int i = mapper.insertBatch(cards);
            }
        }
        return true;
    }

    @Override
    public List<Card> listByFoPkid(String foPkid) {
        LambdaQueryWrapper<Card> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Card::getFoPkid, foPkid);
        wrapper.orderByAsc(Card::getReviewTime);
        List<Card> list = mapper.selectList(wrapper);
        return list;
    }

    @Override
    @Transactional
    public Boolean submit(List<CardDTO> cardDTOList) {
        if (CollectionUtils.isEmpty(cardDTOList)) return false;
        // List<CardDTO> wrongList = new ArrayList<>();
        for (CardDTO cardDTO : cardDTOList) {
            // 答错
            if (cardDTO.getIfCorrect().equals(YesOrNoEnum.No.getVal())) {
                cardDTO.setHitTimes(0);
                cardDTO.setReviewTime(LocalDate.now().plusDays(1));
                // 加入错题集
                // wrongList.add(cardDTO);
            } else { // 答对
                cardDTO.setHitTimes(cardDTO.getHitTimes() + 1);
                int days = ForgettingCurveEnum.getDaysByHitTimes(cardDTO.getHitTimes());
                if (days == -1) {
                    cardDTO.setIfDone(YesOrNoEnum.Yes.getVal());
                } else {
                    cardDTO.setReviewTime(LocalDate.now().plusDays(days));
                }
            }
            cardDTO.setLastReviewTime(LocalDate.now());
        }
        List<Card> cardList = cardConverter.toEntityList(cardDTOList);
        for (Card card : cardList) {
            int i = mapper.updateById(card);
        }
        // todo 待解决sqlite批量更新的问题
        // int i = mapper.updateBatch(cardList);
        return true;


    }

    @Override
    public List<CardDTO> searchList(CardQueryDTO query) {
        LambdaQueryWrapper<Card> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNull(Card::getLastReviewTime)
                .or()
                .ne(Card::getLastReviewTime, LocalDate.now()); // 当天未学的
        wrapper.eq(Card::getFoPkid, query.getFoPkid());
        wrapper.orderByAsc(Card::getReviewTime); // 按照复习时间升序
        wrapper.last("limit 10");
        List<Card> list = mapper.selectList(wrapper);
        List<CardDTO> cardDTOS = cardConverter.toDtoList(list);
        return cardDTOS;
    }
}
