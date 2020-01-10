package com.bdstar.taxi.net.bean;

/**
 * {
 *         "code": 100,
 *         "message": "operation successful",
 *         "object": 1
 *     }
 */
public class DefaultData {
    private int code;
    private String message;
    private int object;

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

    public int getObject() {
        return object;
    }

    public void setObject(int object) {
        this.object = object;
    }
}
