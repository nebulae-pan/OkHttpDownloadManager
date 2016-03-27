package com.dc.downloadmanager;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "DOWNLOAD_ENTITY".
 */
public class DownloadEntity {

    private String url;
    private Long taskSize;
    private Long completedSize;
    private String saveDirPath;
    private String fileName;
    private String threadComplete;

    public DownloadEntity() {
    }

    public DownloadEntity(String url) {
        this.url = url;
    }

    public DownloadEntity(String url, Long taskSize, Long completedSize, String saveDirPath, String fileName, String threadComplete) {
        this.url = url;
        this.taskSize = taskSize;
        this.completedSize = completedSize;
        this.saveDirPath = saveDirPath;
        this.fileName = fileName;
        this.threadComplete = threadComplete;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTaskSize() {
        return taskSize;
    }

    public void setTaskSize(Long taskSize) {
        this.taskSize = taskSize;
    }

    public Long getCompletedSize() {
        return completedSize;
    }

    public void setCompletedSize(Long completedSize) {
        this.completedSize = completedSize;
    }

    public String getSaveDirPath() {
        return saveDirPath;
    }

    public void setSaveDirPath(String saveDirPath) {
        this.saveDirPath = saveDirPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getThreadComplete() {
        return threadComplete;
    }

    public void setThreadComplete(String threadComplete) {
        this.threadComplete = threadComplete;
    }

    @Override
    public String toString()
    {
        return "DownloadEntity{" +
                "url='" + url + '\'' +
                ", taskSize=" + taskSize +
                ", completedSize=" + completedSize +
                ", saveDirPath='" + saveDirPath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", threadComplete='" + threadComplete + '\'' +
                '}';
    }
}
