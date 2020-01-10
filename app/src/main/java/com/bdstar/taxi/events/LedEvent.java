package com.bdstar.taxi.events;

public class LedEvent {
    private String msg;

    public LedEvent(){

    }

    public LedEvent(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
