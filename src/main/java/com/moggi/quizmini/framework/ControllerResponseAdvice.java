package com.moggi.quizmini.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moggi.quizmini.framework.pojo.ResponseMessageDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.LocalDateTime;

/**
 * RestController 结果处理
 */
@Slf4j
@ControllerAdvice(basePackages = {"com.moggi.quizmini"})
public class ControllerResponseAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 判断是否要执行beforeBodyWrite方法,true为执行,false不执行.
     * 通过该方法可以选择哪些类或那些方法的response要进行处理, 其他的不进行处理
     *
     * @param methodParameter the return type
     * @param aClass          the selected converter type
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return !methodParameter.getParameterType().isAssignableFrom(ResponseMessageDTO.class);
    }

    /**
     * 对response执行具体的操作
     *
     * @param data               the body to be written
     * @param methodParameter    the return type of the controller method
     * @param mediaType          the content type selected through content negotiation
     * @param aClass             the converter type selected to write to the response
     * @param serverHttpRequest  the current request
     * @param serverHttpResponse the current response
     * @return
     */
    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object data, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // 如果方法返回的已经是 ResponseMessageDTO 或者子类 则不用处理 直接返回
        if (methodParameter.getGenericParameterType().equals(ResponseMessageDTO.class)) {
            return data;
        }

        ResponseMessageDTO result = new ResponseMessageDTO();
        result.setData(data);
        result.setStatus(200);
        result.setMessage("");
        result.setTimestamp(LocalDateTime.now());

        // 需要特殊处理的primitive type
        // String
        if (methodParameter.getGenericParameterType().equals(String.class)) {
            return objectMapper.writeValueAsString(result);
        }
        // Boolean
        if (methodParameter.getGenericParameterType().getTypeName().toLowerCase().equals("boolean")) {
            boolean flag = (Boolean) data;
            if (!flag) {
                result.setData(false);
            } else {
                result.setData(true);
            }
            return result;
        }

        return result;
    }

}