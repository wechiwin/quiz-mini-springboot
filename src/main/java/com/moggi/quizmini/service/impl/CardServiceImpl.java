package com.moggi.quizmini.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moggi.quizmini.entity.Card;
import com.moggi.quizmini.excel.ExcelListener;
import com.moggi.quizmini.framework.dao.EnhanceMapper;
import com.moggi.quizmini.mapper.CardMapper;
import com.moggi.quizmini.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

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

    @Override
    public boolean upload(byte[] bytes) {
        EasyExcel.read(new ByteArrayInputStream(bytes))
                .head(Card.class)
                .registerReadListener(new ExcelListener<Card>() {
                    @Override
                    protected EnhanceMapper<Card> getMapper() {
                        return mapper;
                    }
                }).sheet().doRead();
        return true;
    }
}
