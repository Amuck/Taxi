package com.bdstar.taxi.events;

public class PassengerOffEvent {
    public static final int OFFTYPE_METER = 0;
    public static final int OFFTYPE_APP = 1;

    private int index;
    private String pay;
    private int result;
    private int type;

    public PassengerOffEvent(){}

    public PassengerOffEvent(int index, String pay){
        this.index = index;
        this.pay = pay;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
