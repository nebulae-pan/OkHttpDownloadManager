package com.dc.okhttpdownloadmanager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dc.downloadmanager.DownloadManager;
import com.dc.downloadmanager.TransferTask;

import java.lang.ref.WeakReference;
import java.util.LinkedList;


public class MainActivity extends AppCompatActivity implements TaskConfirmDialog.InputCompletedListener
{
    private ListView listView;
    DownloadManager downloadManager;
    Handler handler;
    protected Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.listView);
        downloadManager = DownloadManager.getInstance(this,handler);
        handler = new InnerHandler(this);
        setListViewAdapter();
    }

    void setListViewAdapter()
    {
        adapter = new Adapter(this, downloadManager.getTaskList());
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_add_task:
                TaskConfirmDialog dialogFragment=new TaskConfirmDialog();
                android.app.FragmentManager manager=getFragmentManager();
                dialogFragment.show(manager,"");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void inputCompleted(String url, String fileName)
    {
        downloadManager.addTask(url);
        adapter.notifyDataSetChanged();
    }

    static class InnerHandler extends Handler
    {
        WeakReference<MainActivity> reference;
        MainActivity activity;

        public InnerHandler(MainActivity activity)
        {
            this.reference = new WeakReference<>(activity);
            activity = reference.get();
        }

        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what) {
                case 1:
                    activity.adapter.notifyDataSetChanged();
            }
            super.handleMessage(msg);
        }
    }

    /**
     * just sample
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
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null) {
                convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.item_download, parent, false);
            }
            TransferTask tf = data.get(position);
            ((TextView) convertView.findViewById(R.id.title)).setText(tf.getFileName());
            ((ProgressBar) convertView.findViewById(R.id.progressBar)).setProgress((int) (tf.getCompletedSize() / tf
                    .getTaskSize()));
            return convertView;
        }
    }
}
