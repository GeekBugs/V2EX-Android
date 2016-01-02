package com.f1reking.v2ex.util;


import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 * 文件工具类
 * Created by F1ReKing on 2016/1/2.
 */
public class FileUtils {

    /**
     * 获取文件大小
     *
     * @param path
     * @return
     */
    public static long getFileSize(String path) {
        return getFileSize(new File(path));
    }

    /**
     * 获取文件大小
     *
     * @param file
     * @return
     */
    private static long getFileSize(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                long size = 0;
                for (File subFile : file.listFiles()) {
                    size += getFileSize(subFile);
                }
                return size;
            } else {
                return file.length();
            }
        } else {
            throw new IllegalArgumentException("File does not exist! | 文件不存在！");
        }
    }

    /**
     *  复制文件
     * @param originalFilePath
     * @param destFilePath
     * @throws IOException
     */
    public static void copyFile(String originalFilePath, String destFilePath) throws IOException {
        copyFile(new File(originalFilePath), destFilePath);
    }

    public static void copyFile(String originalFilePath, File destFile) throws IOException {
        copyFile(new File(originalFilePath), destFile);
    }

    public static void copyFile(File originalFile, String destFilePath) throws IOException {
        copyFile(originalFile, new File(destFilePath));
    }

    public static void copyFile(File originalFile, File destFile) throws IOException {
        copy(new FileInputStream(originalFile), new FileOutputStream(destFile));
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte buf[] = new byte[1024];
        int numRead = 0;

        while ((numRead = inputStream.read(buf)) != -1) {
            outputStream.write(buf, 0, numRead);
        }

        close(outputStream, inputStream);
    }

    /**
     *  删除文件
     * @param path
     */
    public static void deleteFile(String path) {
        deleteFile(new File(path));
    }

    private static void deleteFile(File file) {
        if (!file.exists()) {
            Log.e("","The file to be deleted does not exist! File's path is: " + file.getPath());
        } else {
            deleteFileRecursively(file);
        }
    }

    private static void deleteFileRecursively(File file) {
        if (file.isDirectory()) {
            for (String filename : file.list()) {
                File item = new File(file, filename);
                if (item.isDirectory()) {
                    deleteFileRecursively(item);
                } else {
                    if (!item.delete()) {
                        L.e("Failed in recursively deleting a file, file's path is: " + item.getPath());
                    }
                }
            }

            if (!file.delete()) {
                Log.e("","Failed in recursively deleting a directory, directories' path is: " + file.getPath());
            } else {
                if (!file.delete()) {
                    Log.e("","Failed in deleting this file, its path is: " + file.getPath());
                }
            }
        }
    }

    public static String readToString(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuffer buffer = new StringBuffer();
        String readLine = null;

        while ((readLine = reader.readLine()) != null) {
            buffer.append(readLine);
            buffer.append("\n");
        }

        reader.close();

        return buffer.toString();
    }

    public static byte[] readToByteArray(File file) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        copy(new FileInputStream(file), outputStream);

        return outputStream.toByteArray();
    }

    public static void writeByteArray(byte[] byteArray, String filePath) throws IOException {
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
        outputStream.write(byteArray);
        outputStream.close();
    }

    /**
     * 保存图片到文件
     *
     * @param fileForSaving
     * @param bitmap
     */
    public static void saveBitmapToFile(String fileForSaving, Bitmap bitmap) {
        File targetFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + fileForSaving + ".png");

        if (!targetFile.exists()) {
            try {
                targetFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(targetFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String toCatenatedPath(String... sequentialPathStrs) {
        String catenatedPath = "";
        for (int i = 0; i < sequentialPathStrs.length - 1; i++) {
            catenatedPath += sequentialPathStrs[i] + File.separator;
        }
        catenatedPath += sequentialPathStrs[sequentialPathStrs.length - 1];
        return catenatedPath;
    }

    /**
     *  关闭
     * @param closeables
     */
    public static void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
