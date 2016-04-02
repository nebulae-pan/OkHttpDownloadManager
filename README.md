# OkHttpDownloadManager v1.0
##introduction
* a simple file download manager support by okHttp
* support breakpoint download
* support multi-thread download
* custom parallel tasks' number and sub-thread's number of a task

##usage
###simple download
firstly,add permission at AndroidManifest.xml

    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
then,initial download manager use this.getApplicationContext() in Activity class.

    DownloadManager.init(this.getApplicationContext());
or in Application class.

    DownloadManager.init(this);
After initialize , use getInstance() in anywhere and will get DownloadManager instance.

    DownloadManager manager = DownloadManager.getInstance();
if you need show download tasks information at Activity, you can get downloading tasks list by

    downloadManager.getTaskList();
return value is a list consist by class TransferTask , can use as the parameter of ListView/RecyclerView's Adapter.
Implement interface DownloadManager.DownloadUpdateListener in Activity,and update UI

    @Override
    public void OnUIUpdate()
    {
        adapter.notifyDataSetChanged();
    }

###other api

    downloadManager.pause(url);
    downloadManager.cancel(url);
    downloadManager.restart(url);

###single task downloading
![single download task](https://github.com/nebulae-pan/OkHttpDownloadManager/blob/master/device-2016-03-21-214932.png)
