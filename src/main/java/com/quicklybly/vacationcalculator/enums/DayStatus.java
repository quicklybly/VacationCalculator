package com.quicklybly.vacationcalculator.enums;

public enum DayStatus {
    WORKING(0),
    DAY_OFF(1),
    ;
    final int code;

    DayStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
