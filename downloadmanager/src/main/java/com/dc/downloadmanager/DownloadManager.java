package com.dc.downloadmanager;

import android.content.Context;
import android.os.Handler;
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
    private static final String TAG = "DownLoadManager";

    final Object updateLock = new Object();//更新界面进程的互斥锁
    boolean isUpdating = false;

    private Handler mHandler;

    LinkedList<TransferTask> taskList;
    //    private ConcurrentHashMap<String,DownloadTask> taskList;
    ExecutorService executorService;

    public void addTask(String url)
    {
        //注册完成事件，方便任务完成后将实例移出实例集合
        startUpdateUI();
        DownloadTask task = new DownloadTask("1.apk", url, SDCardUtils.getSDCardPath() + downLoadPath, downloadDao);
        task.setCompletedListener(this);
        taskList.add(task);
        executorService.execute(task);
    }

    public void restartTask(int index)
    {
        startUpdateUI();//若无更新线程，重新启动
        ((DownloadTask) taskList.get(index)).setCompletedListener(this);
        executorService.execute(taskList.get(index));
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
        ifNeedStopUpdateUI();
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
        ifNeedStopUpdateUI();
    }

    private void init(int nThread)
    {
        this.nThread = nThread;
        executorService = Executors.newFixedThreadPool(this.nThread);
        taskList = new LinkedList<>();
        this.nThread = nThread;
        downloadDao = getDaoSession(context).getDownloadEntityDao();
    }

    private DownloadManager(int nThread)
    {
        init(nThread);
    }

    private DownloadManager(Context context, int nThread, Handler handler)
    {
        this.context = context;
        this.mHandler = handler;
        init(nThread);
    }

    static public DownloadManager getInstance()
    {
        if (mManager == null) {
            synchronized (DownloadManager.class) {
                if (mManager == null) {
                    mManager = new DownloadManager(3);
                }
            }
        }
        return mManager;
    }

    static public DownloadManager getInstance(Context context, Handler handler)
    {
        if (mManager == null) {
            synchronized (DownloadManager.class) {
                if (mManager == null) {
                    mManager = new DownloadManager(context, 3, handler);
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
        Log.v("task finished", "task" + url + "download completed");
        DownloadTask task = getTask(url);
        if (task != null)
            taskList.remove(task);
        else
            Log.e("isFinished", "task=null");
        mHandler.sendEmptyMessage(1);
        ifNeedStopUpdateUI();
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
                    mHandler.sendEmptyMessage(1);
                    Log.v("timer", "updating");
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
    }

    protected void ifNeedStopUpdateUI()
    {
        mHandler.sendEmptyMessage(1);//更新界面
        for (TransferTask task : taskList) {
            if (task.getState() == LoadState.DOWNLOADING)
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
}
