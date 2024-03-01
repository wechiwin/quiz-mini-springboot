package com.moggi.quizmini.mapper;


import com.moggi.quizmini.dto.CardQueryDTO;
import com.moggi.quizmini.entity.Card;
import com.moggi.quizmini.framework.dao.EnhanceMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wechiwin
 * @since 2024-02-07
 */
public interface CardMapper extends EnhanceMapper<Card> {

    List<Card> searchList(@Param("query") CardQueryDTO query);
}
