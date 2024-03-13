package com.moggi.quizmini.dto;

import com.moggi.quizmini.framework.pojo.QueryDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 卡片对象
 *
 * @author wechiwin
 * @since 2024-02-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CardQueryDTO extends QueryDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "card主键")
    private Integer caPkid;

    @ApiModelProperty(value = "folder主键")
    private Integer foPkid;

    /**
     * 人称
     */
    @ApiModelProperty(value = "人称")
    private String grammaticalPerson;

    /**
     * 动词原形/句子原文
     */
    @ApiModelProperty(value = "动词原形/句子原文")
    private String verb;

    /**
     * 动词变位/句子翻译
     */
    @ApiModelProperty(value = "动词变位/句子翻译")
    private String conjugation;

    /**
     * 例句
     */
    @ApiModelProperty(value = "例句")
    private String egSentence;

    /**
     * 重学时间
     */
    @ApiModelProperty(value = "重学时间")
    private LocalDate reviewTime;

    /**
     * 是否标熟
     */
    @ApiModelProperty(value = "是否标熟")
    private Integer ifDone;

    /**
     * 连续成功次数
     * 如果失败的话，hit_times需要重置
     */
    @ApiModelProperty(value = "连续成功次数")
    private Integer hitTimes;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime modifyTime;

    // ======== blows: not entity field =========

    @ApiModelProperty(value = "类别名称")
    private String foName;
}
