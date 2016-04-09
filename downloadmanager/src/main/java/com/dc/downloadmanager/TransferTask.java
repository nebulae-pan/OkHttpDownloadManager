package com.dc.downloadmanager;
import android.util.Log;

import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;

/**
 * Created by pxh on 2016/2/16.
 *
 */
abstract public class TransferTask extends TaskInfo implements Runnable
{
    protected String suffix;

    protected String url;

    protected String saveDirPath;
    protected OkHttpClient client;

    RandomAccessFile file;

    int state = LoadState.PREPARE;

    public void setSaveDirPath(String saveDirPath)
    {
        this.saveDirPath = saveDirPath;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    abstract public String getSuffix();

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getSaveDirPath()
    {
        return saveDirPath;
    }

    @Override
    abstract public void run();

}
