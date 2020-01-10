package com.bdstar.taxi.net.bean;

/**
 * 司机信息
 */
public class DriverFullInfo {
    private String driverid;
    private String vehiid;
    private String localhostIp;
    private DriverInfo jt808DriverInfoEntity;
    private DriverInfoExtra scDriverInfoReplenishEntity;
    private Company jt808CompanyInfoEntity;

    public String getLocalhostIp() {
        return localhostIp;
    }

    public String getVehiid() {
        return vehiid;
    }

    public void setVehiid(String vehiid) {
        this.vehiid = vehiid;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    public DriverInfo getJt808DriverInfoEntity() {
        return jt808DriverInfoEntity;
    }

    public void setJt808DriverInfoEntity(DriverInfo jt808DriverInfoEntity) {
        this.jt808DriverInfoEntity = jt808DriverInfoEntity;
    }

    public DriverInfoExtra getScDriverInfoReplenishEntity() {
        return scDriverInfoReplenishEntity;
    }

    public void setScDriverInfoReplenishEntity(DriverInfoExtra scDriverInfoReplenishEntity) {
        this.scDriverInfoReplenishEntity = scDriverInfoReplenishEntity;
    }

    public Company getJt808CompanyInfoEntity() {
        return jt808CompanyInfoEntity;
    }

    public void setJt808CompanyInfoEntity(Company jt808CompanyInfoEntity) {
        this.jt808CompanyInfoEntity = jt808CompanyInfoEntity;
    }
}
