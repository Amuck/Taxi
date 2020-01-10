package com.bdstar.taxi.events;

import com.bdstar.taxi.serial.bean.ValuationInfo;

import java.util.List;

public class MeterHeartbeatEvent {
    private List<ValuationInfo> valuationInfos;

    public MeterHeartbeatEvent(){}

    public MeterHeartbeatEvent(List<ValuationInfo> infos){
        this.valuationInfos = infos;
    }

    public List<ValuationInfo> getValuationInfos() {
        return valuationInfos;
    }

    public void setValuationInfos(List<ValuationInfo> valuationInfos) {
        this.valuationInfos = valuationInfos;
    }
}
