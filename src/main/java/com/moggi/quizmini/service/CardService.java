package com.moggi.quizmini.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moggi.quizmini.dto.CardDTO;
import com.moggi.quizmini.dto.CardExcelDTO;
import com.moggi.quizmini.dto.CardQueryDTO;
import com.moggi.quizmini.entity.Card;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wechiwin
 * @since 2024-02-07
 */
public interface CardService extends IService<Card> {

    // boolean upload(byte[] bytes);

    List<Card> listByFoPkid(String foPkid);

    boolean upload(List<CardExcelDTO> excelDTOList);

    List<CardDTO> submit(List<CardDTO> cardDTOList);

    List<CardDTO> searchList(CardQueryDTO query);

    Page<CardDTO> searchPage(CardQueryDTO query);

}
