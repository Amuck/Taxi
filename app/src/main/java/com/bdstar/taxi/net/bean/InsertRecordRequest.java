package com.bdstar.taxi.net.bean;

/**
 * {
 * 	"id":"", （必要）
 * 	"driverId":"驾驶员id",（必要）
 * 	"driverName":"驾驶员名称",（非必要 ）
 * 	"licensePlateNumber":"车牌号",（非必要 ）
 * 	"onAdress":"上车地址间"
 * }
 */
public class InsertRecordRequest {
    private String id;
    private String driverId;
    private String onAdress;
    private String driverName;
    private String licensePlateNumber;
    private String devId;

    public InsertRecordRequest(String id, String driverName, String driverId, String onAdress, String devId){
        this.id = id;
        this.driverId = driverId;
        this.onAdress = onAdress;
        this.driverName = driverName;
        this.devId = devId;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getOnAdress() {
        return onAdress;
    }

    public void setOnAdress(String onAdress) {
        this.onAdress = onAdress;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
}
