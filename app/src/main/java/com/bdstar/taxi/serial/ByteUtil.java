package com.bdstar.taxi.serial;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class ByteUtil {

        /**
         * 字符串转化成为16进制字符串
         *
         * @param s
         * @return
         */
        public static String strTo16(String s) {
            String str = "";
            for (int i = 0; i < s.length(); i++) {
                int ch = (int) s.charAt(i);
                String s4 = Integer.toHexString(ch);
                str = str + s4;
            }
            return str;
        }
        /**
         * 16进制转换成为string类型字符串
         *
         * @param s
         * @return
         */
        public static String hexStringToString(String s) {
            if (s == null || s.equals("")) {
                return null;
            }
            s = s.replace(" ", "");
            byte[] baKeyword = new byte[s.length() / 2];
            for (int i = 0; i < baKeyword.length; i++) {
                try {
                    baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                s = new String(baKeyword, "UTF-8");
                new String();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return s;
        }
        /**
         * 向串口发送数据转为字节数组
         */
        public static byte[] hex2byte(String hex) {
            String digital = "0123456789ABCDEF";
            String hex1 = hex.replace(" ", "").toUpperCase();
            char[] hex2char = hex1.toCharArray();
            byte[] bytes = new byte[hex1.length() / 2];
            byte temp;
            for (int p = 0; p < bytes.length; p++) {
                temp = (byte) (digital.indexOf(hex2char[2 * p]) * 16);
                temp += digital.indexOf(hex2char[2 * p + 1]);
                bytes[p] = (byte) (temp & 0xff);
            }
            return bytes;
        }
        /**
         * 接收到的字节数组转换16进制字符串
         */
        public static String bytes2HexString(byte[] b, int size) {
            String ret = "";
            for (int i = 0; i < size; i++) {
                String hex = Integer.toHexString(b[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                ret += hex.toUpperCase();
            }
            return ret;
        }
        public static String bytesToHexString(byte[] src) {
            StringBuilder stringBuilder = new StringBuilder("");
            if (src == null || src.length <= 0) {
                return null;
            }
            for (int i = 0; i < src.length; i++) {
                int v = src[i] & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(hv);
            }
            return stringBuilder.toString();
        }
        /**
         * 接收到的字节数组转换16进制字符串
         */
        public static String byteToStr(byte[] b, int size) {
            String ret = "";
            for (int i = 0; i < size; i++) {
                String hex = Integer.toHexString(b[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                ret += hex.toUpperCase();
            }
            return ret;
        }

    /**
     * ascii转字符串
     * @param data
     * @return
     */
    public static String asciiToStr(byte[] data){
            StringBuilder stringBuilder = new StringBuilder();

            for (byte ch: data)
                stringBuilder.append((char) ch);

            return stringBuilder.toString();
        }

        public static int byteToHexInt(byte value){
            //String hex = Integer.toHexString(value & 0xFF);
            /*if (hex.length() == 1) {
                hex = '0' + hex;
            }*/
            return value & 0xFF;
        }

        public static String intArrayToHexString(int[] ints){
            StringBuilder buffer = new StringBuilder();

            for (int value: ints){
                String hex = Integer.toHexString(value);

                if (hex.length() < 2)
                    buffer.append("0");
                buffer.append(hex);
            }
            return buffer.toString();
        }
        /**
         * 计算CRC16校验码
         * 逐个求和
         *
         * @param bytes 字节数组
         * @return {@link String} 校验码
         * @since 1.0
         */
        public static String getCRC_16(byte[] bytes) {
            int CRC = 0x0000ffff;
            int POLYNOMIAL = 0x0000a001;
            int i, j;
            for (i = 0; i < bytes.length; i++) {
                CRC ^= ((int) bytes[i] & 0x000000ff);
                for (j = 0; j < 8; j++) {
                    if ((CRC & 0x00000001) != 0) {
                        CRC >>= 1;
                        CRC ^= POLYNOMIAL;
                    } else {
                        CRC >>= 1;
                    }
                }
            }
            if (Integer.toHexString(CRC).toUpperCase().length() == 2) {
                return byteToStr(bytes, bytes.length) + "00" + Integer.toHexString(CRC).toUpperCase();
            } else if (Integer.toHexString(CRC).toUpperCase().length() == 3) {
                return byteToStr(bytes, bytes.length) + "0" + Integer.toHexString(CRC).toUpperCase();
            }
            return byteToStr(bytes, bytes.length) + Integer.toHexString(CRC).toUpperCase();
        }

        /**
         * 指令校验和,并取出后两位字节
         * */
        public static String getSum16(byte[] msg, int length) {
            long mSum = 0;
            byte[] mByte = new byte[length];

            /** 逐Byte添加位数和 */
            for (byte byteMsg : msg) {
                long mNum = ((long) byteMsg >= 0) ? (long) byteMsg : ((long) byteMsg + 256);
                mSum += mNum;
            } /** end of for (byte byteMsg : msg) */

            /** 位数和转化为Byte数组 */
            for (int liv_Count = 0; liv_Count < length; liv_Count++) {
                mByte[length - liv_Count - 1] = (byte) (mSum >> (liv_Count * 8) & 0xff);
            } /** end of for (int liv_Count = 0; liv_Count < length; liv_Count++) */
            return byteToStr(msg, length) + byteToStr(mByte, mByte.length).substring(byteToStr(mByte,       mByte.length).length() - 4, byteToStr(mByte, mByte.length).length());
        }

    /**
     * 异或运算和校验
     * @param datas
     * @return
     */
    public static byte getXor(byte[] datas){

        byte temp=datas[0];

        for (int i = 1; i <datas.length; i++) {
            temp ^=datas[i];
        }

        return temp;
    }


    /**
     * 将String转成BCD码
     *
     * @param s
     * @return
     */
    public static byte [] strToBCDBytes(String s)
    {

        if(s.length () % 2 != 0)
        {
            s = "0" + s;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream ();
        char [] cs = s.toCharArray ();
        for (int i = 0;i < cs.length;i += 2)
        {
            int high = cs [i] - 48;
            int low = cs [i + 1] - 48;
            baos.write (high << 4 | low);
        }
        return baos.toByteArray ();
    }
    /**
     * 将BCD码转成String
     *
     * @param b
     * @return
     */
    public static String bcdToString(byte [] b, int decimalSize){
        StringBuilder sb = new StringBuilder();
        boolean isHeadFound = false;

        for (int i = 0;i < b.length;i++ )
        {
            int h = ((b [i] & 0xff) >> 4) + 48;

            //if (isHeadFound)
                sb.append ((char) h);
            /*else if (h != 0){
                sb.append((char) h);
                isHeadFound = true;
            }*/
            int l = (b [i] & 0x0f) + 48;

            //if (isHeadFound)
                sb.append((char) l);
            /*else if (l != 0){
                sb.append ((char) l);
                isHeadFound = true;
            }*/
        }

        /*while (sb.length() < decimalSize + 1){
            sb.insert(0, "0");
        }*/

        if (decimalSize > 0)
            sb.insert(sb.length() - decimalSize, ".");

        return String.valueOf(Float.parseFloat(sb.toString())) ;
    }

    public static byte[] intTo2Bytes(int value){
        byte[] src = new byte[2];

        src[0] = (byte) ((value >> 8) & 0xFF);
        src[1] = (byte) (value & 0xFF);
        /*int temp = value;
        for (int i = 0; i < src.length; i++) {
            src[i] = Integer.valueOf(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8;// 向右移8位
        }*/
        return src;
    }

    /**
     * 转换UNIT8
     * @param value
     * @return
     */
    public static byte intToUnit8(int value){


        return (byte) (value & 0xFF);
    }

    public static int unit8ToInt(byte value){
        return value;
    }

    public static byte[] intToUnit16(int value){
        byte[] src = new byte[2];

        /*src[0] = (byte) ((value >> 8) & 0xFF);
        src[1] = (byte) (value & 0xFF);*/
        int temp = value;
        for (int i = 0; i < src.length; i++) {
            src[src.length - i - 1] = Integer.valueOf(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8;// 向右移8位
        }
        return src;
    }

    public static int Unit16ToInt(byte[] value){
        short s = 0;
        short s0 = (short) (value[0] & 0xff);
        short s1 = (short) (value[1] & 0xff);
        s0 <<= 8;
        s = (short) (s0 | s1);

        return s;
    }

    /**
     * 转换UNIT32
     * @param value
     * @return
     */
    public static byte[] longToUnit32(long value) {
        byte[] src = new byte[4];
        long temp = value;

        for (int i = 0; i < src.length; i++) {
            src[src.length - i - 1] = Long.valueOf(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8;// 向右移8位
        }
        /*src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);*/
        return src;
    }

    public static byte[] longToUnit32Lettle(long value){
        byte[] src = new byte[4];
        long temp = value;

        for (int i = 0; i < src.length; i++) {
            src[i] = Long.valueOf(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8;// 向右移8位
        }
        /*src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);*/
        return src;
    }

    public static long unit32ToLong(byte[] value){
        int s = 0;
        int s0 = value[0] & 0xff;
        int s1 = value[1] & 0xff;
        int s2 = value[2] & 0xff;
        int s3 = value[3] & 0xff;
        s0 <<= 24;
        s1 <<= 16;
        s2 <<= 8;
        s = s0 | s1 | s2 | s3;
        return s;
    }

    /**
     * 截取选择字节
     * @param src
     * @param start
     * @param len
     * @return
     */
    public static byte[] subByteArray(byte[] src, int start, int len){
        byte[] des = new byte[len];

        try {
            System.arraycopy(src, start, des, 0, len);
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }

        return des;
    }

    public static byte[] charsToBytes(char[] chars){
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }
}
