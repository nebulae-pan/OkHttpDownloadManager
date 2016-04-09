package com.dc.downloadmanager;

/**
 * Created by pxh on 2016/4/9.
 */
public class TaskInfo
{
    protected String fileName;

    protected long taskSize;

    protected long completedSize;

    public TaskInfo()
    {
    }

    public TaskInfo(String fileName, long taskSize, long completedSize)
    {
        this.fileName = fileName;
        this.taskSize = taskSize;
        this.completedSize = completedSize;
    }

    public long getTaskSize()
    {
        return taskSize;
    }

    public void setTaskSize(long taskSize)
    {
        this.taskSize = taskSize;
    }

    public long getCompletedSize()
    {
        return completedSize;
    }

    public void setCompletedSize(long completedSize)
    {
        this.completedSize = completedSize;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
}
