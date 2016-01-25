package com.f1reking.v2ex.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast辅助工具类
 * Created by F1ReKing on 2016/1/2.
 */
public class ToastUtils {
    private static Toast toast = null;

    /**
     * 连续toast
     *
     * @param msg
     * @param duration 时长，0为SHORT,1为LONG
     */
    public static void showToast(Context context, String msg, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, duration);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static void showToastAtCenter(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 关闭toast
     */
    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
