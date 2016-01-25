package com.f1reking.v2ex.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by HuangYH on 2015/10/9.
 * 图片压缩工具类
 */
public class ImageCompressUtil {

    /**
     * 按指定的长边缩放，并根据照片中相机的方向信息调整图片方向
     *
     * @param srcPath      图片的完整路径
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap getBitmapFromPath(String srcPath, float targetWidth,
                                           float targetHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, opts);// 此时返回bm为空

        // 得到图片的宽度、高度；
        int imgWidth = opts.outWidth;
        int imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / targetHeight);
        if (widthRatio > 1 || heightRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        // 设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, opts);
        bitmap = rotateBitmapByExif(srcPath, bitmap);
        return bitmap;// 压缩好比例大小后再进行质量压缩
    }

    public static Bitmap rotateBitmapByExif(String srcPath, Bitmap bitmap) {
        ExifInterface exif;
        Bitmap newBitmap = null;
        try {
            exif = new ExifInterface(srcPath);
            if (exif != null) { // 读取图片中相机方向信息
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);
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
                    newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(), m, true);
                    if (newBitmap != bitmap) {
                        bitmap.recycle();
                    }
                    return newBitmap;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap rotateBitmap(Bitmap bmp, int degree) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Matrix m = new Matrix();
        m.postRotate(degree);
        Bitmap newBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, m,
                true);
        if (newBitmap != bmp) {
            bmp.recycle();
            return newBitmap;
        }
        return bmp;
    }

    /**
     * 按质量压缩图片，并把文件从临时文件夹转存到指定文件夹中
     *
     * @param fromPath 图片完整路径
     * @param toPath   要转存的路径
     * @return
     */
    public static File compressAndCopyImageFile(Context context,String fromPath, String toPath,
                                                int quality) {
        float width = DisplayUtils.getDisplayMetrics(context).x * 2 / 3;
        float heigth = DisplayUtils.getDisplayMetrics(context).y * 2 / 3;
        Bitmap bitmap = getBitmapFromPath(fromPath, width, heigth);
        // Bitmap bitmap = BitmapFactory.decodeFile(fromPath);
        // Bitmap bitmap = getCompressBitmapBySampleSize(fromPath, 2);
        File file = new File(toPath);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                out.flush();
            }
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
        recyleBitmap(bitmap);
        return file;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static File copyFile(String oldPath, String newPath) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newFile = new File(newPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldfile); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newFile);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                fs.close();
                inStream.close();
                return newFile;
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Bitmap图像转存到文件上
     *
     * @param bitmap
     * @param fullFileName 完整的文件名
     * @return
     */
    public static File convertBmpToFile(Bitmap bitmap, String fullFileName) {
        File file = new File(fullFileName);
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        return file;
    }

    /**
     * 通过压缩图片的像素来控制内存中Bitmap的大小
     *
     * @param imagePath
     * @param inSampleSize
     * @return
     */
    public static Bitmap getCompressBitmapBySampleSize(String imagePath,
                                                       int inSampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);// 此时返回bm为空
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        if (bitmap != null) {
            L.i("bitmap.getWidth():" + bitmap.getWidth()
                    + ",bitmap.getHeight():" + bitmap.getHeight());
        }
        return bitmap;
    }

    public static void recyleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }


}
