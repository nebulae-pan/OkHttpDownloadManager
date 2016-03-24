package com.dc.downloadmanager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by pxh on 2016/3/24.
 * single downloading thread
 */
public class ThreadTask extends Thread
{
    String url;
    int threadId;
    long threadTaskSize;
    long threadComplete;
    String fileSavePath;
    DownloadTask downloadTask;

    public ThreadTask(String url, int threadId, long threadTaskSize, long threadComplete, String fileSavePath,
                      DownloadTask downloadTask)
    {
        this.url = url;
        this.threadId = threadId;
        this.threadTaskSize = threadTaskSize;
        this.threadComplete = threadComplete;
        this.fileSavePath = fileSavePath;
        this.downloadTask = downloadTask;
    }

    @Override
    public void run()
    {
        int start = (int) (threadTaskSize * (threadId - 1) + threadComplete);//start position
        int end = (int) (threadTaskSize * threadId - 1);//end position
        OkHttpClient client = new OkHttpClient();
        //build request
        Request request = new Request.Builder()
                .url(url)
                .header("RANGE", "bytes=" + start + "-" + end)
                .build();
        BufferedInputStream bis = null;
        RandomAccessFile file = null;
        Response response;
        try {
            response = client.newCall(request).execute();
            bis = new BufferedInputStream(response.body().byteStream());
            file = new RandomAccessFile(fileSavePath, "rwd");

            byte[] buff = new byte[5 * 1024];
            file.seek(start);
            int len;
            //continual download until complete or the LoadState change
            while ((len = bis.read(buff)) > 0 && downloadTask.getState() == LoadState.DOWNLOADING) {
                file.write(buff, 0, len);
                threadComplete += len;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null)
                    bis.close();
                if (file != null)
                    file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public long getThreadComplete()
    {
        return threadComplete;
    }
}
