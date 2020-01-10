package com.bdstar.taxi.events;

public class ErrorEvent {
    private int errorCode;
    private String msg;
    private Throwable throwable;

    public ErrorEvent(){

    }

    public ErrorEvent(String msg){
        this.msg = msg;
    }

    public ErrorEvent(Throwable throwable){
        this.throwable = throwable;
    }

    public ErrorEvent(int code, String msg){
        this.errorCode = code;
        this.msg = msg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
