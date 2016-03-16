package com.dc.downloadmanager;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;

/**
 * Created by pxh on 2016/2/16.
 *
 */
abstract public class TransferTask implements Runnable
{
    protected long taskSize;
    protected long completedSize;
    protected String suffix;

    protected String url;

    protected String fileName;
    protected String saveDirPath;
    protected OkHttpClient client;

    RandomAccessFile file;

    int state = LoadState.PREPARE;

    public long getTaskSize()
    {
        return taskSize;
    }

    public long getCompletedSize()
    {
        return completedSize;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public void setSaveDirPath(String saveDirPath)
    {
        this.saveDirPath = saveDirPath;
    }

    public void setTaskSize(long taskSize)
    {
        this.taskSize = taskSize;
    }

    public void setCompletedSize(long completedSize)
    {
        this.completedSize = completedSize;
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
    //abstract public void reStartTask(String url);

    //abstract public void cancelTask(String url);
}
