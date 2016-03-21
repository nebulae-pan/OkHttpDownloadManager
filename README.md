# OkHttpDownloadManager v1.0
##introduce
* a simple file download manager developed by okhttp
* support breakpoint download

##usage

###simple download
firstly,initial download manager use this.getApplicationContext() in Activity class.

    DownloadManager.getInstance(this.getApplicationContext(), handler);
or in Application class.

    DownloadManager.getInstance(this, handler);
handler is create at UI thread , and you can update UI by this paramter.
After initialize , use getInstance() in anywhere and will get DownloadManager instance.

    DownloadManager manager = DownloadManager.getInstance();
if you need show download tasks infomation,you can get downloading tasks by

    downloadManager.getTaskList();
return value is a list consist by class TranferTask , can use as paramter to ListView/RecyclerView's Adapater.


###other api
