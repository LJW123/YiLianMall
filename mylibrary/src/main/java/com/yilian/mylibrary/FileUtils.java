package com.yilian.mylibrary;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.apache.http.protocol.HTTP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class FileUtils {
    /**
     * 获取指定文件大小
     *
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 图像保存到文件中
     *
     * @param bmp
     * @param filename
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static String saveBtpToFile(Context mContext, Bitmap bmp,
                                       String filename) throws IOException, FileNotFoundException {
        String path = null;
        if (filename != null && filename.contains("/")) {
            path = filename;
        } else {
            path = Constants.BASE_PATH + "/" + filename;
        }
        File basePath = new File(path.substring(0, path.lastIndexOf("/")));
        if (!basePath.exists()) {
            basePath.mkdirs();
        }
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream foutput = null;
        foutput = new FileOutputStream(path);
        if (filename.endsWith(".webp") || filename.endsWith(".WEBP")) {
            bmp.compress(Bitmap.CompressFormat.WEBP, 100, foutput);
        } else if (filename.endsWith(".png") || filename.endsWith(".PNG")) {
            bmp.compress(Bitmap.CompressFormat.PNG, 100, foutput);
        } else {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, foutput);
        }

        foutput.close();
        // bmp.recycle();
        return path;
    }

    public static boolean isExit(String filePath) {
        File apkFile = new File(filePath);
        return apkFile.exists();
    }

    /**
     * 图像保存到文件中
     *
     * @param bmp
     * @param filename
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static String saveDrawableToFile(Context mContext, Bitmap bmp,
                                            String filename) throws IOException, FileNotFoundException {
        String path = null;
        if (filename != null && filename.contains("/")) {
            path = filename;
            File basePath = new File(Constants.BASE_PATH);
            if (!basePath.exists()) {
                basePath.mkdirs();
            }
        } else {
            path = Constants.BASE_PATH + "/" + filename;
            File basePath = new File(Constants.BASE_PATH);
            if (!basePath.exists()) {
                basePath.mkdirs();
            }
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
        FileOutputStream foutput = null;
        foutput = new FileOutputStream(path);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, foutput);

        foutput.close();
        // bmp.recycle();
        return path;
    }


    /**
     * 追加文件：使用FileWriter
     *
     * @param fileName
     * @param content
     */
    public static void writeMsgSdcard(String content, String path,
                                      String fileName, boolean isAdpend) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(
                    path + File.separator + fileName, isAdpend);
            if ("log.txt".equals(fileName)) {
                writer.write("\n\n" + content);
            } else {
                writer.write(content);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 读取文件内容
     */
    public static String readMsgSdcard(String path, String fileName) {
        byte Buffer[] = new byte[1024];
        // 得到文件输入流
        File file = new File(path + File.separator + fileName);
        if (!file.exists()) {
            return "";
        }
        FileInputStream in = null;
        ByteArrayOutputStream outputStream = null;
        try {
            in = new FileInputStream(file);
            // 读出来的数据首先放入缓冲区，满了之后再写到字符输出流中
            int len = in.read(Buffer);
            // 创建一个字节数组输出流
            outputStream = new ByteArrayOutputStream();
            outputStream.write(Buffer, 0, len);
            // 把字节输出流转String
            return new String(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param path
     */
    public static void delFile(String path) {
        if (path == null) {
            return;
        }
        File file = new File(Environment.getExternalStorageDirectory() + path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 删除文件夹里的文件
     */
    public static void deleteDirectory(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteDirectory(f);
            }
            file.delete();
        }

    }

    /**
     * 压缩文件
     *
     * @param sourceFilePath 需要压缩的文件路径
     * @param sourceFileName 压缩的文件名称
     * @param toPath         压缩的文件生成路径
     */
    public static void zipFile(String sourceFilePath, String sourceFileName, String toPath) throws FileNotFoundException,
            IOException {
        ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(
                new FileOutputStream(new File(toPath)), 1024 * 1024));
        sourceFilePath = new String(sourceFilePath.getBytes("8859_1"), HTTP.UTF_8);
        BufferedInputStream in = null;
        try {
            byte buffer[] = new byte[1024 * 1024];
            in = new BufferedInputStream(new FileInputStream(new File(sourceFilePath)),
                    1024 * 1024);
            zipout.putNextEntry(new ZipEntry(sourceFileName));
            int realLength;
            while ((realLength = in.read(buffer)) != -1) {
                zipout.write(buffer, 0, realLength);
            }
            in.close();
            zipout.flush();
            zipout.closeEntry();
        } finally {
            if (in != null)
                in.close();
            if (zipout != null) ;
            zipout.close();
        }
    }

    /**
     * 获取文件大小
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static long getFileSize(String path) throws Exception {
        long s = 0;
        File f = new File(path);
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
        } else {
            f.createNewFile();
            System.out.println("文件不存在");
        }
        return s;
    }

    // 递归取得文件夹（包括子目录）中所有文件的大小
    public static long getFilesSize(File f) throws Exception//取得文件夹大小
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFilesSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    public static String FormetFileSize(long fileS) {//转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static long getlist(File f) {//递归求取目录文件个数
        long size = 0;
        File flist[] = f.listFiles();
        size = flist.length;
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getlist(flist[i]);
                size--;
            }
        }
        return size;
    }

    public static long getDir(File f) {//递归求取目录中文件（包括目录）的个数
        long size = 0;
        File flist[] = f.listFiles();
        size = flist.length;
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getDir(flist[i]);

            }
        }
        return size;
    }

    public static String getFileSizes(String path) throws Exception {
        //取得文件大小
        long l = 0;
        File ff = new File(Environment.getExternalStorageDirectory() + path);
        if (ff.isDirectory()) { //如果路径是文件夹的时候
            System.out.println("文件个数           " + getlist(ff));
            System.out.println("文件和目录总个数           " + getDir(ff));

            System.out.println("目录");
            l = getFilesSize(ff);
            return FormetFileSize(l);
        } else {
            System.out.println("     文件个数           1");
            System.out.println("文件");
            l = getFilesSize(ff);
            return FormetFileSize(l);
        }
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }


    public static String SDPATH = Environment.getExternalStorageDirectory()
            + "/Photo_LJ/";

    public static void saveBitmap(Bitmap bm, String picName) {
        try {
            if (!isFileExist("")) {
                File tempf = createSDDir("");
            }
            File f = new File(SDPATH, picName + ".JPEG");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File createSDDir(String dirName) throws IOException {
        File dir = new File(SDPATH + dirName);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            System.out.println("createSDDir:" + dir.getAbsolutePath());
            System.out.println("createSDDir:" + dir.mkdir());
        }
        return dir;
    }

    public static boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        file.isFile();
        return file.exists();
    }

    public static void deleFile(String fileName) {
        File file = new File(SDPATH + fileName);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }

    public static void deleteDir() {
        File dir = new File(SDPATH);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete();
            else if (file.isDirectory())
                deleteDir();
        }
        dir.delete();
    }

    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {

            return false;
        }
        return true;
    }


    private static String APP_DIR_NAME = "honjane";
    private static String FILE_DIR_NAME = "files";
    private static String mRootDir;
    private static String mAppRootDir;
    private static String mFileDir;

    public static void init() {
        mRootDir = getRootPath();
        if (mRootDir != null && !"".equals(mRootDir)) {
            mAppRootDir = mRootDir + "/" + APP_DIR_NAME;
            mFileDir = mAppRootDir + "/" + FILE_DIR_NAME;
            File appDir = new File(mAppRootDir);
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            File fileDir = new File(mAppRootDir + "/" + FILE_DIR_NAME);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

        } else {
            mRootDir = "";
            mAppRootDir = "";
            mFileDir = "";
        }
    }

    public static String getFileDir() {
        return mFileDir;
    }


    public static String getRootPath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return Environment.getExternalStorageDirectory().getAbsolutePath(); // filePath:  /sdcard/
        } else {
            return Environment.getDataDirectory().getAbsolutePath() + "/data"; // filePath:  /data/data/
        }
    }

    /**
     * 打开文件
     * 兼容7.0
     *
     * @param context     activity
     * @param file        File
     * @param contentType 文件类型如：文本（text/html）
     *                    当手机中没有一个app可以打开file时会抛ActivityNotFoundException
     */
    public static void startActionFile(Context context, File file, String contentType) throws ActivityNotFoundException {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//增加读写权限
        intent.setDataAndType(getUriForFile(context, file), contentType);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 打开相机
     * 兼容7.0
     *
     * @param activity    Activity
     * @param file        File
     * @param requestCode result requestCode
     */
    public static void startActionCapture(Activity activity, File file, int requestCode) {
        if (activity == null) {
            return;
        }
        Logger.i("打开7.0相机");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(activity, file));
        activity.startActivityForResult(intent, requestCode);
    }


    public static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(),
                    ProviderImpl.getInstance().getApplicationId()+".fileprovider", file);
            Logger.i("BuildConfig.APPLICATION_ID:"+ProviderImpl.getInstance().getApplicationId());
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * 打开apk安装界面
     *
     * @param filePath
     */
    public static void openApk(Context context, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uriForFile = FileProvider.getUriForFile(context, ProviderImpl.getInstance().getApplicationId()+".fileprovider", file);
            intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}
