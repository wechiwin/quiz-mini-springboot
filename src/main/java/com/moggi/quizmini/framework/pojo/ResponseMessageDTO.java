package com.moggi.quizmini.framework.pojo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ResponseMessageDTO implements Serializable {

    // 状态码 200成功  400失败
    private int status;

    // 数据
    private Object data;

    // 消息提示
    private String message;

    // 时间戳
    private LocalDateTime timestamp;

}