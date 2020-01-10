package com.bdstar.taxi.utils;

import android.text.TextUtils;
import android.util.Log;

import com.bdstar.taxi.serial.ByteUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * 二维码工具类
 */
public class QrcodeUtil {
    /**
     * 生成简单二维码
     *
     * @param content                字符串内容
     * @param width                  二维码宽度
     * @param height                 二维码高度
     * @return BitMap
     */
    public static byte[] createQRCodeBits(String content, int width,int height) {
        // 字符串内容判空
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        // 宽和高>=0
        if (width < 0 || height < 0) {
            return null;
        }
        try {
            /* 1.设置二维码相关配置 */
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            // 字符转码格式设置
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            // 容错率设置
            hints.put(EncodeHintType.ERROR_CORRECTION, "H");
            hints.put(EncodeHintType.MARGIN, "1");
            // 空白边距设置
            /*if (!TextUtils.isEmpty(margin)) {
                hints.put(EncodeHintType.MARGIN, margin);
            }*/
            /* 2.将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象 */
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            byte[] pixels = new byte[width * height / 8];
            for (int y = 0; y < height; y ++){
                BitArray row = bitMatrix.getRow(y, null);

                row.toBytes(0, pixels, y * row.getSizeInBytes(), row.getSizeInBytes());
            }

            return pixels;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
