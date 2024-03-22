package com.moggi.quizmini.framework.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * 项目全局配置类
 * 如果将Swagger新建一个Config类extends WebMvcConfigurationSupport
 * 那么jackson的配置不会生效
 * <p>
 * Jackson配置
 * https://www.cnblogs.com/wlandwl/p/jackson.html
 * https://www.cnblogs.com/fanshuyao/p/14707710.html
 * https://blog.csdn.net/Junna_zeng/article/details/123537881
 * https://blog.csdn.net/NestorBian/article/details/103227896
 * https://blog.csdn.net/qq_36268103/article/details/128670815
 * https://blog.csdn.net/weixin_43296313/article/details/122618360
 * https://blog.csdn.net/m0_50707445/article/details/120273345
 * https://blog.csdn.net/censc/article/details/111830338
 */
@Configuration
@EnableSwagger2
public class MyWebMvcConfigurationSupport extends WebMvcConfigurationSupport {

    /**
     * 跨域访问配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }

    /**
     * 全局MediaType  json格式
     * MediaType "application/json;charset=UTF-8";
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    // swagger
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.moggi.quizmini.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Quiz")
                .description("Quiz-app")
                .version("1.0")
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // swagger资源
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        // 静态文件资源 https://blog.csdn.net/zhuzicc/article/details/107635012
        // todo springboot 配置静态文件 https://blog.csdn.net/u010358168/article/details/81205116
        registry.addResourceHandler("/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/templates/");
        registry.addResourceHandler("/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/");
    }

    @Override
    protected void addFormatters(FormatterRegistry registry) {
        // 用于GetMapping 全局格式化日期转换
        registry.addFormatterForFieldType(LocalDate.class, new LocalDateFormatter());
        registry.addFormatterForFieldType(LocalDateTime.class, new LocalDateTimeFormatter());
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 代替框架默认的JackSon配置 用于post 全局格式化日期转换，long转字符串
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter =
                new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setObjectMapper(objectMapper());
        // 基于顺序，先执行自定义的
        converters.add(0, jackson2HttpMessageConverter);
    }

    /**
     * 配置ObjectMapper
     */
    private ObjectMapper objectMapper() {
        String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
        String dateFormat = "yyyy-MM-dd";
        String timeFormat = "HH:mm:ss";
        ObjectMapper objectMapper = new ObjectMapper();
        // 忽略空Bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 忽略在json字符串中存在，但是在对象中不存在对应属性的情况，防止错误。
        // 例如json数据中多出字段，而对象中没有此字段。如果设置true，抛出异常，因为字段不对应；false则忽略多出的字段，默认值为null，将其他字段反序列化成功
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // 时间序列化
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(timeFormat)));
        javaTimeModule.addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat(dateTimeFormat)));
        // 时间反序列化
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFormat)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(timeFormat)));
        javaTimeModule.addDeserializer(Date.class, new DateDeserializers.DateDeserializer() {
            @SneakyThrows
            @Override
            public Date deserialize(JsonParser jsonParser, DeserializationContext dc) {
                String text = jsonParser.getText().trim();
                SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
                return sdf.parse(text);
            }
        });
        javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance);
        javaTimeModule.addSerializer(BigInteger.class, ToStringSerializer.instance);

        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }

    // @Override
    // public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    //     argumentResolvers.add(new RequestSingleParamHandlerMethodArgumentResolver());
    //     // super.addArgumentResolvers(argumentResolvers);
    // }
}