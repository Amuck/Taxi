package com.bdstar.taxi.serial.bean;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class MeterStatus {
    private static final int DEVICE_STATUS_DEFAULT = 0x00;
    private static final int DEVICE_STATUS_LIMIT_TIMES = 0x01;
    private static final int DEVICE_STATUS_LIMIT_DATE = 0x02;

    private byte[] deviceId = new byte[5];
    private int deviceVersion;
    private int softMainVersion;
    private int softSecondVersion;
    private int status;
    private int workStatus;
    private byte[] carId = new byte[6];
    private byte[] businessLicense = new byte[16];
    private byte[] qualificationCertificate = new byte[19];
    private byte[] serviceCount = new byte[4];

    public MeterStatus(byte[] data){
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);

        try {
            inputStream.read(deviceId);
            deviceVersion = inputStream.read();
            softMainVersion = inputStream.read();
            softSecondVersion = inputStream.read();
            status = inputStream.read();
            workStatus = inputStream.read();
            inputStream.read(carId);
            inputStream.read(businessLicense);
            inputStream.read(qualificationCertificate);
            inputStream.read(serviceCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDeviceId() {
        return new String(deviceId);
    }

    public void setDeviceId(byte[] deviceId) {
        this.deviceId = deviceId;
    }

    public int getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(int deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public int getSoftMainVersion() {
        return softMainVersion;
    }

    public void setSoftMainVersion(int softMainVersion) {
        this.softMainVersion = softMainVersion;
    }

    public int getSoftSecondVersion() {
        return softSecondVersion;
    }

    public void setSoftSecondVersion(int softSecondVersion) {
        this.softSecondVersion = softSecondVersion;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(int workStatus) {
        this.workStatus = workStatus;
    }

    public String getCarId() {
        return new String(carId);
    }

    public void setCarId(byte[] carId) {
        this.carId = carId;
    }

    public String getBusinessLicense() {
        return new String(businessLicense);
    }

    public void setBusinessLicense(byte[] businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getQualificationCertificate() {
        return new String(qualificationCertificate);
    }

    public void setQualificationCertificate(byte[] qualificationCertificate) {
        this.qualificationCertificate = qualificationCertificate;
    }

    public byte[] getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(byte[] serviceCount) {
        this.serviceCount = serviceCount;
    }
}
