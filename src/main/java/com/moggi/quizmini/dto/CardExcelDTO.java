package com.moggi.quizmini.dto;

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
public class CardExcelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "类别名称")
    private String foName;

    @ExcelProperty(value = "动词原形/句子原文")
    private String verb;

    @ExcelProperty(value = "人称")
    private String grammaticalPerson;

    @ExcelProperty(value = "动词变位/句子翻译")
    private String conjugation;

    @ExcelProperty(value = "例句")
    private String egSentence;

}
