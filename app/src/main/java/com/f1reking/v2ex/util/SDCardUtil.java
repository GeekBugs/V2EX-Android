package com.f1reking.v2ex.util;

import android.app.ActivityManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * SD卡相关的辅助类
 * Created by F1ReKing on 2016/1/2.
 */
public class SDCardUtil {

    private SDCardUtil() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * Check the SD card
     *
     * @return 是否存在SDCard
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * Check if the file is exists
     *
     * @param filePath 文件路径
     * @param fileName 文件名
     * @return 是否存在文件
     */
    public static boolean isFileExistsInSDCard(String filePath, String fileName) {
        boolean flag = false;
        if (isSDCardEnable()) {
            File file = new File(filePath, fileName);
            if (file.exists()) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获取空闲内存容量
     *
     * @param context
     * @return
     */
    public static int getCacheMaxSize(Context context) {
        ActivityManager activeManager = ((ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE));
        int memClass = activeManager.getMemoryClass();
        return 1024 * 1024 * memClass / 4; // 硬引用缓存容量，为系统可用内存的1/4
    }

    /**
     * Write file to SD card
     *
     * @param filePath 文件路径
     * @param filename 文件名
     * @param content  内容
     * @return 是否保存成功
     * @throws Exception
     */
    public static boolean saveFileToSDCard(String filePath, String filename,
                                           String content) throws Exception {
        boolean flag = false;
        if (isSDCardEnable()) {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(filePath, filename);
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(content.getBytes());
            outStream.close();
            flag = true;
        }
        return flag;
    }

    /**
     * Read file as stream from SD card
     *
     * @param fileName String PATH =
     *                 Environment.getExternalStorageDirectory().getAbsolutePath() +
     *                 "/dirName";
     * @return Byte数组
     */
    public static byte[] readFileFromSDCard(String filePath, String fileName) {
        byte[] buffer = null;
        try {
            if (isSDCardEnable()) {
                String filePaht = filePath + "/" + fileName;
                FileInputStream fin = new FileInputStream(filePaht);
                int length = fin.available();
                buffer = new byte[length];
                fin.read(buffer);
                fin.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * Delete file
     *
     * @param filePath 文件路径
     * @param fileName filePath =
     *                 Environment.getExternalStorageDirectory().getPath()
     * @return 是否删除成功
     */
    public static boolean deleteSDFile(String filePath, String fileName) {
        File file = new File(filePath + "/" + fileName);
        if (!file.exists() || file.isDirectory())
            return false;
        return file.delete();
    }

    /**
     * 得到路径
     *
     * @param context
     * @return
     */
    public static String getRootPath(Context context) {
        if (context == null) {
            return null;
        } else {
            try {
                File e = context.getExternalFilesDir((String) null);
                if (e == null) {
                    e = context.getFilesDir();
                }

                return e == null ? null : e.getAbsolutePath();
            } catch (Exception var2) {
                var2.printStackTrace();
                return null;
            }
        }
    }

    /**
     * 文件主目录
     *
     * @return
     */
    public static String getMainPath() throws Exception {
        try {
            String path = getSDPath() + "/FingerFinance/";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            return path;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取错误日志文件夹
     *
     * @return
     * @throws Exception
     */
    public static String getErrorPath() throws Exception {
        try {
            String path = getMainPath() + "error/";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            return path;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 临时目录
     *
     * @return
     */
    public static String getTempPath() throws Exception {
        try {
            String path = getMainPath() + "temp/";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            return path;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取临时照片文件路径
     *
     * @return
     * @throws Exception
     */
    public static String getTempImageFilePath() throws Exception {
        try {
            return SDCardUtil.getTempPath() + "temp.jpeg";
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 文件上传目录
     *
     * @return
     * @throws Exception
     */
    public static String getUploadPath() throws Exception {
        try {
            String path = getMainPath() + "upload/";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            return path;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 删除上传文件中的所有文件
     */
    public static void deleteUploadFiles() {
        try {
            File uploadDir = new File(getUploadPath());
            if (uploadDir.exists() && uploadDir.isDirectory()) {
                File[] files = uploadDir.listFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].exists() && files[i].isFile()) {
                            files[i].delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param filePath
     * @return 删除文件
     */
    public static boolean deleteFile(String filePath) {
        if (filePath == null) {
            return false;
        }
        return deleteFile(new File(filePath));
    }

    /**
     * @param file
     * @return 删除文件
     */
    public static boolean deleteFile(File file) {
        if (file != null && file.exists() && file.isFile()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 头像文件目录
     *
     * @return
     */
    public static String getPersonIconPath() throws Exception {
        try {
            String path = getMainPath() + "heads/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return path;
        } catch (Exception e) {
            throw e;
        }
    }

    // 取SD卡路径
    public static String getSDPath() throws Exception {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory(); // 获取根目录
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            throw new Exception("SDcard不存在");
        }
    }

    // 判断所传入的文件是否大于SDcard剩余的容量
    public static boolean isSDSizeEnough(long fileSize) throws Exception {
        try {
            if ("".equals(getSDPath())) {
                return false;
            }
        } catch (Exception e) {
            throw e;
        }
        File path = Environment.getExternalStorageDirectory();
        StatFs statfs = new StatFs(path.getPath());
        long blockSize = statfs.getBlockSize();
        long availableBlocks = statfs.getAvailableBlocks();
        if (availableBlocks * blockSize > fileSize)
            return true;
        else
            return false;
    }


    /**
     * 得到文件路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

}

