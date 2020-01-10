package com.bdstar.taxi.events;

public class MeterSendMsgEvent {
    private byte[] data;

    public MeterSendMsgEvent(){}

    public MeterSendMsgEvent(byte[] data){
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
