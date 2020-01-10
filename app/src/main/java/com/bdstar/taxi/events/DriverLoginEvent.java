package com.bdstar.taxi.events;

import com.bdstar.taxi.net.bean.DriverFullInfo;
import com.bdstar.taxi.net.bean.DriverInfo;

/**
 * 司机刷卡信号
 */
public class DriverLoginEvent {
    private DriverFullInfo driverInfo;

    public DriverLoginEvent(){}

    public DriverLoginEvent(DriverFullInfo info){
        this.driverInfo = info;
    }

    public DriverFullInfo getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(DriverFullInfo driverInfo) {
        this.driverInfo = driverInfo;
    }
}
