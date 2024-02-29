package com.moggi.quizmini.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
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
public class CardDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    @ApiModelProperty(value = "card主键")
    private Integer caPkid;

    @ExcelIgnore
    @ApiModelProperty(value = "folder主键")
    private Integer foPkid;

    /**
     * 人称
     */
    @ExcelProperty(value = "人称")
    @ApiModelProperty(value = "人称")
    private String grammaticalPerson;

    /**
     * 动词原形/句子原文
     */
    @ExcelProperty(value = "动词原形/句子原文")
    @ApiModelProperty(value = "动词原形/句子原文")
    private String verb;

    /**
     * 动词变位/句子翻译
     */
    @ExcelProperty(value = "动词变位/句子翻译")
    @ApiModelProperty(value = "动词变位/句子翻译")
    private String conjugation;

    /**
     * 例句
     */
    @ExcelProperty(value = "例句")
    @ApiModelProperty(value = "例句")
    private String egSentence;

    /**
     * 重学时间
     */
    @ExcelIgnore
    @ApiModelProperty(value = "重学时间")
    private LocalDate reviewTime;

    /**
     * 是否标熟
     */
    @ExcelIgnore
    @ApiModelProperty(value = "是否标熟")
    private Integer ifDone;

    /**
     * 连续成功次数
     * 如果失败的话，hit_times需要重置
     */
    @ApiModelProperty(value = "连续成功次数")
    private Integer hitTimes;

    @ExcelIgnore
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ExcelIgnore
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime modifyTime;

    /**
     * 最近一次学习时间
     */
    @ApiModelProperty(value = "最近一次学习时间")
    private LocalDate lastReviewTime;

    // =================

    @ExcelProperty(value = "类别名称")
    @ApiModelProperty(value = "类别名称")
    private String foName;

    /**
     * 仅前端使用，用户是否做对
     */
    @ApiModelProperty(value = "仅前端使用，用户是否做对")
    private Integer ifCorrect;

}
