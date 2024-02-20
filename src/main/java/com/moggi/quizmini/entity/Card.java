package com.moggi.quizmini.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
    @ExcelIgnore
    private Integer caPkid;

    @ExcelIgnore
    private Integer foPkid;

    /**
     * 人称
     */
    @ExcelProperty(value = "人称")
    private String grammaticalPerson;

    /**
     * 动词原形/句子原文
     */
    @ExcelProperty(value = "动词原形/句子原文")
    private String verb;

    /**
     * 动词变位/句子翻译
     */
    @ExcelProperty(value = "动词变位/句子翻译")
    private String conjugation;

    /**
     * 例句
     */
    @ExcelProperty(value = "例句")
    private String egSentence;

    /**
     * 重学时间
     */
    @ExcelIgnore
    private String reviewTime;

    /**
     * 是否标熟
     */
    @ExcelIgnore
    private String ifDone;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifyTime;


}
