package com.xiling.ddui.tools;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

public class DBase64 {

    /**
     * Base64编码
     *
     * @param text 需要编码的文本
     */
    public static String encode(String text) {
        if (!TextUtils.isEmpty(text)) {
            String encode = text;
            try {
                encode = new String(Base64.encode(text.getBytes(), Base64.DEFAULT));
                DLog.d("encode text(" + text + ") = " + encode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return encode;
        } else {
            return text;
        }
    }


    /**
     * Base64解码
     *
     * @param enText 需要解码的文本
     */
    public static String decode(String enText) {
        if (!TextUtils.isEmpty(enText)) {
            String result = new String(Base64.decode(enText.getBytes(), Base64.DEFAULT));
            DLog.d("decode enText(" + enText + ") = " + result);
            return result;
        }
        return enText;
    }

    /**
     * @param @param  bitmap
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: bitmapToBase64
     * @Description: (Bitmap 转换为字符串)
     */

    @SuppressLint("NewApi")
    public static String bitmapToBase64(Bitmap bitmap) {

        // 要返回的字符串
        String reslut = null;

        ByteArrayOutputStream baos = null;

        try {

            if (bitmap != null) {

                baos = new ByteArrayOutputStream();
                /**
                 * 压缩只对保存有效果bitmap还是原来的大小
                 */
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);

                baos.flush();
                baos.close();
                // 转换为字节数组
                byte[] byteArray = baos.toByteArray();

                // 转换为字符串
                reslut = Base64.encodeToString(byteArray, Base64.DEFAULT);
                //URL编码
                reslut = URLEncoder.encode(reslut);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return reslut;

    }

}
