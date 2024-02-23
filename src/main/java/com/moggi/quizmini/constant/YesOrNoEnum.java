package com.moggi.quizmini.constant;

import lombok.Getter;

/**
 * @Author weiqirui
 * @Date 2024/2/21
 */
@Getter
public enum YesOrNoEnum {

    Yes(1),

    No(0),

    ;
    private int val;

    YesOrNoEnum(int val) {
        this.val = val;
    }
}
