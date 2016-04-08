package com.dc.downloadmanager;

/**
 * Created by pxh on 2016/4/8.
 */
public class DownloadManagerConfig
{
    int nThread;
    int subThreadNumber;
    String downloadSavePath;

    public DownloadManagerConfig setMaxTasksNumber(int threadNumber)
    {
        this.nThread = threadNumber;
        return this;
    }

    public DownloadManagerConfig setSavePath(String savePath)
    {
        this.downloadSavePath = savePath;
        return this;
    }

    public DownloadManagerConfig setSingleTaskThreadNumber(int threadNumber)
    {
        this.subThreadNumber = threadNumber;
        return this;
    }
}
