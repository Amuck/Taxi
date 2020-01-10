package com.bdstar.taxi.serial;

import android.serialport.SerialPort;
import android.serialport.SerialPortFinder;
import android.util.Log;

import com.bdstar.taxi.events.SerialDataWriteEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum SerialController {
    INSTANCE;

    private static final String TAG = SerialController.class.getSimpleName();
    //private Map<String, SerialProtocol> protocolMap;
    //private Map<String, InputStream> inputStreamMap;
    private Map<Class<? extends SerialProtocol>, SerialPort> portMap;
    private Map<Class<? extends SerialProtocol>, OutputStream> outputStreamMap;
    private Map<Class<? extends SerialProtocol>, ReadThread> readThreadMap;

    SerialController(){
        //protocolMap = new HashMap<>();
        portMap = new HashMap<>();
        outputStreamMap = new HashMap<>();
        readThreadMap = new HashMap<>();
    }

    public void start(){
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        for (ReadThread readThread: readThreadMap.values()){
            readThread.start();
        }
    }

    public void stop(){
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);

        for (ReadThread readThread: readThreadMap.values()){
            readThread.interrupt();
        }

        try {
            for (OutputStream stream: outputStreamMap.values()){
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (SerialPort port: portMap.values()){
            port.close();
        }
    }

    public SerialProtocol getProtocol(Class<? extends SerialProtocol> type){

        ReadThread readThread = readThreadMap.get(type);

        if (readThread != null)
            return readThread.protocol;
        else
            return null;
    }

    /**
     * 连接设备
     * @param protocolClass
     */
    public void connect(Class<? extends SerialProtocol> protocolClass){
        try {
            SerialProtocol protocol = protocolClass.newInstance();
            SerialPort serialPort = SerialPort
                    .newBuilder(protocol.getPath(), protocol.getBaudrate())
                    .dataBits(protocol.getDataBits())
                    .parity(protocol.getParity())
                    .stopBits(protocol.getStopBits())
                    .build();

            ///protocolMap.put(protocol.getTag(), protocol);
            //inputStreamMap.put(protocol.getTag(), serialPort.getInputStream());
            portMap.put(protocolClass, serialPort);
            outputStreamMap.put(protocolClass, serialPort.getOutputStream());
            readThreadMap.put(protocolClass, new ReadThread(protocol, serialPort));
        } catch (IOException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onSerialDataWrite(SerialDataWriteEvent event){
        try {
            OutputStream outputStream = outputStreamMap.get(event.getType());

            if (outputStream != null)
                outputStream.write(event.getData());

            Log.d(TAG, "发送至"+event.getType().getSimpleName()+"的数据(" + event.getData().length +"字节)" + ByteUtil.bytesToHexString(event.getData()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findPaths(){
        SerialPortFinder finder = new SerialPortFinder();

        Log.d(TAG, "设备:" + Arrays.toString(finder.getAllDevices()));
        Log.d(TAG, "设备路径:" + Arrays.toString(finder.getAllDevicesPath()));
    }

    class ReadThread extends Thread{
        private SerialProtocol protocol;
        private SerialPort serialPort;
        //private boolean isReading = false;

        public ReadThread(SerialProtocol protocol, SerialPort serialPort){
            this.protocol = protocol;
            this.serialPort = serialPort;
        }

        public SerialProtocol getProtocol() {
            return protocol;
        }

        @Override
        public void run() {
            //ByteArrayOutputStream bos = new ByteArrayOutputStream();

            while (!isInterrupted()){
                //sleep(800);

                InputStream inputStream = serialPort.getInputStream();
                if (inputStream == null) return;

                    /*byte[] buffer = new byte[1024];
                    int size = 0;

                    while ((size = inputStream.read(buffer)) > 0){
                        bos.write(buffer, 0, size);

                        if (protocol.isEnd(bos.toByteArray()))
                            break;

                        buffer = new byte[1024];
                    }

                    byte[] data = bos.toByteArray();

                    bos.reset();
                    protocol.onDataRead(data);*/
                protocol.dataRead(inputStream);
            }

            try {
                //bos.close();
                serialPort.getInputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
