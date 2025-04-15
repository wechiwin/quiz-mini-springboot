// package com.moggi.quizmini.framework.dao.query;
//
// import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
// import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
// import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
// import com.moggi.quizmini.framework.dao.CustomQueryWrapper;
// import com.moggi.quizmini.framework.pojo.QueryDTO;
// import org.apache.commons.lang3.StringUtils;
// import org.springframework.util.ReflectionUtils;
//
// import java.lang.reflect.Field;
// import java.lang.reflect.Modifier;
// import java.time.LocalDate;
// import java.time.LocalTime;
// import java.util.*;
//
// /***
//  * 根据QueryDto及其子类构造mybatis-plus的QueryWrapper工具类<br>
//  */
// public class WrapperBuilder<T> {
//
//     private static final String DOT = ".";
//
//     /**
//      * 总入口，根据QueryDto构造QueryWrapper<br>
//      * <br>
//      * 逻辑如下：<br>
//      * 1 将QueryDto中的dcl加入QueryWrapper<br>
//      * 2 如果extendFields为true，将QueryDto子类中的扩展属性加入QueryWrapper<br>
//      * 3 如果filterFields不为空，将QueryDto中filter的属性（在filterFields中）加入QueryWrapper<br>
//      *
//      * @param queryDto 查询参数，不能为空
//      * @return
//      */
//     public CustomQueryWrapper buildQueryWrapper(QueryDTO queryDto, boolean extendFields, boolean extendSort, String[] filterFields) {
//         CustomQueryWrapper result = new CustomQueryWrapper();
//
//         // // 对dcl构造条件
//         // DclObject dcl = queryDto.getDcl();
//         // for (QueryCondition condition : dcl.getConditions()) {
//         //     buildDclField(result, condition);
//         // }
//
//         // 对子类定义的条件进行构造
//         if (extendFields) {
//             buildExtendFields(result, queryDto);
//         }
//
//         // // 对filter定义的条件进行构造
//         // if (filterFields != null && filterFields.length > 0) {
//         //     buildFilterFields(result, queryDto, filterFields);
//         // }
//
//         // 构建查询
//         if (extendSort) {
//             buildExtendOrders(queryDto, new WrapperOrderProvider(result));
//         }
//
//         return result;
//     }
//     //
//     // /**
//     //  * 构造Filter的查询条件
//     //  *
//     //  * @param wrapper
//     //  * @param queryDto
//     //  * @param filterFields 不能为空
//     //  */
//     // public void buildFilterFields(QueryWrapper wrapper, QueryDTO queryDto, String[] filterFields) {
//     //     for (String field : filterFields) {
//     //         if (field == null || !queryDto.getFilter()
//     //                 .containsKey(field)) {
//     //             continue;
//     //         }
//     //
//     //         Object fieldValue = queryDto.getFilter()
//     //                 .get(field);
//     //
//     //         // 跳过空值，认为空值是没设置
//     //         if (fieldValue == null) {
//     //             continue;
//     //         }
//     //
//     //         // 如果是集合，默认用in；如果不是集合，用like
//     //         QueryOp op = (fieldValue instanceof Collection) ? QueryOp.in : QueryOp.like;
//     //         buildSingleQueryField(wrapper, field, fieldValue, op);
//     //
//     //         // TODO：如果filter字段超过一定数目，则抛出异常
//     //     }
//     // }
//
//     /**
//      * 根据扩展属性添加到wrapper中
//      *
//      * @param wrapper
//      * @param queryDto
//      * @return
//      */
//     public void buildExtendFields(QueryWrapper wrapper, QueryDTO queryDto) {
//
//         // 如果不是QueryDto的子类（是QueryDto.class）
//         if (queryDto.getClass()
//                 .equals(QueryDTO.class)) {
//             return;
//         }
//
//         // 遍历子类每个属性（不看父类属性）
//         Field[] extendFields = queryDto.getClass()
//                 .getDeclaredFields();
//         for (Field f : extendFields) {
//             // 跳过静态属性
//             if (Modifier.isStatic(f.getModifiers())) {
//                 continue;
//             }
//
//             // 获取属性值
//             ReflectionUtils.makeAccessible(f); // 这里有点神奇，暂时这样处理
//             Object fieldValue = ReflectionUtils.getField(f, queryDto);
//
//             // 跳过空值，认为空值是没设置
//             if (fieldValue == null) {
//                 continue;
//             }
//
//             // 如果值是String，但是为blank，即“”，也不处理
//             if ((fieldValue instanceof String) && (org.apache.commons.lang3.StringUtils.isBlank((String) fieldValue))) {
//                 continue;
//             }
//
//             // 如果是Collection，但是为空，也不处理
//             if (fieldValue instanceof Collection && ((Collection) fieldValue).isEmpty()) {
//                 continue;
//             }
//
//             // 获取注解声明
//             QueryType queryType = f.getAnnotation(QueryType.class);
//             // 查询类型
//             QueryOp op = QueryOp.eq;
//             String[] fields = null;
//             if (queryType != null) {
//                 //@QueryType(ignore=true) //声明忽略，则忽略
//                 if (queryType.ignore()) {
//                     continue;
//                 }
//
//                 op = queryType.value();
//                 fields = queryType.field();
//             }
//
//             // 如果注解没有声明字段，则以属性名为准
//             if (fields == null || fields.length == 0) {
//                 fields = new String[]{f.getName()};
//             }
//
//             // 如果是QueryRange且没有定义@QueryType，默认用between
//             if (fieldValue instanceof QueryRange && queryType == null) {
//                 op = QueryOp.between;
//             }
//
//             if (fieldValue instanceof Collection
//                     && (op == QueryOp.like || op == QueryOp.likeRight || op == QueryOp.likeLeft || op == QueryOp.eqOrLikeRight)
//                     && fields.length == 1) {
//                 QueryWrapper<T> qw = (QueryWrapper) wrapper;
//                 // 如果有多个值但是对应一个列，嵌套，内部使用or
//                 QueryOp finalOp = op;
//                 String[] finalFields = fields;
//                 qw.nested(i -> buildOrQueryValues(i, finalFields[0], (Collection) fieldValue, finalOp));
//             } else {
//                 // 构造查询条件，传入时fields一定大于0
//                 buildQueryField(wrapper, fields, fieldValue, op);
//             }
//
//
//         }
//     }
//
//     // /**
//     //  * 构建order
//     //  *
//     //  * @param queryDto
//     //  * @param provider
//     //  */
//     // public static void buildExtendOrders(QueryDTO queryDto, OrderProvider provider) {
//     //     // 构造扩展字段时，考虑@QuerySort
//     //     QuerySort qs = queryDto.getClass().getAnnotation(QuerySort.class);
//     //     if (qs == null) {
//     //         // 没有定义排序，不论是默认排序还是前端排序都没有
//     //         return;
//     //     }
//     //
//     //     // 不允许排序字段重复出现
//     //     Set<String> sorted = new HashSet<>();
//     //
//     //     // 这个方法不需要用converter，有可能没有设置转换类型
//     //     Sort sort = queryDto.getPage().getSort();
//     //     if (sort != null) {
//     //         List<Sort.Order> orders = sort.getOrders();
//     //         // 兼容性处理
//     //         orders = (orders == null) ? new ArrayList<>() : orders;
//     //         int size = orders.size();
//     //
//     //         // 不能超过前端排序最大字段
//     //         if (size > qs.maxCustomSort()) {
//     //             size = qs.maxCustomSort();
//     //         }
//     //
//     //         for (int i = 0; i < size; ++i) {
//     //             Sort.Order order = orders.get(i);
//     //             // 获得安全的排序sql
//     //             String sqlField = buildSortField(order.getProp(), qs);
//     //             if (sqlField != null && !sorted.contains(sqlField)) {
//     //                 // 如果允许前端排序，并且有前端排序参数
//     //                 if (Sort.Order.ORDER.ASC.toString()
//     //                         .equalsIgnoreCase(order.getOrder())) {
//     //                     provider.orderByAsc(sqlField);
//     //                 } else {
//     //                     provider.orderByDesc(sqlField);
//     //                 }
//     //                 sorted.add(sqlField); // 标记已排序字段，防止重复排序
//     //
//     //                 // 如果不支持多字段排序，找到一个就退出
//     //                 if (!provider.supportMultiField()) {
//     //                     return;
//     //                 }
//     //             }
//     //         }
//     //     }
//     //
//     //     // 如果已经进行了客户端排序，则不进行默认排序
//     //     if (!sorted.isEmpty()) {
//     //         return;
//     //     }
//     //
//     //     // 如果没有客户端排序，则使用默认排序
//     //     String[] defaultFields = qs.defaultField();
//     //     Sort.Order.ORDER[] orders = qs.defaultOrder();
//     //     if (defaultFields.length != orders.length) {
//     //         // 如果配置不对，也不生成
//     //         return;
//     //     }
//     //
//     //     for (int i = 0, size = defaultFields.length; i < size; ++i) {
//     //         String field = defaultFields[i];
//     //         Sort.Order.ORDER order = orders[i];
//     //         if (Sort.Order.ORDER.ASC == order) {
//     //             provider.orderByAsc(field);
//     //         } else {
//     //             provider.orderByDesc(field);
//     //         }
//     //
//     //         // 如果不支持多字段排序，找到一个就退出
//     //         if (!provider.supportMultiField()) {
//     //             return;
//     //         }
//     //     }
//     // }
//
//
//     public void buildQueryField(QueryWrapper<T> wrapper, String[] fields, Object fieldValue, QueryOp op) {
//         if (fields.length == 1) {
//             // 如果只有单个属性，不嵌套，直接调用单条查询语句
//             buildSingleQueryField(wrapper, fields[0], fieldValue, op);
//         } else {
//             // 如果有多个属性，嵌套，内部使用or
//             wrapper.nested(i -> buildOrQueryFields(i, fields, fieldValue, op));
//         }
//     }
//
//     /**
//      * 构造OR条件的查询语句(多个字段)<br/>
//      *
//      * @param wrapper
//      * @param fields
//      * @param value
//      * @param op
//      * @return
//      */
//     protected QueryWrapper<T> buildOrQueryFields(QueryWrapper<T> wrapper, String[] fields, Object value, QueryOp op) {
//         for (String field : fields) {
//             wrapper.or();
//             buildSingleQueryField(wrapper, field, value, op);
//         }
//         return wrapper;
//     }
//
//     /**
//      * 构造OR条件的查询语句(多个取值)<br/>
//      *
//      * @param wrapper
//      * @param field
//      * @param values
//      * @param op
//      * @return
//      */
//     protected QueryWrapper<T> buildOrQueryValues(QueryWrapper<T> wrapper, String field, Collection values, QueryOp op) {
//         // 优化：对于传入的List/Collection等参数，进行Set去重
//         Set set = removeDuplicate(values);
//         for (Object value : set) {
//             wrapper.or();
//             buildSingleQueryField(wrapper, field, value, op);
//         }
//         return wrapper;
//     }
//
//     protected UpdateWrapper<T> buildOrUpdateValues(UpdateWrapper<T> wrapper, String field, Collection values, QueryOp op) {
//         // 优化：对于传入的List/Collection等参数，进行Set去重
//         Set set = removeDuplicate(values);
//         for (Object value : set) {
//             wrapper.or();
//             buildSingleQueryField(wrapper, field, value, op);
//         }
//         return wrapper;
//     }
//
//     /**
//      * 构造单个查询条件，只适用于扩展属性<br>
//      * 不建议使用在filter中<br>
//      *
//      * @param wrapper
//      * @param field
//      * @param val
//      * @param op
//      */
//     public static AbstractWrapper buildSingleQueryField(AbstractWrapper wrapper, String field, Object val, QueryOp op) {
//
//         // 以sql表示（下划线）的列名
//         String fieldSql = buildSqlField(field);
//         switch (op) {
//             case eq:
//                 wrapper.eq(fieldSql, val);
//                 break;
//             case lt:
//                 wrapper.lt(fieldSql, val);
//                 break;
//             case le:
//                 wrapper.le(fieldSql, val);
//                 break;
//             case gt:
//                 wrapper.gt(fieldSql, val);
//                 break;
//             case ge:
//                 wrapper.ge(fieldSql, val);
//                 break;
//             case ne:
//                 wrapper.ne(fieldSql, val);
//                 break;
//             case like:
//                 wrapper.like(fieldSql, val);
//                 break;
//             case notLike:
//                 wrapper.notLike(fieldSql, val);
//                 break;
//             case likeLeft:
//                 wrapper.likeLeft(fieldSql, val);
//                 break;
//             case likeRight:
//                 wrapper.likeRight(fieldSql, val);
//                 break;
//             case in:
//                 wrapper.in(fieldSql, removeDuplicate((Collection) val));
//                 break;
//             case notIn:
//                 wrapper.notIn(fieldSql, removeDuplicate((Collection) val));
//                 break;
//             case between:
//                 if (val instanceof QueryRange) {
//                     // 如果目标是date类型数据，则不进行end值处理
//                     QueryRange range = (QueryRange) val;
//                     Object start = range.getStart();
//                     Object end = range.getEnd();
//                     if (start != null && end != null) {
//                         // 处理结束时间
//                         end = handleEndValue(end);
//                         wrapper.between(fieldSql, start, end);
//                     } else if (start == null && end != null) {
//                         end = handleEndValue(end);
//                         wrapper.le(fieldSql, end);
//                     } else if (start != null && end == null) {
//                         wrapper.ge(fieldSql, start);
//                     }
//                 }
//                 break;
//             case notBetween:
//                 // 未验证
//                 if (val instanceof QueryRange) {
//                     QueryRange range = (QueryRange) val;
//                     Object start = range.getStart();
//                     Object end = range.getEnd();
//                     if (start != null && end != null) {
//                         // 处理结束时间
//                         end = handleEndValue(end);
//                         wrapper.notBetween(fieldSql, start, end);
//                     } else if (start == null && end != null) {
//                         end = handleEndValue(end);
//                         wrapper.gt(fieldSql, end);
//                     } else if (start != null && end == null) {
//                         wrapper.lt(fieldSql, start);
//                     }
//                 }
//                 break;
//             case isNull:
//                 if (Boolean.TRUE.equals(val)) {
//                     wrapper.isNull(fieldSql);
//                 } else {
//                     wrapper.isNotNull(fieldSql);
//                 }
//                 break;
//             case isNotNull:
//                 if (Boolean.TRUE.equals(val)) {
//                     wrapper.isNotNull(fieldSql);
//                 } else {
//                     wrapper.isNull(fieldSql);
//                 }
//                 break;
//             case eqOrLikeRight:
//                 wrapper.nested(w -> {
//                     AbstractWrapper ww = (AbstractWrapper) w;
//                     ww.eq(fieldSql, val);
//                     ww.or();
//                     ww.likeRight(fieldSql, val + "/");
//                     return w;
//                 });
//                 break;
//             default:
//                 break;
//         }
//         return wrapper;
//     }
//
//
//     /**
//      * 处理Between右边的值
//      *
//      * @param endValue
//      * @return
//      */
//     public static Object handleEndValue(Object endValue) {
//         if (endValue == null) {
//             return endValue;
//         }
//
//         Object result = endValue;
//         // 如果是日期，自动变为当天最后的时间
//         if (endValue instanceof LocalDate) {
//             // 不用LocalDateTimeUtil中的getEndDay，因为在目标字段类型为date时，between x and y，如果y精度是6个9以上，则会获得次日的内容
//             LocalTime time = LocalTime.of(23, 59, 59, 9999);
//             result = ((LocalDate) endValue).atTime(time);
//         }
//
//         return result;
//     }
//
//     /**
//      * 转义like中的字符
//      *
//      * @param val
//      * @return
//      */
//     public static String escapeLikeString(String val) {
//         if (val == null) {
//             return null;
//         }
//
//         val = (val)
//                 .replaceAll("\\\\", "\\\\\\\\") // 先对\转义，否则无法搜索出带有\的内容
//                 .replaceAll("%", "\\\\%") // 对%转义
//                 .replaceAll("_", "\\\\_"); // 对_转义
//         return val;
//     }
//
//
//     /**
//      * 替换Wrappers.query
//      *
//      * @param <T>
//      * @return
//      */
//     public static <T> CustomQueryWrapper<T> getWrapper() {
//         return new CustomQueryWrapper<>();
//     }
//
//     // /**
//     //  * 构造一个字段的条件
//     //  *
//     //  * @param wrapper
//     //  * @param condition
//     //  */
//     // public void buildDclField(AbstractWrapper wrapper, QueryCondition condition) {
//     //
//     //     String field = condition.getField();
//     //     QueryOp op = condition.getOp();
//     //     Object value = condition.getValue();
//     //     ValueType valueType = condition.getValueType();
//     //     if (valueType == ValueType.Boolean || valueType == ValueType.Integer || valueType == ValueType.String) {
//     //         buildSingleQueryField(wrapper, field, value, op);
//     //         return;
//     //     } else if (valueType == ValueType.StringList || valueType == ValueType.IntegerList) {
//     //         if (op == QueryOp.in) {
//     //             // 如果是in [...]
//     //             buildSingleQueryField(wrapper, field, value, op);
//     //             return;
//     //         } else if (op == QueryOp.like || op == QueryOp.likeRight || op == QueryOp.likeLeft) {
//     //             // 如果是like [...]
//     //
//     //             if (wrapper instanceof QueryWrapper) {
//     //                 QueryWrapper<T> qw = (QueryWrapper) wrapper;
//     //
//     //                 // 如果有多个属性，嵌套，内部使用or
//     //                 qw.nested(i -> buildOrQueryValues(i, field, (List) value, op));
//     //             } else if (wrapper instanceof UpdateWrapper) {
//     //                 UpdateWrapper<T> qw = (UpdateWrapper) wrapper;
//     //
//     //                 // 如果有多个属性，嵌套，内部使用or
//     //                 qw.nested(i -> buildOrUpdateValues(i, field, (List) value, op));
//     //             } else {
//     //                 throw new RuntimeException("Unknown Wrapper Type");
//     //             }
//     //             return;
//     //         }
//     //     }
//     //
//     //     throw new RuntimeException("无法处理DCL条件");
//     // }
//
//     /**
//      * 将java对象驼峰式字段转为数据库的下划线字段
//      *
//      * @param field
//      * @return
//      */
//     public static String buildSqlField(String field) {
//         String result;
//         if (field.contains(DOT)) {
//             String[] arr = field.split("\\" + DOT);
//             result = arr[0] + DOT + buildSqlField(arr[1]);
//             return result;
//         }
//
//         // 如果不是驼峰式命名，不转换
//         if (!StringUtils.isCamel(field)) {
//             result = field;
//         } else {
//             // 驼峰转下划线(使用mybatis的工具类，让其有共同的效果)
//             result = StringUtils.camelToUnderline(field);
//         }
//
//         return result;
//     }
//
//     /**
//      * 【安全的】获得排序字段
//      *
//      * @param field
//      * @param query
//      * @return
//      */
//     public static String buildSortField(String field, QuerySort query) {
//
//         boolean safeField = false;
//         String targetSqlField = null;
//
//         // 检查是buildSqlField否类中的字段名
//         Class[] clazz = query.customClass();
//         for (Class c : clazz) {
//             try {
//                 if (c.getDeclaredField(field) != null) {
//                     safeField = true;
//                     break;
//                 }
//             } catch (NoSuchFieldException e) {
//             }
//         }
//
//         // 检查是否声明过可以作为排序的字段名
//         String[] fields = query.customField();
//         if (!safeField) {
//             // 如果只有一个字段，尝试用逗号split
//             if (fields.length == 1) {
//                 fields = fields[0].split(",");
//             }
//
//             // 判断传入字段是否在后端配置范围
//             for (String f : fields) {
//                 String customField = f;
//                 String customSqlField = null;
//                 if (f.contains("|")) {
//                     String[] arr = f.split("\\|");
//                     customField = arr[0];
//                     customSqlField = arr[1];
//                 }
//
//                 // 如果用户传入的field是有配置过的
//                 if (customField.equals(field)) {
//                     safeField = true;
//                     targetSqlField = customSqlField;
//                     break;
//                 }
//             }
//         }
//
//         // 如果未声明，则返回null
//         if (!safeField) {
//             return null;
//         }
//
//         // 尝试将字段名转为sql字段形式
//         if (targetSqlField != null) {
//             // 如果已经配置了sql形式，用配置的
//             return targetSqlField;
//         } else {
//             // 默认按照驼峰转下划线形式
//             String sqlField = buildSqlField(field);
//             return sqlField;
//         }
//     }
//
//     /**
//      * 对Collection类型对象去重
//      *
//      * @param list
//      * @return
//      */
//     public static Set removeDuplicate(Collection list) {
//         if (list == null) {
//             return new HashSet();
//         }
//
//         Set set = new HashSet(list.size());
//         set.addAll(list);
//         return set;
//     }
// }