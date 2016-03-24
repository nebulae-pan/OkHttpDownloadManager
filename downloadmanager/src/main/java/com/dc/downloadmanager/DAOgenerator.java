package com.dc.downloadmanager;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DAOgenerator
{
    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(2, "com.dc.downloadmanager");
        //addArticle(schema);
        addDownloadEntity(schema);
        //DAO生成路径
        new DaoGenerator().generateAll(schema, "../OkhttpDownloadManager/downloadmanager/src/main/java");

    }

    private static void addDownloadEntity(Schema schema)
    {
        Entity entity = schema.addEntity("DownloadEntity");
        entity.addStringProperty("url").primaryKey();
        entity.addLongProperty("taskSize");
        entity.addLongProperty("completedSize");
        entity.addStringProperty("saveDirPath");
        entity.addStringProperty("fileName");
        entity.addStringProperty("threadComplete");
    }
}
