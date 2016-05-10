package com.grabpaisa.dashboard;

/**
 * Created by tahir on 5/5/2016.
 */
public class TelecomOperator {

    private String code;

    private String name;

    public TelecomOperator(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
