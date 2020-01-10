package com.bdstar.taxi.net.bean;

/**
 * "code": 100,
 *         "message": "operation successful",
 */
public class BaseData {
    private int code;
    private String message;

    public BaseData(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
