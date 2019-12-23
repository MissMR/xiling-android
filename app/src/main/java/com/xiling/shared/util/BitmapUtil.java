package com.xiling.shared.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xiling.MyApplication;
import com.xiling.R;
import com.xiling.shared.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.util
 * @since 2017-07-06
 */
public class BitmapUtil {

    public static Bitmap scaleBitmap(String imgUrl, int width, int height) {
        URL url;
        Bitmap scaledBitmap = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(imgUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap;
            if (input == null) {
                bitmap = BitmapFactory.decodeResource(MyApplication.getInstance().getResources(), R.mipmap.ic_launcher);
            } else {
                bitmap = BitmapFactory.decodeStream(input);
            }
            scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            bitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return scaledBitmap;
    }

    public static Bitmap scaleBitmapForShare(String filePath) {
        return scaleBitmap(filePath, Constants.SHARE_THUMB_SIZE, Constants.SHARE_THUMB_SIZE);
    }

    public static byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        bitmap.recycle();
        return stream.toByteArray();
    }

}
