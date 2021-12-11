package com.uyu.device.devicetraining.data.db;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.uyu.device.devicetraining.data.entity.content.TrainingContent;

/**
 * 数据库定义类
 * Created by windern on 2016/5/25.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    //数据库名称
    public static final String NAME = "AppDatabase";
    //数据库版本号
    public static final int VERSION = 3;

    @Migration(version = 3, database = AppDatabase.class)
    public static class Migration3 extends AlterTableMigration<TrainingContent> {

        public Migration3(Class<TrainingContent> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "seq_show");
            addColumn(SQLiteType.INTEGER, "type");
        }
    }
}
