package com.f1reking.v2ex.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;

/**
 * 转换工具类
 * Created by F1ReKing on 2016/1/2.
 */
public class ConvertUtil {

    /**
     * dp 转换成 px
     *
     * @param dp
     * @param context
     * @return
     */
    public static int dp2px(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * px 转换成 dp
     *
     * @param px
     * @param context
     * @return
     */
    public static float px2dp(float px, Context context) {
        return px / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
    }

    /**
     * bitmap 转换成 ByteArray
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmap2ByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byteArray 转换成 Bitmap
     *
     * @param byteArray
     * @return
     */
    public static Bitmap byteArray2Bitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    /**
     * iamgeUri网络图片 转换成 路径
     *
     * @param context
     * @param contentUri
     * @return
     */
    public static String imageUri2Path(Context context, Uri contentUri) {
        Cursor c = context.getContentResolver().query(contentUri, null, null, null, null);
        c.moveToFirst();
        String mediaFilePath = c.getString(c.getColumnCount());
        c.close();
        return mediaFilePath;
    }

}
