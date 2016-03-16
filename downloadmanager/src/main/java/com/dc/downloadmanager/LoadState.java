package com.dc.downloadmanager;

/**
 * Created by pxh on 2016/2/15.
 * The states of Download/Upload
 */
public class LoadState
{
    public static final int PREPARE=1;
    public static final int START=2;
    public static final int DOWNLOADING=3;
    public static final int PAUSE=4;
    public static final int NET_ERROR=5;
    public static final int COMPLETED=6;
    public static final int CANCEL=7;
}
