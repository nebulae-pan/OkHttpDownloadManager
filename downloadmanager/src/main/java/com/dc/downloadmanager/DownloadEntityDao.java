package com.dc.downloadmanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DOWNLOAD_ENTITY".
*/
public class DownloadEntityDao extends AbstractDao<DownloadEntity, String> {

    public static final String TABLENAME = "DOWNLOAD_ENTITY";

    /**
    */
    public static class Properties {
        public final static Property Url = new Property(0, String.class, "url", true, "URL");
        public final static Property TaskSize = new Property(1, Long.class, "taskSize", false, "TASK_SIZE");
        public final static Property CompletedSize = new Property(2, Long.class, "completedSize", false, "COMPLETED_SIZE");
        public final static Property SaveDirPath = new Property(3, String.class, "saveDirPath", false, "SAVE_DIR_PATH");
        public final static Property FileName = new Property(4, String.class, "fileName", false, "FILE_NAME");
        public final static Property ThreadComplete = new Property(5, String.class, "threadComplete", false, "THREAD_COMPLETE");
        public final static Property SubThreadNum = new Property(6, Integer.class, "subThreadNum", false, "SUB_THREAD_NUM");
    };


    public DownloadEntityDao(DaoConfig config) {
        super(config);
    }
    
    public DownloadEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DOWNLOAD_ENTITY\" (" + //
                "\"URL\" TEXT PRIMARY KEY NOT NULL ," + // 0: url
                "\"TASK_SIZE\" INTEGER," + // 1: taskSize
                "\"COMPLETED_SIZE\" INTEGER," + // 2: completedSize
                "\"SAVE_DIR_PATH\" TEXT," + // 3: saveDirPath
                "\"FILE_NAME\" TEXT," + // 4: fileName
                "\"THREAD_COMPLETE\" TEXT," + // 5: threadComplete
                "\"SUB_THREAD_NUM\" INTEGER);"); // 6: subThreadNum
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DOWNLOAD_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected void bindValues(SQLiteStatement stmt, DownloadEntity entity) {
        stmt.clearBindings();
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(1, url);
        }
 
        Long taskSize = entity.getTaskSize();
        if (taskSize != null) {
            stmt.bindLong(2, taskSize);
        }
 
        Long completedSize = entity.getCompletedSize();
        if (completedSize != null) {
            stmt.bindLong(3, completedSize);
        }
 
        String saveDirPath = entity.getSaveDirPath();
        if (saveDirPath != null) {
            stmt.bindString(4, saveDirPath);
        }
 
        String fileName = entity.getFileName();
        if (fileName != null) {
            stmt.bindString(5, fileName);
        }
 
        String threadComplete = entity.getThreadComplete();
        if (threadComplete != null) {
            stmt.bindString(6, threadComplete);
        }
 
        Integer subThreadNum = entity.getSubThreadNum();
        if (subThreadNum != null) {
            stmt.bindLong(7, subThreadNum);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public DownloadEntity readEntity(Cursor cursor, int offset) {
        DownloadEntity entity = new DownloadEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // url
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // taskSize
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // completedSize
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // saveDirPath
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // fileName
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // threadComplete
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6) // subThreadNum
        );
        return entity;
    }

    @Override
    public void readEntity(Cursor cursor, DownloadEntity entity, int offset) {
        entity.setUrl(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setTaskSize(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setCompletedSize(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setSaveDirPath(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setFileName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setThreadComplete(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setSubThreadNum(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
     }

    @Override
    protected String updateKeyAfterInsert(DownloadEntity entity, long rowId) {
        return entity.getUrl();
    }
    
    @Override
    public String getKey(DownloadEntity entity) {
        if(entity != null) {
            return entity.getUrl();
        } else {
            return null;
        }
    }

    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
