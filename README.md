# OkHttpDownloadManager v1.0
##introduction
* a simple file download manager developed by okhttp
* support breakpoint download

##usage

###simple download
firstly,add permisson at AndroidManifest.xml

    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
then,initial download manager use this.getApplicationContext() in Activity class.

    DownloadManager.getInstance(this.getApplicationContext());
or in Application class.

    DownloadManager.getInstance(this);
handler is create at UI thread , and you can update UI by this paramter.
After initialize , use getInstance() in anywhere and will get DownloadManager instance.

    DownloadManager manager = DownloadManager.getInstance();
if you need show download tasks infomation,you can get downloading tasks by

    downloadManager.getTaskList();
return value is a list consist by class TranferTask , can use as paramter to ListView/RecyclerView's Adapater.


###other api
