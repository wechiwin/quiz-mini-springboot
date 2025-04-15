// package com.moggi.quizmini.framework.dao.query;
//
// import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
// import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
// import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
// import com.baomidou.mybatisplus.core.toolkit.StringUtils;
// import lombok.SneakyThrows;
// import lombok.extern.slf4j.Slf4j;
// import org.ehcache.shadow.org.terracotta.context.query.Queries;
//
// import java.lang.reflect.Field;
// import java.util.*;
//
// @Slf4j
// public class QueryWrapperUtil {
//     private static final String DOT = ".";
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
//     @SneakyThrows
//     public static <T> QueryWrapper getWrapper2(T bean) {
//         QueryWrapper<T> wrapper = new QueryWrapper<>();
//         if (bean == null) {
//             return wrapper;
//         }
//         Field[] fields = bean.getClass().getDeclaredFields();
//         // List<Object[]> fields = getFields(bean.getClass());
//         for (Field field : fields) {
//             field.setAccessible(true);
//             field.
//         }
//         buildWrapper(wrapper);
//         return wrapper;
//     }
//
//     private static <T> void buildWrapper(QueryWrapper<T> wrapper) {
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
//
//     // https://blog.csdn.net/bluestarjava/article/details/128388964
//     public static <T> QueryWrapper getWrapper(T bean) {
//         QueryWrapper<T> queryWrapper = new QueryWrapper<>();
//         if (bean == null) {
//             return queryWrapper;
//         }
//         try {
//             List<Object[]> fields = getFields(bean.getClass());
//             for (Object[] os : fields) {
//                 Field field = (Field) os[0];
//                 QueryType queryType = (QueryType) os[1];
//                 Object val = getTargetValue(bean, field, queryType);
//                 if (val == null) {
//                     continue;
//                 }
//                 String filedColumn = StringUtils.isBlank(queryType.field()) ? StringUtils.camelToUnderline(field.getName()) : queryType.column();
//                 queryType.type().buildQuery(queryWrapper, filedColumn, val);
//             }
//         } catch (Exception e) {
//             log.error("设置查询条件失败{}", e);
//         }
//         return queryWrapper;
//     }
//
//     // https://blog.csdn.net/huangeight/article/details/132023930
//     public static <T> QueryWrapper<T> buildQueryWrapper(T t) {
//         QueryWrapper<T> wrapper = new QueryWrapper<T>();
//         if (t == null) {
//             return wrapper;
//         }
//         // hutool包装的方法,获得一个类中所有字段列表,包括其父类中的字段和私有字段
//         // 相当于getDeclaredFields()和getFields()的结合体
//         Field[] fields = ReflectUtil.getFields(t.getClass());
//         List<Field> list = Arrays.asList(fields);
//         if (list.isEmpty()) {
//             return wrapper;
//         }
//         for (Field field : list) {
//             // hutool包装的方法，与field.get(t)类似，但不需要try/catch
//             Object value = ReflectUtil.getFieldValue(t, field.getName());
//             // 字段值为空时不参与条件查询
//             if (value == null || "".equals(value)) {
//                 continue;
//             }
//             // 允许访问私有字段
//             field.setAccessible(true);
//             // 获取Wrapper注解
//             Wrapper wrapperAnno = field.getAnnotation(Wrapper.class);
//             if (wrapperAnno == null) {
//                 continue;
//             }
//             // 设置该字段对应数据库的列名,默认字段名=列名
//             String name = wrapperAnno.column().isEmpty()
//                     ? field.getName()
//                     : wrapperAnno.column();
//             // 根据注解的值添加对应的wrapper操作
//             switch (wrapperAnno.value()) {
//                 case EQ:
//                     wrapper.eq(name, value);
//                     break;
//                 case LT:
//                     wrapper.lt(name, value);
//                     break;
//                 case GT:
//                     wrapper.gt(name, value);
//                     break;
//             }
//         }
//         return wrapper;
//     }
//
//
//     /**
//      * 获取bean值
//      *
//      * @param vo
//      * @param field
//      * @return
//      * @throws Exception
//      */
//     public static Object getObjectValue(Object vo, Field field) throws Exception {
//         Object o = field.get(vo);
//         if (o == null) {
//             return null;
//         }
//         // 空字符串转换为null对象
//         if (field.getGenericType().toString().equals("class java.lang.String")) {
//             String val = (String) o;
//             if (StringUtils.isBlank(val)) {
//                 o = null;
//             }
//         }
//         return o;
//     }
//
//     /**
//      * 获取bean中的属性值
//      *
//      * @param vo    实体对象
//      * @param field 字段
//      * @param query 注解
//      * @return 最终的属性值
//      * @throws Exception
//      */
//     private static Object getTargetValue(Object vo, Field field, QueryType query) throws Exception {
//         Object o = getObjectValue(vo, field);
//         if (o == null) {
//             return o;
//         }
//         if (StringUtils.isNotEmpty(query.targetAttr())) {
//             String target = query.targetAttr();
//             if (target.contains(".")) {
//                 String[] targets = target.split("[.]");
//                 for (String name : targets) {
//                     o = getValue(o, name);
//                 }
//             } else {
//                 o = getValue(o, target);
//             }
//         }
//         return o;
//     }
//
//     /**
//      * 以类的属性的get方法方法形式获取值
//      *
//      * @param o
//      * @param name
//      * @return value
//      * @throws Exception
//      */
//     private static Object getValue(Object o, String name) throws Exception {
//         if (StringUtils.isNotNull(o) && StringUtils.isNotEmpty(name)) {
//             if (o instanceof Map) {
//                 Map<String, Object> objectMap = BeanUtils.beanToMap(o);
//                 if (objectMap.containsKey(name)) {
//                     return objectMap.get(name);
//                 }
//                 return null;
//             } else {
//                 Class<?> clazz = o.getClass();
//                 Field field = clazz.getDeclaredField(name);
//                 field.setAccessible(true);
//                 o = getObjectValue(o, field);
//             }
//         }
//         return o;
//     }
//
//     /**
//      * 获取字段注解信息
//      */
//     public static List<Object[]> getFields(Class clazz) {
//         List<Object[]> fields = new ArrayList<Object[]>();
//         List<Field> tempFields = new ArrayList<>();
//         tempFields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
//         tempFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
//         for (Field field : tempFields) {
//             // 单注解
//             if (field.isAnnotationPresent(QueryType.class)) {
//                 QueryType attr = field.getAnnotation(QueryType.class);
//                 if (attr != null) {
//                     field.setAccessible(true);
//                     fields.add(new Object[]{field, attr});
//                 }
//             }
//
//             // 多注解
//             if (field.isAnnotationPresent(Queries.class)) {
//                 Queries attrs = field.getAnnotation(Queries.class);
//                 QueryType[] queries = attrs.value();
//                 for (QueryType attr : queries) {
//                     if (attr != null) {
//                         field.setAccessible(true);
//                         fields.add(new Object[]{field, attr});
//                     }
//                 }
//             }
//         }
//         return fields;
//     }
//
// }