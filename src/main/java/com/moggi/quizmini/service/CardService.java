package com.moggi.quizmini.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moggi.quizmini.entity.Card;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wechiwin
 * @since 2024-02-07
 */
public interface CardService extends IService<Card> {

    boolean upload(byte[] bytes);

}
