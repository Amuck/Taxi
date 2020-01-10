package com.bdstar.taxi.serial;

import android.util.Log;

import com.bdstar.taxi.events.MeterHeartbeatEvent;
import com.bdstar.taxi.events.MeterStatuEvent;
import com.bdstar.taxi.events.PassengerOffEvent;
import com.bdstar.taxi.events.PassengerOnEvent;
import com.bdstar.taxi.serial.bean.CarpoolPassengerOff;
import com.bdstar.taxi.serial.bean.CarpoolPassengerOn;
import com.bdstar.taxi.serial.bean.ValuationInfo;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * 计价器协议
 *
 * 起始位 包长度 设备类型 厂商标识 命令字   数据区  校验码 结束位
 * 2bytes 2bytes 1byte     1byte   2bytes  n bytes 1byte  2bytes
 */
public class MeterProtocol extends SerialProtocol {
    //INSTANCE;

    private static final String TAG = "MeterProtocol";

    @Override
    public int getBaudrate() {
        return 9600;
    }

    @Override
    public String getPath() {
        return "/dev/ttyUSB0";
    }

    @Override
    public int getParity() {
        return 0;
    }

    @Override
    public int getDataBits() {
        return 8;
    }

    @Override
    public int getStopBits() {
        return 1;
    }

    @Override
    public void dataRead(InputStream inputStream){
        try {
            byte a = (byte) inputStream.read();

            if (a != PACKAGE_HEAD[0]) return;

            byte b = (byte) inputStream.read();

            if (b != PACKAGE_HEAD[1]) return;

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            bos.write(a);
            bos.write(b);

            while (true){
                byte c = (byte) inputStream.read();

                bos.write(c);

                if (c == PACKAGE_HEAD[0]){
                    byte d = (byte) inputStream.read();

                    bos.write(d);

                    if (d == PACKAGE_HEAD[1]) {
                        if (bos.size() > 4)
                            break;
                        else {
                            bos.reset();
                            bos.write(a);
                            bos.write(b);
                        }
                    }
                }
            }



            byte[] result = bos.toByteArray();

            bos.close();
            onDataRead(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Command{
        /**
         * 计价器状态查询指令
         */
        public static final byte[] STATUS = {0x00, 0x00};
        /**
         * 计价器校时指令
         */
        public static final byte[] CLOCK = {0x00, 0x01};
        /**
         * 运价参数查询指令
         */
        public static final byte[] PRICE_QUERY = {0x00, 0x04};
        /**
         * 运价参数设置指令
         */
        public static final byte[] PRICE_SET = {0x00, 0x05};
        /**
         * 营运数据查询指令
         */
        public static final byte[] DATA_QUERY = {0x00, 0x06};
        /**
         * 计价器开机指令
         */
        public static final byte[] TURN_ON = {0x00, (byte) 0xE0};
        /**
         * 计价器关机指令
         */
        public static final byte[] TURN_OFF = {0x00, (byte) 0xE3};
        /**
         * 单次营运开始通知
         */
        public static final byte[] START = ByteUtil.hex2byte("00E7");//{0x00, 0xE7};
        /**
         * 单次营运结束通知
         */
        public static final byte[] FINISH = ByteUtil.hex2byte("00E8");//{0x00, 0xE8};
        /**
         * 计价器心跳指令
         */
        public static final byte[] HEARTBEAT = {0x00, (byte) 0xE9};
        /**
         * 当班营运数据补传
         */
        public static final int[] DUTY_SERVICE_DATA_EXTRA = {0x00, 0xF1};
        /**
         * 单次营运数据补传
         */
        public static final int[] ONCE_SERVICE_DATA_EXTRA = {0x00, 0xF2};
        /**
         * 计价器固件升级指令
         */
        public static final byte[] METER_UPDATE = {0x00, (byte) 0xFF};
        /**
         * 合乘进重车
         */
        public static final byte[] CARPOOL_ON = ByteUtil.hex2byte("1FE7");//{0x1F, 0xE7};
        /**
         * 合乘出重车
         */
        public static final byte[] CARPOOL_OFF = ByteUtil.hex2byte("1FE2");//{0x1F, 0xE2};

        public static final byte[] PRINT = ByteUtil.hex2byte("1F81");

        /**
         * 心跳
         */
        public static final byte[] CARPOOL_HEARTBEAT = ByteUtil.hex2byte("1FE9");
    }

    private static final byte[] PACKAGE_HEAD = ByteUtil.hex2byte("55AA");//{0x55, 0xAA};
    private static final byte DEVICE_TYPE = 0x00;
    private static final byte PRODUCT = 0x00;
    private static final byte RESULT_OK = ByteUtil.hex2byte("90")[0];
    private static final int RESULT_ERROR = 0xFF;
    /*private static final byte COMMAND_DEFAULT = 0x00;
    private static final byte COMMAND_EXTRA = 0x10;*/

    /**
     * 打包数据
     * @param command 命令
     * @param data  数据
     * @return  发往计价器得数据包
     */
    private void packData(byte[] command, byte[] data) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream(11 + data.length);
        byte[] result;

        stream.write(ByteUtil.intTo2Bytes(4 + data.length));
        stream.write(DEVICE_TYPE);
        stream.write(PRODUCT);
        stream.write(command);
        stream.write(data);

        byte[] jiaoyanqu = stream.toByteArray();
        byte jiaoyan = ByteUtil.getXor(jiaoyanqu);

        stream.reset();
        stream.write(PACKAGE_HEAD);
        stream.write(jiaoyanqu);
        stream.write(jiaoyan);
        stream.write(PACKAGE_HEAD);

        result = stream.toByteArray();
        stream.close();

        sendData(result);
        //EventBus.getDefault().post(new MeterSendMsgEvent(result));
    }

    /**
     * 接受计价器信息处理
     * @param data
     */
    public void onDataRead(byte[] data) {

        Log.d(TAG, "收到来自"+getClass().getSimpleName()+"的数据(" + data.length +"字节)" + ByteUtil.bytesToHexString(data));
        byte[] fullCommand = getCommand(data);

        if (fullCommand == null) return;

        byte command = fullCommand[1];

        if (fullCommand[0] == Command.START[0] &&
                command == Command.START[1]){
            onServiceStart(getData(data));
        }else if (fullCommand[0] == Command.CARPOOL_ON[0] &&
                command == Command.CARPOOL_ON[1]){
            onCarpoolPassengerOn(getData(data));
        }else if (fullCommand[0] == Command.FINISH[0] &&
                command == Command.FINISH[1]){
            onServiceFinish(getData(data));
        }else if (fullCommand[0] == Command.CARPOOL_OFF[0] &&
                command == Command.CARPOOL_OFF[1]){
            onCarpoolPassengerOff(getData(data));
        }else if (fullCommand[0] == Command.CARPOOL_HEARTBEAT[0] &&
                fullCommand[1] == Command.CARPOOL_HEARTBEAT[1]){
            onHeartbeat(getData(data));
        }else if (fullCommand[0] == Command.STATUS[0] &&
                fullCommand[1] == Command.STATUS[1]){
            onMeterStatus(getData(data));
        }else if (fullCommand[0] == Command.PRINT[0] &&
                fullCommand[1] == Command.PRINT[1]){
            onPrintRequest(getData(data));
        }
    }

    /**
     * 获取命令
     * @param packageData
     * @return
     */
    private byte[] getCommand(byte[] packageData){
        if (packageData.length < 8) return null;
        return new byte[]{packageData[6], packageData[7]};
    }

    /*private boolean isDataAvailabel(byte[] data){
        int len = data.length;

        if ((len > 4) && (data[0] == )
    }*/

    /**
     * 获取数据
     * @param packageData
     * @return
     */
    private byte[] getData(byte[] packageData){
        byte[] des = new byte[packageData.length - 11];

        System.arraycopy(packageData, 8, des, 0, des.length);

        return des;
    }

    /**
     * 收到计价器心跳
     * @param data
     */
    private void onHeartbeat(byte[] data){
        try{
            List<ValuationInfo> infos = new ArrayList<>();
            int count = ByteUtil.unit8ToInt(data[8]);

            for (int i = 0; i < count; i ++){
                ValuationInfo valuationInfo = new ValuationInfo();
                int offset = 30 * i + 9;

                valuationInfo.setIndex(ByteUtil.unit8ToInt(data[offset]));
                valuationInfo.setUnivalent(ByteUtil.bcdToString(ByteUtil.subByteArray(data, offset + 15, 2), 2));
                valuationInfo.setMileage(ByteUtil.bcdToString(ByteUtil.subByteArray(data, offset + 17, 4), 2));
                valuationInfo.setPay(ByteUtil.bcdToString(ByteUtil.subByteArray(data, offset + 24, 4), 2));

                infos.add(valuationInfo);
            }

            EventBus.getDefault().post(new MeterHeartbeatEvent(infos));
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    private void onServiceStart(byte[] data){
        serviceStartResponse();

        EventBus.getDefault().post(new PassengerOnEvent(0));
        Log.d(TAG, "起步: "+ByteUtil.bytesToHexString(data));
    }

    private void serviceStartResponse(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try {

            stream.write(ByteUtil.intToUnit8(RESULT_OK));
            /*stream.write(ByteUtil.strToBCDBytes("0000"));
            stream.write(ByteUtil.strToBCDBytes("0000"));
            stream.write(ByteUtil.longToUnit32(0));*/

            packData(Command.START, stream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onServiceFinish(byte[] data){
        PassengerOffEvent event = new PassengerOffEvent();

        event.setType(PassengerOffEvent.OFFTYPE_METER);
        event.setIndex(-1);
        event.setPay(ByteUtil.bcdToString(ByteUtil.subByteArray(data, 58, 3), 1));

        EventBus.getDefault().post(event);
        serviceFinishResponse();
        Log.d(TAG, "最后乘客下车: "+ event.getPay() + "元");
    }

    private void serviceFinishResponse(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try {

            stream.write(ByteUtil.intToUnit8(RESULT_OK));

            packData(Command.FINISH, stream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 当第二个乘客上车时
     * @param data
     */
    private void onCarpoolPassengerOn(byte[] data){
        String hex = ByteUtil.bytesToHexString(data);

        Log.d(TAG, hex);

        PassengerOnEvent event = new PassengerOnEvent();

        event.setIndex(ByteUtil.unit8ToInt(data[0]) - 1);
        event.setId(ByteUtil.unit32ToLong(ByteUtil.subByteArray(data, 1, 4)));
        event.setTime(ByteUtil.bcdToString(ByteUtil.subByteArray(data, 5, 7), 0));
        event.setResult(ByteUtil.unit8ToInt(data[0]));

        EventBus.getDefault().post(event);
    }

    /**
     *APP下车
     * @param data
     */
    private void onCarpoolPassengerOff(byte[] data){
        PassengerOffEvent event = new PassengerOffEvent();

        event.setType(PassengerOffEvent.OFFTYPE_APP);
        event.setIndex(ByteUtil.unit8ToInt(data[0]) - 1);
        event.setPay(ByteUtil.bcdToString(ByteUtil.subByteArray(data, 31, 4), 2));
        event.setResult(ByteUtil.unit8ToInt(data[37]));

        EventBus.getDefault().post(event);

        Log.d(TAG, "拼车乘客下车: " + event.getPay() + "元");
    }

    /**
     *  表 3 计价器状态查询指令应答数据区定义
     * @param data
     */
    private void onMeterStatus(byte[] data){
        MeterStatuEvent event = new MeterStatuEvent();

        event.setCarNo(ByteUtil.asciiToStr(ByteUtil.subByteArray(data, 10, 6)));

        EventBus.getDefault().post(event);

        Log.d(TAG, "车牌号： " + event.getCarNo());
    }

    /**
     * 拼车乘客上车
     * @param carpoolPassengerOn
     */
    public void carpoolPassengerOn(CarpoolPassengerOn carpoolPassengerOn){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try {

            stream.write(ByteUtil.intToUnit8(carpoolPassengerOn.getIndex()));
            stream.write(ByteUtil.longToUnit32(carpoolPassengerOn.getId()));
            stream.write(ByteUtil.strToBCDBytes(carpoolPassengerOn.getParam1()));
            stream.write(ByteUtil.strToBCDBytes(carpoolPassengerOn.getParam2()));
            stream.write(ByteUtil.strToBCDBytes(carpoolPassengerOn.getParam3()));

            packData(Command.CARPOOL_ON, stream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拼车乘客下车
     * @param carpoolPassengerOff
     */
    public void carpoolPassengerOff(CarpoolPassengerOff carpoolPassengerOff){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try {

            stream.write(ByteUtil.intToUnit8(carpoolPassengerOff.getIndex()));
            stream.write(ByteUtil.longToUnit32(carpoolPassengerOff.getId()));
            stream.write(ByteUtil.intToUnit8(carpoolPassengerOff.getPayType()));

            packData(Command.CARPOOL_OFF, stream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void queryMeterStatus(){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault());

            stream.write(ByteUtil.strToBCDBytes(dateFormat.format(new Date())));

            packData(Command.STATUS, stream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 收到计价器得打印请求
     * @param data
     */
    private void onPrintRequest(byte[] data){
        if (data != null){
            /*int result = ByteUtil.unit8ToInt(data[0]);
            byte resultByte = data[0];*/
            //不能直接和16进制表示的数比较
            if (data[0] == RESULT_OK){

                if (printOffSet < 5)
                    printNext();
                else {
                    Callback callback = printCallbackRef.get();
                    if (callback != null){
                        callback.onCompleted(true);
                    }
                }
            }else {
                Callback callback = printCallbackRef.get();
                if (callback != null){
                    callback.onCompleted(false);
                }
            }
        }
    }

    /**
     * 分包偏移，二维码分4包 + 文字1包 = 最大值4
     */
    private int printOffSet;
    private byte[] qrcodeData;
    private String extraPrint;
    private WeakReference<Callback> printCallbackRef;
    private static final int PRINT_TYPE_IMG = 1;
    private static final int PRINT_TYPE_TEXT = 2;

    /**
     * 打印
     * @param qrcodeData 二维码数据128*128
     * @param extra 打印额外的文字信息
     */
    public void print(byte[] qrcodeData, String extra, Callback callback){
        this.qrcodeData = qrcodeData;
        this.extraPrint = extra;
        printCallbackRef = new WeakReference<>(callback);
        printOffSet = 0;

        printNext();
    }

    /**
     * 打印下一行，打印是一行行数据打印
     */
    private void printNext() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data = new byte[512];

        try {
            if (printOffSet < 4){
                bos.write(ByteUtil.intToUnit8(PRINT_TYPE_IMG));
                bos.write(ByteUtil.intToUnit16(printOffSet));
                System.arraycopy(qrcodeData, printOffSet * data.length, data, 0, data.length);
                bos.write(data);
            }else {
                bos.write(ByteUtil.intToUnit8(PRINT_TYPE_TEXT));
                bos.write(ByteUtil.intToUnit16(0));
                bos.write(extraPrint.getBytes("GB18030"));
            }

            packData(Command.PRINT, bos.toByteArray());
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        printOffSet ++;
    }

    public interface Callback{
        void onCompleted(boolean isSuccessful);
    }
}
