package com.moggi.quizmini.framework.config;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    @Bean("objectMapper")
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper getObjectMapper(Jackson2ObjectMapperBuilder builder) {

        ObjectMapper mapper = builder.build();

        // 在反序列化时忽略在 json 中存在但 Java 对象不存在的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 在序列化时日期格式默认为 yyyy-MM-dd'T'HH:mm:ss.SSSZ ,比如如果一个类中有private Date date;这种日期属性，
        // 序列化后为：{"date" : 1413800730456}，若不为true，则为{"date" : "2014-10-20T10:26:06.604+0000"}
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 在JSON中允许C/C++ 样式的注释(非标准，默认禁用)
        mapper.configure(Feature.ALLOW_COMMENTS, true);
        // 允许没有引号的字段名(非标准)
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许单引号(非标准)
        mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        // 强制转义非ASCII字符
        mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        // 将内容包裹为一个JSON属性，属性名由@JsonRootName注解指定
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        // 序列化枚举是以toString()来输出，默认false，即默认以name()来输出
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        // 序列化Map时对key进行排序操作，默认false
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        // 序列化char[]时以json数组输出，默认false
        mapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, true);
        // 允许字段名没有引号（可以进一步减小json体积）：
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许出现特殊字符和转义符
        // mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);这个已经过时。
        mapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);

        // 序列化结果格式化，美化输出
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        // 反序列化时，空字符串对于的实例属性为null：
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        // 枚举输出成字符串 WRITE_ENUMS_USING_INDEX：输出索引
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        // 反序列化时，遇到未知属性不要抛出异常：
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 反序列化时，遇到忽略属性不要抛出异常：
        mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        // 允许序列化空的POJO类 (否则会抛出异常)
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 把java.util.Date, Calendar等序列化为时间格式的字符串(如果不执行以下设置，就会序列化成时间戳格式)：
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 日期格式
        mapper.setDateFormat(new SimpleDateFormat());

        // 设置时区 GMT+8
        // map.put("CTT", "Asia/Shanghai");
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        // Include.NON_NULL 属性为NULL 不序列化
        // ALWAYS // 默认策略，任何情况都执行序列化
        // NON_EMPTY // null、集合数组等没有内容、空字符串等，都不会被序列化
        // NON_DEFAULT // 如果字段是默认值，就不会被序列化
        // NON_ABSENT // null的不会序列化，但如果类型是AtomicReference，依然会被序列化
        mapper.setSerializationInclusion(Include.NON_NULL);

        // 忽略值为默认值的属性
        mapper.setDefaultPropertyInclusion(Include.NON_DEFAULT);

        return mapper;
    }

}