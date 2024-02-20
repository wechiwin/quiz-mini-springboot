package com.moggi.quizmini.framework.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.commons.lang3.StringUtils;

public class CustomQueryWrapper<T> extends QueryWrapper<T> {

    public CustomQueryWrapper() {
        super();
    }

    public CustomQueryWrapper(T entity) {
        super(entity);
    }

    // /**
    //  * 非对外公开的构造方法,只用于生产嵌套 sql
    //  *
    //  * @param entityClass 本不应该需要的
    //  */
    // private CustomQueryWrapper(T entity, Class<T> entityClass, AtomicInteger paramNameSeq,
    //                            Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments) {
    //     super.setEntity(entity);
    //     this.entityClass = entityClass;
    //     this.paramNameSeq = paramNameSeq;
    //     this.paramNameValuePairs = paramNameValuePairs;
    //     this.expression = mergeSegments;
    // }

    public CustomQueryWrapper(T entity, String... columns) {
        super(entity, columns);
    }

    /**
     * 重写生成customSqlSegment的方法，以AND开头
     */
    @Override
    public String getCustomSqlSegment() {
        String result = super.getCustomSqlSegment();
        if (StringUtils.isBlank(result)) {
            return result;
        }

        // 如果以WHERE开头，改为以AND开头
        if (result.startsWith(Constants.WHERE + " ")) {
            result = result.replaceFirst(Constants.WHERE + " ", Constants.AND + " ");
        }
        return result;
    }

    // 已经修改了mybatis-plus的QueryWrapper源代码，在QueryWrapper中进行转义处理
    // 此处暂时保留
    // @Override
    // protected QueryWrapper<T> likeValue(boolean condition, String column, Object val, SqlLike sqlLike) {
    //     if (val instanceof String) {
    //         val = WrapperBuilder.escapeLikeString((String) val);
    //     }
    //     return super.likeValue(condition, column, val, sqlLike);
    // }

    // /**
    //  * 用于生成嵌套 sql
    //  * <p>
    //  * 故 sqlSelect 不向下传递
    //  * </p>
    //  */
    // @Override
    // protected QueryWrapper<T> instance() {
    //     return new CustomQueryWrapper<>(entity, entityClass, paramNameSeq,
    //             paramNameValuePairs, new MergeSegments());
    // }

    /**
     * 是否有排序
     *
     * @return
     */
    public boolean hasOrderBy() {
        return !this.expression.getOrderBy().isEmpty();
    }

    /**
     * 获得OrderBy的Statement
     *
     * @return
     */
    public String getCustomOrderByStatement() {
        return this.expression.getOrderBy().getSqlSegment();
    }

}
