package com.bdstar.taxi.serial;

import com.bdstar.taxi.events.SerialDataWriteEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStream;

public abstract class SerialProtocol {

    public void sendData(byte[] data){
        EventBus.getDefault().post(new SerialDataWriteEvent(getClass(), data));
    }

    /*public static String getTag(){
        return className;
    }*/

    //public abstract String getTag();
    public abstract int getBaudrate();
    public abstract String getPath();
    public abstract int getParity();
    public abstract int getDataBits();
    public abstract int getStopBits();
    //public abstract boolean isEnd(byte[] data);
    public abstract void dataRead(InputStream inputStream);
}
