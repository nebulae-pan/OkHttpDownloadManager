package com.dc.okhttpdownloadmanager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dc.downloadmanager.DownloadManager;
import com.dc.downloadmanager.DownloadManagerConfig;
import com.dc.downloadmanager.LoadState;
import com.dc.downloadmanager.TransferTask;

import java.io.File;
import java.util.LinkedList;


public class MainActivity extends AppCompatActivity implements TaskConfirmDialog.InputCompletedListener,
        DownloadManager.DownloadUpdateListener
{
    private ListView listView;
    DownloadManager downloadManager;
    protected Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.listView);
        DownloadManagerConfig config = new DownloadManagerConfig()
                .setMaxTasksNumber(3)
                .setSingleTaskThreadNumber(3)
                .setSavePath("");

        DownloadManager.init(this.getApplicationContext(),config);
        downloadManager = DownloadManager.getInstance();
        downloadManager.setUpdateListener(this);
        setListViewAdapter();
        verifyStoragePermissions(this);
    }

    private static void deleteFilesByDirectory(File directory)
    {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    void setListViewAdapter()
    {
        adapter = new Adapter(this, downloadManager.getTaskList());
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                TaskConfirmDialog dialogFragment = new TaskConfirmDialog();
                android.app.FragmentManager manager = getFragmentManager();
                dialogFragment.show(manager, "");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void inputCompleted(String url, String fileName)
    {

        url = "http://apk.hiapk.com/web/api.do?qt=8051&id=716";
        String url1 = "https://github.com/nebulae-pan/OkHttpDownloadManager/archive/master.zip";
        String url2 = "https://github.com/bxiaopeng/AndroidStudio/archive/master.zip";
        String url3 = "https://github.com/romannurik/AndroidAssetStudio/archive/master.zip";
        String url4 = "https://github.com/facebook/fresco/archive/master.zip";
        String url5 = "https://github.com/bacy/volley/archive/master.zip";
        downloadManager.addTask(url, "123.apk");
        downloadManager.addTask(url1, "1.zip");
        downloadManager.addTask(url2, "2.zip");
        downloadManager.addTask(url3, "3.zip");
        downloadManager.addTask(url4, "4.zip");
        downloadManager.addTask(url5, "5.zip");
    }

    @Override
    public void OnUIUpdate()
    {
        adapter.notifyDataSetChanged();
    }

    /**
     * just a sample
     */
    static class Adapter extends BaseAdapter
    {
        LinkedList<TransferTask> data;
        Context context;

        public Adapter(Context context, LinkedList<TransferTask> data)
        {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount()
        {
            return data.size();
        }

        @Override
        public Object getItem(int position)
        {
            return data.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            if (convertView == null) {
                convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.item_download, parent, false);
            }
            final TransferTask tf = data.get(position);
            //if taskSize isn't initial complete,post to getView
            ((TextView) convertView.findViewById(R.id.title)).setText(tf.getFileName());
            ((ProgressBar) convertView.findViewById(R.id.progressBar)).setProgress((int) (tf.getTaskSize() > 0 ? 100
                    * tf.getCompletedSize() / tf.getTaskSize() : 0));
            if (tf.getState() == LoadState.PREPARE) {
                (convertView.findViewById(R.id.operation)).setEnabled(false);
                ((Button) convertView.findViewById(R.id.operation)).setText("connecting");
            }
            if (tf.getState() == LoadState.PAUSE) {
                ((Button) convertView.findViewById(R.id.operation)).setText("start");
            }
            if (tf.getState() == LoadState.DOWNLOADING) {
                (convertView.findViewById(R.id.operation)).setEnabled(true);
                ((Button) convertView.findViewById(R.id.operation)).setText("pause");
            }
            (convertView.findViewById(R.id.operation)).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (tf.getState() == LoadState.DOWNLOADING)
                        DownloadManager.getInstance().pauseTask(position);
                    else if (tf.getState() == LoadState.PAUSE)
                        DownloadManager.getInstance().restartTask(position);
                }
            });
            return convertView;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //deleteFilesByDirectory(new File("/data/data/"+ this.getPackageName() + "/databases"));
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity)
    {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
