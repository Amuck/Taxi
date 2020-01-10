package com.bdstar.taxi.serial;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LEDProtocol extends SerialProtocol{

    public static final String CMD_SEND_TEXT = "1f";

    private String frontText = " ";
    private String backText = "西昌欢迎您";

    @Override
    public int getBaudrate() {
        return 9600;
    }

    @Override
    public String getPath() {
        return "/dev/ttyUSB1";
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
    public void dataRead(InputStream inputStream) {
        /*try {

        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        try {
            int a =inputStream.read();

            Thread.sleep(200);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendFrontText(String txt){
        this.frontText = txt;

        setLedText();
    }

    public void sendBackText(String txt){
        this.backText = txt;
        setLedText();
    }

    private void setLedText(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            String showText = "0长128左移20:%红" + frontText + "\\0128左移20:%红" + backText;
            byte[] strData = showText.getBytes("unicodelittleunmarked");
            outputStream.write("DNSLED".getBytes());
            outputStream.write(0);
            outputStream.write(ByteUtil.hex2byte(CMD_SEND_TEXT));
            outputStream.write(ByteUtil.longToUnit32Lettle(strData.length));
            outputStream.write(ByteUtil.intToUnit16(0));
            outputStream.write(ByteUtil.intToUnit16(0));
            outputStream.write(strData);

            sendData(outputStream.toByteArray());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            outputStream.write("DNSLED".getBytes());
            outputStream.write(0);
            outputStream.write(ByteUtil.hex2byte("0502fdfd02"));
            outputStream.write(ByteUtil.hex2byte("ffffffff"));

            sendData(outputStream.toByteArray());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    sendFrontText("空车");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.run();
    }
}
