package com.bdstar.taxi.serial.bean;

public class CarpoolPassengerOff {
    private int index;
    private long id = 0;
    private int payType = 0xFE;

    public CarpoolPassengerOff(int index){
        this.index = index + 1;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }
}
