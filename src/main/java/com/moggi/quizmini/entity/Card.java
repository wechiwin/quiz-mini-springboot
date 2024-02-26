package com.moggi.quizmini.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 卡片对象
 * </p>
 *
 * @author wechiwin
 * @since 2024-02-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ca_pkid", type = IdType.ASSIGN_ID)
    private Integer caPkid;

    private Integer foPkid;

    /**
     * 人称
     */
    private String grammaticalPerson;

    /**
     * 动词原形/句子原文
     */
    private String verb;

    /**
     * 动词变位/句子翻译
     */
    private String conjugation;

    /**
     * 例句
     */
    private String egSentence;

    /**
     * 重学时间
     */
    private LocalDate reviewTime;

    /**
     * 是否标熟
     */
    private Integer ifDone;

    /**
     * 连续成功次数
     * 如果失败的话，hit_times需要重置
     */
    private Integer hitTimes;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;

    /**
     * 最近一次学习时间
     */
    private LocalDate lastReviewTime;

}
