package com.moggi.quizmini.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moggi.quizmini.constant.YesOrNoEnum;
import com.moggi.quizmini.dto.CardExcelDTO;
import com.moggi.quizmini.entity.Card;
import com.moggi.quizmini.entity.Folder;
import com.moggi.quizmini.mapper.CardMapper;
import com.moggi.quizmini.mapper.FolderMapper;
import com.moggi.quizmini.service.CardService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Override
    public boolean upload(List<CardExcelDTO> excelDTOList) {
        // 查询folder信息
        Map<String, List<CardExcelDTO>> groupByFoName = excelDTOList.stream().collect(Collectors.groupingBy(CardExcelDTO::getFoName));
        Set<String> foNames = groupByFoName.keySet();
        // Set<String> foNames = excelDTOList.stream().map(CardExcelDTO::getFoName).collect(Collectors.toSet());
        List<Folder> folderList = folderMapper.selectList(new LambdaQueryWrapper<Folder>().in(Folder::getFoName, foNames));
        if (CollectionUtils.isEmpty(folderList)) return false;
        Map<String, Integer> foNameAndPkidMap = folderList.stream().collect(Collectors.toMap(Folder::getFoName, Folder::getFoPkid, (v1, v2) -> v2));
        for (Map.Entry<String, List<CardExcelDTO>> entry : groupByFoName.entrySet()) {
            String foName = entry.getKey();
            List<CardExcelDTO> dtoList = entry.getValue();

            Integer foPkid = foNameAndPkidMap.get(foName);

            List<Card> cards = new ArrayList<>(dtoList.size());
            for (CardExcelDTO excelDTO : dtoList) {
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
        List<Card> list = mapper.selectList(wrapper);
        return list;
    }
}
