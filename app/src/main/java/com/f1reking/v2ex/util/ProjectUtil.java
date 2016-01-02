package com.f1reking.v2ex.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by F1ReKing on 2016/1/2.
 */
public class ProjectUtil {

    private static Context context;

    public ProjectUtil(Context context) {
        this.context = context;
    }

    /**
     * 手机号验证
     *
     * @param mobileNum
     * @return 验证通过返回true
     */
    public static boolean isMobileNum(String mobileNum) {

        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(mobileNum);
        b = m.matches();
        return b;
    }

    /**
     * 对电话、手机号进行校验
     *
     * @param phoneNum
     * @return
     */
    public static boolean isPhoneNumberValid(String phoneNum) {
        boolean isValid = false;
        String expression = "((^[1][3,4,5,7,8][0-9]{9}$)|(^(010|02\\d|0[3-9]\\d{2})?\\d{6,8}$))";
        CharSequence inputStr = phoneNum;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 抖动动画
     *
     * @author HuangYH
     */
    public static Animation shakeAnimation(int CycleTimes) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 10);
        translateAnimation.setInterpolator(new CycleInterpolator(CycleTimes));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    public static File getPhotoDir(Context context) {
        String path = SDCardUtil.getRootPath(context);
        if (path == null) {
            ToastUtil.showToast(context, "无存储设备", 0);
            return null;
        }
        path = path + File.separator + "photo" + File.separator;
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        if (!dir.exists()) {
            ToastUtil.showToast(context, "无法创建文件", 0);
            return null;
        }
        return dir;
    }

    // 对一张图片进行宽高比例的压缩
    public static Bitmap getimage(String srcPath, int scaleWidth,
                                  int scaleHeigth, String waterMark) {
        try {
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 设置为true时，BitmapFactory.decodeFile(String pathName, Options
            // opts)并不会返回真正的bitmap给我们。
            // 而只会填充了这张图片的属性值返回给我们。
            newOpts.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 这里的bitmap其实是null
            int imageWidth = newOpts.outWidth;// 但我们可以取到属性值
            int imageHeight = newOpts.outHeight;// 因为填充了属性值
            // --------------------------
            int scaleFactor = Math.min(imageWidth / scaleWidth, imageHeight
                    / scaleHeigth);
            if (scaleFactor < 0)
                scaleFactor = 1;
            newOpts.inSampleSize = scaleFactor;// 设置缩放比例
            newOpts.inJustDecodeBounds = false;
            newOpts.inPurgeable = true;
            bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
            // 图片方向---------------------------
            ExifInterface exif = new ExifInterface(srcPath);
            if (exif != null) { // 读取图片中相机方向信息
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
                int digree = 0;
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        digree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        digree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        digree = 270;
                        break;
                }
                if (digree != 0) {
                    Matrix m = new Matrix();
                    m.postRotate(digree);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(), m, true);
                }
            }
            // 打水印---------------------
            if (!TextUtils.isEmpty(waterMark))
                return bitmap = mark(bitmap, waterMark);
            else
                return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 添加水印
    public static Bitmap mark(Bitmap src, String watermark) {
        try {
            SimpleDateFormat sdFormatter = new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss");
            int w = src.getWidth();
            int h = src.getHeight();
            Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
            Canvas canvas = new Canvas(result);
            canvas.drawBitmap(src, 0, 0, null);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setShadowLayer(1, 1, 1, Color.BLACK);
            paint.setAlpha(100);
            paint.setTextSize(20);
            paint.setAntiAlias(true);
            paint.setUnderlineText(false);
            canvas.drawText(sdFormatter.format(new Date()) + " " + watermark,
                    20, h - 60, paint);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * 方法名：compressImage
     * 功 能：图片压缩（质量压缩）,取得压缩后的路径
     * 参 数：@param image
     * 参 数：@return
	 * 返回值：Bitmap
	 */
    public static String getCompressImagePath(Context context, String srcPath,
                                              int scaleWidth, int scaleHeigth, String waterMark, int condense) {

        if (srcPath == null || srcPath.length() <= 0)
            return "";
        Bitmap image = getimage(srcPath, scaleWidth, scaleHeigth, waterMark);
        if (image == null)
            return "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        L.i("lzy", "压缩前质量大小======前====" + baos.toByteArray().length / 1024);
        while (baos.toByteArray().length / 1024 > condense) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            L.i("lzy",
                    "压缩" + options + "%后质量大小======后===="
                            + baos.toByteArray().length / 1024);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMddhhmmss");
        String fileName = "image" + sdFormatter.format(new Date()) + ".jpg";
        File file = new File(getPhotoDir(context), fileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = isBm.read(buffer)) != -1) {
                out.write(buffer, 0, length);
                out.flush();
            }
            baos.close();
            isBm.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out = null;
            }
        }
        if (!image.isRecycled()) {
            image.recycle();
        }
        return file.getAbsolutePath();

    }

    //把日期转为字符串
    public static String ConverToString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    //把字符串转为日期
    public static Date ConverToDate(String strDate) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.parse(strDate);
    }


}
