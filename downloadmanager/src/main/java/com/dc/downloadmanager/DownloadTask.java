package com.dc.downloadmanager;

import android.os.Handler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by pxh on 2016/2/11.
 * 传输过程中的任务信息
 */
public class DownloadTask extends TransferTask
{
    private Handler mHandler;

    int subThreadNum=3;
    private DownloadEntityDao downloadDao;
    private CompletedListener completedListener;
    private ThreadTask[] tasks;

    /**
     * 保存的信息
     */
    private final DownloadEntity downloadEntity;


    public DownloadTask(String fileName, String url, String saveDirPath, DownloadEntityDao downloadDao)
    {
        this.url = url;
//        mHandler = new Handler(Looper.getMainLooper());
        this.saveDirPath = saveDirPath;
        this.fileName = fileName;
        client = new OkHttpClient();
        this.state = LoadState.PREPARE;
        this.suffix = obtainSuffix();
        this.downloadDao = downloadDao;
        downloadEntity = new DownloadEntity();
        downloadEntity.setCompletedSize(0L);
        downloadEntity.setTaskSize(taskSize);
        downloadEntity.setUrl(url);
        downloadEntity.setFileName(fileName);
        downloadEntity.setSaveDirPath(saveDirPath);
        //新建任务
        this.downloadDao.insertOrReplace(downloadEntity);
    }

    public DownloadTask(DownloadEntityDao downloadDao, DownloadEntity downloadEntity)
    {
        client = new OkHttpClient();
        this.downloadDao = downloadDao;
        this.downloadEntity = downloadEntity;
        this.url = downloadEntity.getUrl();
        this.saveDirPath = downloadEntity.getSaveDirPath();
        this.fileName = downloadEntity.getFileName();
        this.completedSize = downloadEntity.getCompletedSize();
        this.taskSize = downloadEntity.getTaskSize();
        this.state = LoadState.PREPARE;
        suffix = obtainSuffix();
    }

    @Override
    public void run()
    {
        InputStream inputStream = null;
        BufferedInputStream bis = null;
        try {
            SDCardUtils.isExist(saveDirPath);
            /*file = new RandomAccessFile(saveDirPath + fileName, "rwd");
            if (file.length() < completedSize) {
                completedSize = 0;
            }*/
            state = LoadState.START;
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                System.out.println("resource not found");
                return;
            }
            state = LoadState.DOWNLOADING;
            if (completedSize == 0) {
                taskSize = responseBody.contentLength();
                downloadEntity.setTaskSize(taskSize);
            }
            updateCompleteSize();
            if (state == LoadState.PAUSE) {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            try {
                if (file != null)
                    file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        state = LoadState.COMPLETED;
        //通知Manager任务已经结束
        completedListener.isFinished(url);
    }

    @Override
    public String toString()
    {
        return "DownloadTask{" +
                "fileName='" + fileName + '\'' +
                ", taskSize=" + taskSize +
                ", completeSize=" + completedSize +
                ", suffix='" + suffix + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getSuffix()
    {
        return suffix;
    }

    private String obtainSuffix()
    {
        int index = fileName.lastIndexOf(".");
        if (index < 0) {
            return null;
        }
        return fileName.substring(index + 1).toLowerCase(Locale.getDefault());
    }

    interface CompletedListener
    {
        void isFinished(String url);
    }

    public void setCompletedListener(CompletedListener completedListener)
    {
        this.completedListener = completedListener;
    }

    public void setHandler(Handler mHandler)
    {
        this.mHandler = mHandler;
    }

    private void updateCompleteSize()
    {
        downloadEntity.setCompletedSize(completedSize);
        downloadDao.insertOrReplace(downloadEntity);
    }
}

