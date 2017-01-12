package com.wangly.okhttputil.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wangly on 2017/1/11.
 */

public class FileUtils {

    public static final String sdcard = Environment.getExternalStorageDirectory() + File.separator;

    /**
     * 写文件
     *
     * @param inputStream
     */
    public static void write(InputStream inputStream, long fileSize) {
        File file = createSDFile("test.exe");
        FileOutputStream fos = null;
        int downloadSize = 0;
        try {
            fos = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                downloadSize += len;


//                int progress = (int) (downloadSize * 100 / fileSize);
//                System.out.println("下载文件的进度值：" + progress);
            }
            fos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 创建文件
     *
     * @param fileName
     * @return
     */
    public static File createSDFile(String fileName) {
        File file = new File(sdcard + fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
