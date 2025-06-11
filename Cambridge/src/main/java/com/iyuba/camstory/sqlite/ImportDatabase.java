package com.iyuba.camstory.sqlite;

import android.content.Context;
import android.os.Environment;


import com.iyuba.camstory.R;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.voa.frame.components.ConfigManagerVOA;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 导入数据库
 *
 * @author chentong
 */
public class ImportDatabase {
    private final int BUFFER_SIZE = 400000;
    // public static final Object writeLock=new Object();
//	public static final String DB_NAME = "voa_database.sqlite"; // 保存的数据库文件名
    public static final String DB_NAME = "fiction_data.sqlite"; // 保存的数据库文件名
    public static String PACKAGE_NAME = "";
    public static String DB_PATH = ""; // 在手机里存放数据库的位置
    private int lastVersion, currentVersion;
    private Context mContext;
    private CrashApplication crashApplication;

    public void setLastVersion(int lastVersion) {
        this.lastVersion = lastVersion;
    }

    public ImportDatabase(Context context) {
        mContext = context;
        PACKAGE_NAME = CrashApplication.getInstance().getBaseContext()
                .getPackageName();
        DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath()
                + "/" + PACKAGE_NAME + "/" + "databases";
    }

    public void setVersion(CrashApplication crashApplication, int curVersion, int lastVeision) {
        this.crashApplication = crashApplication;
        this.lastVersion = lastVeision;
        this.currentVersion = curVersion;
    }

    /**
     * 修改后，此函数作为第一次运行时的创建数据库函数
     * @param dbfile
     */
    public synchronized void copyDatabase(String dbfile) {
        lastVersion = ConfigManagerVOA.Instance(crashApplication).loadInt("database_version");
        File database = new File(dbfile);
        if (currentVersion > lastVersion) {
            if (database.exists()) {
                database.delete();
            }
            ConfigManagerVOA.Instance(crashApplication).putInt("database_version", currentVersion);
            if (!database.exists()) {// 判断数据库文件是否存在，若不存在则执行导入
                loadDataBase(dbfile);
            }
        } else {
            if (!database.exists()) {// 判断数据库文件是否存在，若不存在则执行导入
                loadDataBase(dbfile);
            }
        }

    }

    /**
     * 将数据库文件拷贝到需要的位置
     *
     * @param dbfile
     */

    private void loadDataBase(String dbfile) {
        final String dbfile1 = dbfile;
        // TODO 自动生成的方法存根
        try {
//			InputStream is = CrashApplication.getInstance().getResources()
//					.openRawResource(R.raw.voa_database); // 欲导入的数据库
            InputStream is = CrashApplication.getInstance().getResources()
                    .openRawResource(R.raw.fiction_data); // 欲导入的数据库
            BufferedInputStream bis = new BufferedInputStream(is);
            if (!(new File(DB_PATH).exists())) {
                new File(DB_PATH).mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(dbfile1);
            BufferedOutputStream bfos = new BufferedOutputStream(fos);
            byte[] buffer = new byte[BUFFER_SIZE];
            int count = 0;
            while ((count = bis.read(buffer)) > 0) {
                bfos.write(buffer, 0, count);
            }
            fos.close();
            is.close();
            bis.close();
            bfos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
