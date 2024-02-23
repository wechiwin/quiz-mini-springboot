package com.moggi.quizmini.constant;

import lombok.Getter;

/**
 * @Author weiqirui
 * @Date 2024/2/23
 */
@Getter
public enum ForgettingCurveEnum {


    ONE(1, 1),
    TWO(2, 2),
    THREE(3, 4),
    FOUR(4, 7),
    FIVE(5, 15),
    SIX(6, 30),
    SEVEN(7, 60),
    EIGHT(8, 90),

    ;

    /**
     * 连续成功次数
     */
    private int hitTimes;
    /**
     * 若干天后重背
     */
    private int days;

    ForgettingCurveEnum(int hitTimes, int days) {
        this.hitTimes = hitTimes;
        this.days = days;
    }

    public static int getDaysByHitTimes(int hitTimes) {
        ForgettingCurveEnum[] forgettingCurveEnums = values();
        for (ForgettingCurveEnum forgettingCurveEnum : forgettingCurveEnums) {
            if (forgettingCurveEnum.getHitTimes() == hitTimes) {
                return forgettingCurveEnum.getDays();
            }
        }
        return -1;
    }
}
