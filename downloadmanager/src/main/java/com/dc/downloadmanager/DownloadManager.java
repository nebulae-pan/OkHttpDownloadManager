package com.dc.downloadmanager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pxh on 2016/2/15.
 * 管理下载任务
 */
public class DownloadManager implements DownloadTask.CompletedListener
{
    static DownloadManager mManager;
    Context context;
    static private DaoMaster daoMaster;
    static private DaoSession daoSession;

    private DownloadEntityDao downloadDao;

    String downLoadPath = "";

    private int nThread;

    final Object updateLock = new Object();//更新界面进程的互斥锁
    boolean isUpdating = false;
    DownloadUpdateListener mDownloadUpdate;

    private Handler mHandler;

    LinkedList<TransferTask> taskList;

    ExecutorService executorService;

    private DownloadManager(Context context,int nThread)
    {
        this.context=context;
        init(nThread);
    }

    private void init(int nThread)
    {
        mHandler = new Handler(Looper.getMainLooper());
        this.nThread = nThread;
        executorService = Executors.newFixedThreadPool(this.nThread);
        taskList = new LinkedList<>();
        this.nThread = nThread;
        downloadDao = getDaoSession(context).getDownloadEntityDao();
    }

    public void addTask(String url,String fileName)
    {
        //注册完成事件，方便任务完成后将实例移出实例集合
        DownloadTask task = new DownloadTask(fileName, url, SDCardUtils.getSDCardPath() + downLoadPath, downloadDao);
        task.setCompletedListener(this);
        taskList.add(task);
        executorService.execute(task);
        startUpdateUI();
    }

    public void restartTask(int index)
    {
        ((DownloadTask) taskList.get(index)).setCompletedListener(this);
        executorService.execute(taskList.get(index));
        startUpdateUI();//若无更新线程，重新启动
    }

    /**
     * Can get downloading task list
     * @return
     */
    public LinkedList<TransferTask> getTaskList()
    {
        return taskList;
    }

    public void pauseTask(int index)
    {
        DownloadTask task = (DownloadTask) taskList.get(index);
        if (task != null)
            task.setState(LoadState.PAUSE);
        else
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
        //删除数据库中的数据
        downloadDao.deleteByKey(url);
    }

    static public DownloadManager getInstance(Context context)
    {
        if (mManager == null) {
            synchronized (DownloadManager.class) {
                if (mManager == null) {
                    mManager = new DownloadManager(context, 3);
                }
            }
        }
        return mManager;
    }

    public void setThreadNum(int nThread)
    {
        this.nThread = nThread;
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
        Log.v("task finished", "task : " + url + " download completed");
        DownloadTask task = getTask(url);
        if (task != null)
            taskList.remove(task);
        else
            Log.e("isFinished", "task=null");
    }

    public void setUpdateListener(DownloadUpdateListener updateListener)
    {
        this.mDownloadUpdate=updateListener;
    }

    /**
     * 需判断状态，全部暂停后停止更新界面
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
                            if(mDownloadUpdate==null)return;
                            mDownloadUpdate.OnUIUpdate();
                            Log.v("123","123123");
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
        Log.v("Update UI Thread","thread stop");
    }

    protected void ifNeedStopUpdateUI()
    {
        for (TransferTask task : taskList) {
            if (task.getState() == LoadState.DOWNLOADING||task.getState()==LoadState.PREPARE)
                return;
        }
        stopUpdateUI();
    }

    /**
     * get DaoMaster
     *
     * @param context
     * @return
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
     *
     * @param context
     * @return
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

    public interface DownloadUpdateListener
    {
        void OnUIUpdate();
    }
}
