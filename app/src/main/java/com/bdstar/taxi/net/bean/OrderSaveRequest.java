package com.bdstar.taxi.net.bean;

/**
 * {
 * "driverId":"驾驶员id",
 * "driverName":"驾驶员名称",
 * "licensePlateNumber":"车牌号",
 * "onTime":"上车时间",
 * "offTime":"下车时间",
 * "onAdress":"上车地址间",
 * "offAdress":"下车地址",
 * "payManey":"支付金额",
 * "payType":"支付方式",
 * "payState":"支付状态"
 * }
 */
public class OrderSaveRequest {

    private String driverId;
    private String driverName;
    private String licensePlateNumber;
    private String onTime;
    private String offTime;
    private String onAdress;
    private String offAdress;
    private String payManey;
    private String payType;
    private String payState;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getOnTime() {
        return onTime;
    }

    public void setOnTime(String onTime) {
        this.onTime = onTime;
    }

    public String getOffTime() {
        return offTime;
    }

    public void setOffTime(String offTime) {
        this.offTime = offTime;
    }

    public String getOnAdress() {
        return onAdress;
    }

    public void setOnAdress(String onAdress) {
        this.onAdress = onAdress;
    }

    public String getOffAdress() {
        return offAdress;
    }

    public void setOffAdress(String offAdress) {
        this.offAdress = offAdress;
    }

    public String getPayManey() {
        return payManey;
    }

    public void setPayManey(String payManey) {
        this.payManey = payManey;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }
}
