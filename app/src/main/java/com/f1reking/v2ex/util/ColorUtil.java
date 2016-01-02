package com.f1reking.v2ex.util;

import android.content.Context;
import android.graphics.Color;

import java.util.regex.Pattern;

/**
 *Created by F1ReKing on 2016/1/2.
 * 颜色转换工具类
 */
public class ColorUtil {

    /**
     * 获取资源中的颜色
     * @param context
     * @param color
     * @return
     */
    public static int getResourcesColor(Context context, int color) {
        int reg = 0x00ffffff;
        reg = context.getResources().getColor(color);
        return reg;
    }

    /**
     * 将十六进制 颜色代码 转换为int
     * @param color
     * @return
     */
    public static int HextoColor(String color){
        String reg = "#[a-f0-9A-F]{8}";
        if (!Pattern.matches(reg,color)){
            color = "#00ffffff";
        }
        return Color.parseColor(color);
    }

    /**
     * 修改颜色透明度
     * @param color
     * @param alpha
     * @return
     */
    public static int changeAlpha(int color, int alpha){
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha,red,green,blue);
    }

}
