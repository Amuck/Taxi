package com.bdstar.taxi.events;

/**
 * 获取车牌号等信息
 */
public class MeterStatuEvent {
    private String carNo;

    public MeterStatuEvent(){}

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
}
