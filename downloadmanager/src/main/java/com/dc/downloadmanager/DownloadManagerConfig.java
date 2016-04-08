package com.dc.downloadmanager;

/**
 * Created by pxh on 2016/4/8.
 * Download Manager's Configs
 */
public class DownloadManagerConfig
{
    int nThread;
    int subThreadNumber;
    String downloadSavePath;

    public DownloadManagerConfig()
    {
        nThread = 1;
        subThreadNumber = 1;
        downloadSavePath = "";
    }

    /**
     * set concurrent tasks' number, range at 1~10. default:1
     * @param threadNumber threadNumber
     * @return Config Instance
     */
    public DownloadManagerConfig setMaxTasksNumber(int threadNumber)
    {
        this.nThread = threadNumber;
        return this;
    }

    /**
     * set File SavePath, default: null
     * @param savePath savePath
     * @return Config Instance
     */
    public DownloadManagerConfig setSavePath(String savePath)
    {
        this.downloadSavePath = savePath;
        return this;
    }

    /**
     * set Download Threads' Number for a single task,range at 1~5 ,default: 1
     * @param threadNumber threadNumber
     * @return Config Instance
     */
    public DownloadManagerConfig setSingleTaskThreadNumber(int threadNumber)
    {
        this.subThreadNumber = threadNumber;
        return this;
    }
}
