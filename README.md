# OkHttpDownloadManager v1.0
##introduction
* a file download manager support by okHttp
* support breakpoint download
* support multi-thread download

##usage
add dependency in gradle

    compile 'nebulae.library.wheel:downloadmanager:1.0.1'
###simple download
firstly,add permission at AndroidManifest.xml

    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
then use follow code to start your download
    
    DownloadManagerConfig config = new DownloadManagerConfig()          //get Config
                    .setMaxTasksNumber(3)                               //Config Max concurrent numbers of tasks
                    .setSingleTaskThreadNumber(3)                       //Config the threads' number of single task
                    .setSavePath("");                                   //Config the Path to save files
    DownloadManager.init(this.getApplicationContext(), config);         //initial manager in Application
    //DownloadManager.init(this.getApplicationContext(), config); or in Activity use getApplicationContext()
    DownloadManager downloadManager = DownloadManager.getInstance();    //getInstance
    downloadManager.addTask(url, fileName);                             //start a download task
if you need show download tasks information at Activity, you can get downloading tasks list by

    ArrayList<TransferTask> list = downloadManager.getTaskList();
return value is a list consist by class TransferTask , can use as the parameter of ListView/RecyclerView's Adapter.
Implement interface DownloadManager.DownloadUpdateListener in Activity,and update UI

    @Override
    public void OnUIUpdate()
    {
        //update operation, like
        //adapter.notifyDataSetChanged();
    }

###other api

    downloadManager.pauseTask(url);     //pause a task is being download
    downloadManager.cancelTask(url);    //cancel a task
    downloadManager.reStart(url);       //restart a paused task
    downloadManager.getCompletedTasks();//get completed tasks' list
    downloadManager.deleteAllData();    //clear data
###download demo
####single task downloading
![single download task](https://github.com/nebulae-pan/OkHttpDownloadManager/blob/master/device-2016-03-21-214932.png)

####exceed limit's tasks block
![single download task](https://github.com/nebulae-pan/OkHttpDownloadManager/blob/master/device-2016-04-02-231252.png)

![single download task](https://github.com/nebulae-pan/OkHttpDownloadManager/blob/master/device-2016-03-31-213811.png)

####download completed
![single download task](https://github.com/nebulae-pan/OkHttpDownloadManager/blob/master/device-2016-03-30-221358.png)

