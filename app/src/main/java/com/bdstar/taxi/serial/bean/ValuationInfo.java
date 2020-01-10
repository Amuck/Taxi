package com.bdstar.taxi.serial.bean;

/**
 * 计价信息
 */
public class ValuationInfo {
    private int index;
    private String univalent;
    private String mileage;
    private String pay;

    public ValuationInfo(){}

    public ValuationInfo(String univalent, String mileage, String pay){
        this.univalent = univalent;
        this.mileage = mileage;
        this.pay = pay;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getUnivalent() {
        return univalent;
    }

    public void setUnivalent(String univalent) {
        this.univalent = univalent;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}
