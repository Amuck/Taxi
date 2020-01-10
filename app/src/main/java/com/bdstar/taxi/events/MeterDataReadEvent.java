package com.bdstar.taxi.events;

public class MeterDataReadEvent {
    private byte[] data;

    public MeterDataReadEvent(){}

    public MeterDataReadEvent(byte[] data){
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
