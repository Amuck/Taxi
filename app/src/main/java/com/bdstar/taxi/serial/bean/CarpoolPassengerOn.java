package com.bdstar.taxi.serial.bean;

public class CarpoolPassengerOn {
    private int index;
    private long id;
    private String param1 = "0000";
    private String param2 = "0000";
    private String param3 = "0000";

    public CarpoolPassengerOn(int index, long id){
        this.index = index + 1;
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public long getId() {
        return id;
    }

    public String getParam1() {
        return param1;
    }

    public String getParam2() {
        return param2;
    }

    public String getParam3() {
        return param3;
    }
}
