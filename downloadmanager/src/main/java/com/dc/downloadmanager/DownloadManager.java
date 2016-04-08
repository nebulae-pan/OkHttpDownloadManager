package com.dc.downloadmanager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pxh on 2016/2/15.
 * 管理下载任务
 */
public class DownloadManager implements DownloadTask.CompletedListener
{
    Context context;

    static DownloadManager mManager;
    static private DaoMaster daoMaster;
    static private DaoSession daoSession;
    private DownloadEntityDao downloadDao;

    private DownloadManagerConfig config;

    final Object updateLock = new Object();//update thread mutex lock
    boolean isUpdating = false;
    DownloadUpdateListener mDownloadUpdate;

    private Handler mHandler;

    LinkedList<TransferTask> taskList;

    ExecutorService executorService;

    static public void init(Context context, DownloadManagerConfig config)
    {
        if (mManager == null)
            synchronized (DownloadManager.class) {
                if (mManager == null)
                    mManager = new DownloadManager(context, config);
            }
    }

    private DownloadManager(Context context, DownloadManagerConfig config)
    {
        this.context = context;
        checkConfig(config);
        mHandler = new Handler(Looper.getMainLooper());
        executorService = Executors.newFixedThreadPool(config.nThread);
        taskList = new LinkedList<>();
        getDownloadTask();
        downloadDao = getDaoSession(context).getDownloadEntityDao();
    }

    private void checkConfig(DownloadManagerConfig config)
    {
        if (config.nThread > 10)
            config.setMaxTasksNumber(10);
        if (config.nThread <= 0)
            config.setMaxTasksNumber(1);
        if (config.subThreadNumber > 5)
            config.setSingleTaskThreadNumber(5);
        if (config.subThreadNumber <= 0)
            config.setSingleTaskThreadNumber(1);
        this.config = config;
    }

    public void addTask(String url, String fileName)
    {
        //register accomplish callback , when task finish ,remove it from taskList
        DownloadTask task = new DownloadTask(fileName, url, SDCardUtils.getSDCardPath() + config.downloadSavePath,
                config.subThreadNumber, downloadDao);
        task.setCompletedListener(this);
        taskList.add(task);
        executorService.execute(task);
        startUpdateUI();
    }

    public void restartTask(int index)
    {
        ((DownloadTask) taskList.get(index)).setCompletedListener(this);
        executorService.execute(taskList.get(index));
        startUpdateUI();//if there is no update thread , restart
    }

    /**
     * Can get downloading task list
     *
     * @return list of TransferTaskz
     */
    public LinkedList<TransferTask> getTaskList()
    {
        return taskList;
    }

    public void pauseTask(int index)
    {
        DownloadTask task = (DownloadTask) taskList.get(index);
        if (task != null) {
            task.setState(LoadState.PAUSE);
        } else
            Log.e("pauseTask", "task=null");
    }

    public void cancelTask(String url)
    {
        DownloadTask task = getTask(url);
        if (task != null)
            task.setState(LoadState.CANCEL);
        else
            Log.e("cancelTask", "task=null");
        taskList.remove(task);
        //delete the data in the database
        downloadDao.deleteByKey(url);
    }

    static public DownloadManager getInstance()
    {
        if (mManager == null)
            throw new NullPointerException();
        return mManager;
    }

    private DownloadTask getTask(String url)
    {
        for (TransferTask task : taskList) {
            DownloadTask dTask = (DownloadTask) task;
            if (dTask.getUrl().equals(url)) {
                return dTask;
            }
        }
        return null;
    }

    @Override
    public void isFinished(String url)
    {
        DownloadTask task = getTask(url);
        if (task != null)
            taskList.remove(task);
        else
            Log.e("isFinished", "task=null");
    }

    public void setUpdateListener(DownloadUpdateListener updateListener)
    {
        WeakReference<DownloadUpdateListener> reference = new WeakReference<>(updateListener);//prevent memory leak
        this.mDownloadUpdate = reference.get();
    }

    /**
     * estimate all tasks' state , if there is haven't downloading or preparing task ,stop this thread
     */
    Runnable updateUIByOneSecond = new Runnable()
    {
        @Override
        public void run()
        {
            synchronized (updateLock) {
                if (isUpdating) {
                    mHandler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if (mDownloadUpdate == null) return;
                            mDownloadUpdate.OnUIUpdate();
                        }
                    });
                    ifNeedStopUpdateUI();
                    mHandler.postDelayed(this, 1000);
                }
            }
        }
    };

    protected void startUpdateUI()
    {
        synchronized (updateLock) {
            if (!isUpdating) {
                isUpdating = true;
                new Thread(updateUIByOneSecond).start();
            }
        }
    }

    protected void stopUpdateUI()
    {
        synchronized (updateLock) {
            isUpdating = false;
        }
        //Log.v("Update UI Thread", "thread stop");
    }

    protected void ifNeedStopUpdateUI()
    {
        for (TransferTask task : taskList) {
            if (task.getState() == LoadState.DOWNLOADING || task.getState() == LoadState.PREPARE)
                return;
        }
        stopUpdateUI();
    }

    /**
     * get DaoMaster
     */
    public static DaoMaster getDaoMaster(Context context)
    {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, "downloadDB", null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * get DaoSession
     */
    public static DaoSession getDaoSession(Context context)
    {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    private void getDownloadTask()
    {
        DownloadEntityDao downloadEntityDao = getDaoSession(context).getDownloadEntityDao();
        List<DownloadEntity> entityList = downloadEntityDao.loadAll();
        for (DownloadEntity entity : entityList) {
            Log.e("dao", entity.toString());
            if (entity.getCompletedSize().equals(entity.getTaskSize())) {
                //handle already downloaded files
            } else
                taskList.add(new DownloadTask(downloadEntityDao, entity));
        }

    }

    public interface DownloadUpdateListener
    {
        void OnUIUpdate();
    }

}
