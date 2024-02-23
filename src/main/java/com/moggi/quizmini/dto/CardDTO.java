package com.moggi.quizmini.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
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
public class CardDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private String createTime;

    @ExcelIgnore
    private String modifyTime;

    // =================

    @ExcelProperty(value = "类别名称")
    private String foName;


}
