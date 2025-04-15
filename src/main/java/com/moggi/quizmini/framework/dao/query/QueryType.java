package com.moggi.quizmini.framework.dao.query;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QueryType {

    /**
     * 查询类别
     *
     * @return
     */
    QueryOp value() default QueryOp.eq;

    /**
     * 查询字段，不填默认与属性名相同
     *
     * @return
     */
    String[] field() default {};

    /**
     * 忽略当前字段，不生成SQL
     *
     * @return
     */
    boolean ignore() default false;
}