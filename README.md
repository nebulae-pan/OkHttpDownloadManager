# OkHttpDownloadManager v1.0
##introduction
* a simple file download manager developed by okHttp
* support breakpoint download
* support multi-thread download

##usage
###simple download
firstly,add permission at AndroidManifest.xml

    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
then,initial download manager use this.getApplicationContext() in Activity class.

    DownloadManager.getInstance(this.getApplicationContext());
or in Application class.

    DownloadManager.getInstance(this);
After initialize , use getInstance() in anywhere and will get DownloadManager instance.

    DownloadManager manager = DownloadManager.getInstance();
if you need show download tasks information,you can get downloading tasks by

    downloadManager.getTaskList();
return value is a list consist by class TransferTask , can use as parameter to ListView/RecyclerView's Adapter.

###other api

    downloadManager.pause(url);
    downloadManager.restart(url);

![single download task](https://github.com/nebulae-pan/OkHttpDownloadManager/blob/master/device-2016-03-21-214932.png)
