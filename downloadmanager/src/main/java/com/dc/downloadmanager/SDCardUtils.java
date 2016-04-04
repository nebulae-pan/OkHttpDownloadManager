package com.dc.downloadmanager;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;

/**
 * Created by pxh on 2016/2/15.
 */
public class SDCardUtils
{
    private SDCardUtils()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * estimate SDCard whether enable
     *
     * @return
     */
    public static boolean isSDCardEnable()
    {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    /**
     * get SDCard's path
     *
     * @return
     */
    public static String getSDCardPath()
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * get the available size of SDCard
     *
     * @return
     */
    @TargetApi(18)
    public static long getSDCardAllSize()
    {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // get free data block's number
            if (Build.VERSION.SDK_INT >= 18) {
                long availableBlocks = (long) stat.getAvailableBlocksLong() - 4;
                // get the size of a single data block(byte)
                long freeBlocks = stat.getAvailableBlocksLong();
                return freeBlocks * availableBlocks;
            } else {
                long availableBlocks = (long) stat.getAvailableBlocks() - 4;
                // get the size of a single data block(byte)
                long freeBlocks = stat.getAvailableBlocks();
                return freeBlocks * availableBlocks;
            }

        }
        return 0;
    }

    /**
     * get assign appoint path's space available size,unit:byte
     *
     * @param filePath
     * @return SDCard or memory's available space
     */
    public static long getFreeBytes(String filePath)
    {
        //if the path below SDCard,get SDCard's available space
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {//else get memory's
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * get system store path
     *
     * @return
     */
    public static String getRootDirectoryPath()
    {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    public static void isExist(String path)
    {
        File file = new File(path);
        if (!file.exists()) {
            boolean b=file.mkdirs();
            if(!b)
            {
                Log.v("mkdir","mkdir error");
            }
        }
    }

}
