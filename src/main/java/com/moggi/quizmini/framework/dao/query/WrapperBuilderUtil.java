package com.moggi.quizmini.framework.dao.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.moggi.quizmini.framework.pojo.QueryDTO;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class WrapperBuilderUtil {

    /**
     * 构建 QueryWrapper
     */
    public static <T> QueryWrapper<T> buildQueryWrapper(Class<T> entityClass, QueryDTO queryDTO) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        if (queryDTO == null) {
            return wrapper;
        }

        Field[] fields = queryDTO.getClass().getDeclaredFields();
        for (Field field : fields) {
            QueryType queryType = field.getAnnotation(QueryType.class);
            if (queryType == null || queryType.ignore()) continue;

            field.setAccessible(true);
            Object fieldValue = getFieldValue(field, queryDTO);
            if (fieldValue == null) continue;

            String[] targetFields = queryType.field().length == 0 ? new String[]{field.getName()} : queryType.field();
            QueryOp op = queryType.value();

            buildQueryField(wrapper, targetFields, fieldValue, op);
        }

        return wrapper;
    }

    private static Object getFieldValue(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("无法获取字段值: " + field.getName(), e);
        }
    }

    /**
     * 构建一个字段或多个字段的查询
     */
    private static <T> void buildQueryField(QueryWrapper<T> wrapper, String[] fields, Object value, QueryOp op) {
        if (fields.length == 1) {
            buildSingleQueryField(wrapper, fields[0], value, op);
        } else {
            wrapper.nested(i -> buildOrQueryFields(i, fields, value, op));
        }
    }

    /**
     * 多字段的 OR 查询，如 (name LIKE ? OR code LIKE ?)
     */
    private static <T> void buildOrQueryFields(QueryWrapper<T> wrapper, String[] fields, Object value, QueryOp op) {
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            if (i == 0) {
                applyCondition(wrapper, field, value, op);
            } else {
                applyCondition(wrapper.or(), field, value, op);
            }
        }
    }

    /**
     * 单字段的查询
     */
    private static <T> void buildSingleQueryField(QueryWrapper<T> wrapper, String field, Object value, QueryOp op) {
        applyCondition(wrapper, field, value, op);
    }

    /**
     * 实际根据操作符应用条件
     */
    private static <T> void applyCondition(QueryWrapper<T> wrapper, String field, Object value, QueryOp op) {
        String buildField = buildSqlField(field);
        switch (op) {
            case eq:
                wrapper.eq(buildField, value);
                break;
            case ne:
                wrapper.ne(buildField, value);
                break;
            case gt:
                wrapper.gt(buildField, value);
                break;
            case ge:
                wrapper.ge(buildField, value);
                break;
            case lt:
                wrapper.lt(buildField, value);
                break;
            case le:
                wrapper.le(buildField, value);
                break;
            case like:
                wrapper.like(buildField, value);
                break;
            case notLike:
                wrapper.notLike(buildField, value);
                break;
            case likeLeft:
                wrapper.likeLeft(buildField, value);
                break;
            case likeRight:
                wrapper.likeRight(buildField, value);
                break;
            case between:
                if (value instanceof List) {
                    List<?> list = (List<?>) value;
                    if (list.size() == 2) {
                        wrapper.between(buildField, list.get(0), list.get(1));
                    }
                }
                break;
            case notBetween:
                if (value instanceof List) {
                    List<?> list = (List<?>) value;
                    if (list.size() == 2) {
                        wrapper.notBetween(buildField, list.get(0), list.get(1));
                    }
                }
                break;
            case in:
                wrapper.in(buildField, toCollection(value));
                break;
            case notIn:
                wrapper.notIn(buildField, toCollection(value));
                break;
            case isNull:
                wrapper.isNull(buildField);
                break;
            case isNotNull:
                wrapper.isNotNull(buildField);
                break;
            case eqOrLikeRight:
                wrapper.nested(w -> w.eq(buildField, value).or().likeRight(buildField, value));
                break;
            default:
                throw new UnsupportedOperationException("不支持的查询操作: " + op);
        }
    }

    /**
     * 将java对象驼峰式字段转为数据库的下划线字段
     *
     * @param field
     * @return
     */
    private static String buildSqlField(String field) {
        String result;
        // if (field.contains(DOT)) {
        //     String[] arr = field.split("\\" + DOT);
        //     result = arr[0] + DOT + buildSqlField(arr[1]);
        //     return result;
        // }

        // 如果不是驼峰式命名，不转换
        if (!StringUtils.isCamel(field)) {
            result = field;
        } else {
            // 驼峰转下划线(使用mybatis的工具类，让其有共同的效果)
            result = StringUtils.camelToUnderline(field);
        }

        return result;
    }

    private static Collection<?> toCollection(Object value) {
        if (value instanceof Collection) {
            return (Collection<?>) value;
        } else if (value.getClass().isArray()) {
            return Arrays.asList((Object[]) value);
        } else {
            throw new IllegalArgumentException("IN 操作的字段值必须是 Collection 或数组");
        }
    }
}
