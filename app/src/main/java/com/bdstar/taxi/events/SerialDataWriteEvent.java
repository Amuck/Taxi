package com.bdstar.taxi.events;

import com.bdstar.taxi.serial.SerialProtocol;

public class SerialDataWriteEvent {
    private Class<? extends SerialProtocol> type;
    private byte[] data;

    public SerialDataWriteEvent(){}

    public SerialDataWriteEvent(Class<? extends SerialProtocol> type, byte[] data){
        this.type = type;
        this.data = data;
    }

    public Class<? extends SerialProtocol> getType() {
        return type;
    }

    public void setType(Class<? extends SerialProtocol> type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
