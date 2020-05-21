package com.yilian.mylibrary;

import android.content.Context;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;


/**
 * Created by  on 2017/6/27 0027.
 */

public class ImageCompressUtil {
    /**
     * 图片压缩
     *不能递归，否则会造成文件名过长的问题
     * @param file      图片文件
     * @param maxWidth  宽
     * @param maxHeight 高
     * @param quality   质量
     * @param context
     * @return 压缩后的文件
     */
    public static File compressImage(File file, int maxWidth, int maxHeight, int quality, Context context) {
        try {
            long fileSize = FileUtils.getFileSize(file);
            Logger.i("before压缩前图片大小:"+fileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File compressToFile = null;
        try {
            compressToFile = new Compressor(context)
                    .setMaxWidth(maxWidth)
                    .setMaxHeight(maxHeight)
                    .setQuality(quality)
                    .compressToFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            long fileSize = FileUtils.getFileSize(compressToFile);
            Logger.i("after压缩后图片大小:"+fileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compressToFile;
    }
}
