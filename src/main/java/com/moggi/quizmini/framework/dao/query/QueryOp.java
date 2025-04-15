package com.moggi.quizmini.framework.dao.query;

public enum QueryOp {
    /**
     * 等于
     */
    eq,
    /**
     * 不等于
     */
    ne,
    /**
     * 大于
     */
    gt,
    /**
     * 大于等于
     */
    ge,
    /**
     * 小于
     */
    lt,
    /**
     * 小于等于
     */
    le,
    /**
     * 模糊匹配
     */
    like,
    /**
     * 模糊不匹配
     */
    notLike,
    /**
     * 左匹配
     */
    likeLeft,
    /**
     * 右匹配
     */
    likeRight,
    /**
     * 之间
     */
    between,
    /**
     * 不在之间
     */
    notBetween,
    /**
     * 为空
     */
    isNull,
    /**
     * 不为空
     */
    isNotNull,
    /**
     * 在范围
     */
    in,
    /**
     * 不在范围
     */
    notIn,
    /**
     * 相等或前缀匹配
     */
    eqOrLikeRight
}