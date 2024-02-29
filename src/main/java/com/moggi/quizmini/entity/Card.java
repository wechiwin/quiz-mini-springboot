package com.moggi.quizmini.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;

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
@NoArgsConstructor
@AllArgsConstructor
@TableName("card")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ca_pkid", type = IdType.ASSIGN_ID)
    private Integer caPkid;

    @TableField(value = "fo_pkid", jdbcType = JdbcType.INTEGER)
    private Integer foPkid;

    /**
     * 人称
     */
    @TableField("grammatical_person")
    private String grammaticalPerson;

    /**
     * 动词原形/句子原文
     */
    @TableField("verb")
    private String verb;

    /**
     * 动词变位/句子翻译
     */
    @TableField("conjugation")
    private String conjugation;

    /**
     * 例句
     */
    @TableField("eg_sentence")
    private String egSentence;

    /**
     * 重学时间
     */
    @TableField("review_time")
    private LocalDate reviewTime;

    /**
     * 是否标熟
     */
    @TableField("if_done")
    private Integer ifDone;

    /**
     * 连续成功次数
     * 如果失败的话，hit_times需要重置
     */
    @TableField("hit_times")
    private Integer hitTimes;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "modify_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;

    /**
     * 最近一次学习时间
     */
    @TableField("last_review_time")
    private LocalDate lastReviewTime;

}
