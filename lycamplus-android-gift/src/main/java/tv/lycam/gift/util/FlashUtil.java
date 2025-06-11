package tv.lycam.gift.util;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import okhttp3.ResponseBody;

/**
 * Created by su on 16/6/8.
 */
public class FlashUtil {

    private static File flashDir;
    public static String FLASH_DIR = "flashAnims";

    /**
     * 下载zip
     *
     * @param archive
     * @param decompressDir
     * @return
     */
    public static boolean unZipFile(File archive, String decompressDir) {
        try {
            BufferedInputStream bi;
            ZipFile zf = new ZipFile(archive, ZipFile.OPEN_READ);
            Enumeration e = zf.entries();
            while (e.hasMoreElements()) {
                ZipEntry ze2 = (ZipEntry) e.nextElement();
                String entryName = ze2.getName();
                String path = decompressDir + "/" + entryName;
                if (ze2.isDirectory()) {
                    System.out.println("正在创建解压目录 - " + entryName);
                    File decompressDirFile = new File(path);
                    if (!decompressDirFile.exists()) {
                        decompressDirFile.mkdirs();
                    }
                } else {
                    System.out.println("正在创建解压文件 - " + entryName);
                    String fileDir = path.substring(0, path.lastIndexOf("/"));
                    File fileDirFile = new File(fileDir);
                    if (!fileDirFile.exists()) {
                        fileDirFile.mkdirs();
                    }
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(decompressDir + "/" + entryName));
                    bi = new BufferedInputStream(zf.getInputStream(ze2));
                    byte[] readContent = new byte[1024];
                    int readCount = bi.read(readContent);
                    while (readCount != -1) {
                        bos.write(readContent, 0, readCount);
                        readCount = bi.read(readContent);
                    }
                    bos.close();
                }
            }
            zf.close();
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 写文件
     *
     * @param context
     * @param body
     * @param filename
     * @return
     */
    public static boolean writeResponseBodyToDisk(Context context, ResponseBody body, String filename) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File flashDir = getFlashDir(context);
        File dst = new File(flashDir, filename + ".zip");
        try {
            byte[] fileReader = new byte[4096];

            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;

            inputStream = body.byteStream();
            outputStream = new FileOutputStream(dst);

            while (true) {
                int read = inputStream.read(fileReader);

                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
                fileSizeDownloaded += read;
            }

            outputStream.flush();

        } catch (IOException e) {
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return unZipFile(dst, flashDir.getAbsolutePath());
    }


    public static boolean isZipExist(Context context, String filename) {

        File flashDir = getFlashDir(context);
        File dst = new File(flashDir, filename + ".zip");
        return dst.exists();
    }

    public static File getFlashDir(Context context) {
        if (flashDir == null) {
            flashDir = new File(String.valueOf(getFlashDir(context, FLASH_DIR)));
        }
        return flashDir;
    }

    private static String getFlashDir(Context ctx, String flashDir) {
        String path;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return null;
        }
        path = path + "/" + "." + ctx.getPackageName();
        String animDir = path + "/" + flashDir;
        File file = new File(animDir);
        if (file.exists()) {
            if (!file.isDirectory()) {
                file.delete();
                if (file.mkdirs()) {
                    return animDir;
                } else {
                    return null;
                }
            }
        } else {
            if (file.mkdirs()) {
                return animDir;
            } else {
                return null;
            }
        }
        return animDir;
    }
}
