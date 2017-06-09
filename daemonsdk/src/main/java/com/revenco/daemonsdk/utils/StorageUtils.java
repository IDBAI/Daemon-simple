package com.revenco.daemonsdk.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/1/3.
 * Android 标准文件夹使用方式，请使用这个类去创建文件夹或者文件
 */
public class StorageUtils {
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String TAG = "StorageUtils";

    private StorageUtils() {
    }

    /**
     * 获得应用的缓存目录
     * 1、优先获取  /storage/sdcard1/Android/data/<application package>/cache ，如果获取失败
     * 2、则获取  /data/data/<application package>/cache ，应用的私有目录
     *
     * @param context
     * @return
     */
    public static File getCacheDirectory(Context context) {
        return getCacheDirectory(context, true);
    }

    /**
     * 获得应用的缓存目录
     * 1、优先获取  /storage/sdcard1/Android/data/<application package>/cache ，如果获取失败
     * 2、则获取  /data/data/<application package>/cache ，应用的私有目录
     *
     * @param context
     * @param preferExternal 是否优先获取扩展卡的目录
     * @return
     */
    public static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;
        if (preferExternal && "mounted".equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            //getCacheDir()方法用于获取/data/data/<application package>/cache目录
            //getFilesDir()方法用于获取/data/data/<application package>/files目录
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            Log.w(TAG, "Can\'t define system cache directory! \'%s\' will be used.");
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    /**
     * 获取自定义的缓存文件夹
     * 1、优先获取  /storage/sdcard1/Android/data/<application package>/cacheDir ，如果获取失败
     * 2、则获取  /data/data/<application package>/cacheDir ，应用的私有目录
     *
     * @param context
     * @param cacheDir 自定义的文件夹名称
     * @return
     */
    public static File getOwnCacheDirectory(Context context, String cacheDir) {
        File appCacheDir = null;
        if ("mounted".equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
        }
        if (appCacheDir == null || !appCacheDir.exists() && !appCacheDir.mkdirs()) {
            appCacheDir = new File(context.getCacheDir(), cacheDir);
        }
        return appCacheDir;
    }

    /**
     * 在扩展卡  /storage/sdcard1/Android/data/<application package>/data 目录基础上创建文件
     * name: group/photo/readme.md
     * 将会创建文件：  /storage/sdcard1/Android/data/<application package>/data/group/photo/readme.md
     *
     * @return
     */
    public static File getFilePathOnData(Context context, String name) {
        File file = new File(getExternalDataDir(context).getAbsolutePath() + "/" + name);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();//mkdirs能够级联创建文件夹
            }
            try {
                file.createNewFile();//创建文件，必须要存在父文件夹
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 在扩展卡  /storage/sdcard1/Android/data/<application package>/data 目录基础上创建目录
     * name: group/photo
     * 将会创建文件夹：  /storage/sdcard1/Android/data/<application package>/data/group/photo 目录
     *
     * @param context
     * @param name
     * @return
     */
    public static File getDirOnData(Context context, String name) {
        File file = new File(getExternalDataDir(context).getAbsolutePath() + "/" + name);
        if (!file.exists() || !file.isDirectory()) {
            boolean mkdirs = file.mkdirs();//mkdirs能够级联创建文件夹
            if (!mkdirs) {
                System.out.println("make dir failed!");
                System.out.println("maybe exists the same name file,delete it and make dir again!");
                file.delete();
                mkdirs = file.mkdirs();
                if (mkdirs)
                    System.out.println("make dir success!");
            }
        }
        return file;
    }

    /**
     * 在扩展卡  /storage/sdcard1/Android/data/<application package>/cache 目录基础上创建目录
     * name: group/photo
     * 将会创建文件：  /storage/sdcard1/Android/data/<application package>/cache/group/photo 目录
     *
     * @param context
     * @param name
     * @return
     */
    public static File getDirOnCache(Context context, String name) {
        File file = new File(getCacheDirectory(context).getAbsolutePath() + "/" + name);
        if (!file.exists() || !file.isDirectory()) {
            boolean mkdirs = file.mkdirs();//mkdirs能够级联创建文件夹
            if (!mkdirs) {
                System.out.println("make dir failed!");
                System.out.println("maybe exists the same name file,delete it and make dir again!");
                file.delete();
                mkdirs = file.mkdirs();
                if (mkdirs)
                    System.out.println("make dir success!");
            }
        }
        return file;
    }

    /**
     * 在扩展卡  /storage/sdcard1/Android/data/<application package>/cache 目录基础上创建文件
     * name: group/photo/readme.md
     * 将会创建文件：  /storage/sdcard1/Android/data/<application package>/cache/group/photo/readme.md
     *
     * @param context
     * @param name
     * @return
     */
    public static File getFilePathOnCache(Context context, String name) {
        File file = new File(getCacheDirectory(context).getAbsolutePath() + "/" + name);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();//mkdirs能够级联创建文件夹
            }
            try {
                file.createNewFile();//创建文件，必须要存在父文件夹
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 获取扩展卡的缓存目录：
     * /storage/sdcard1/Android/data/<application package>/cache
     *
     * @param context
     * @return
     */
    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                Log.w(TAG, "Unable to create external cache directory");
                return null;
            }
            try {
                (new File(appCacheDir, ".nomedia")).createNewFile();
            } catch (IOException var4) {
                Log.i(TAG, "Can\'t create \".nomedia\" file in application external cache directory");
            }
        }
        return appCacheDir;
    }

    /**
     * 获取扩展卡下面的text文件夹目录
     * /storage/sdcard1/Android/data/<application package>/text
     *
     * @param context
     * @return
     */
    public static File getExternalTextDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "text");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                Log.w(TAG, "Unable to create external text directory");
                return null;
            }
            try {
                (new File(appCacheDir, ".nomedia")).createNewFile();
            } catch (IOException var4) {
                Log.i(TAG, "Can\'t create \".nomedia\" file in application external cache directory");
            }
        }
        return appCacheDir;
    }

    /**
     * 获取扩展卡的data目录：
     * /storage/sdcard1/Android/data/<application package>/data
     *
     * @param context
     * @return
     */
    public static File getExternalDataDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "data");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                Log.w(TAG, "Unable to create external text directory");
                return null;
            }
            try {
                (new File(appCacheDir, ".nomedia")).createNewFile();
            } catch (IOException var4) {
                Log.i(TAG, "Can\'t create \".nomedia\" file in application external cache directory");
            }
        }
        return appCacheDir;
    }

    /**
     * @param file 文件是否存在
     * @return
     */
    public static boolean exists(File file) {
        return file == null ? false : file.exists();
    }

    /**
     * 是否有扩展卡的写权限
     *
     * @param context
     * @return
     */
    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == 0;
    }
}
