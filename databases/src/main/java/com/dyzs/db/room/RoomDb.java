package com.dyzs.db.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.commonsware.cwac.saferoom.SafeHelperFactory;
import com.dyzs.db.room.dao.LoginInfoDao;
import com.dyzs.db.room.entities.LoginInfo;


/**
 * Created by dyzs on 2020/4/21.
 * 目前仅有一个表
 *
 * https://blog.csdn.net/u013491829/article/details/104025952
 * https://www.cnblogs.com/best-hym/p/12259615.html
 */
@androidx.room.Database(entities = {LoginInfo.class}, version = 1, exportSchema = false)
public abstract class RoomDb extends RoomDatabase {

    private static RoomDb INSTANCE;

    public abstract LoginInfoDao loginInfoDao();

    private static SupportSQLiteOpenHelper.Factory SAFE_HELPER_FACTORY = new SafeHelperFactory("123456".getBytes());

    public static RoomDb getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                            .databaseBuilder(context, RoomDb.class, "db_room.db")
                            /*//.fallbackToDestructiveMigration()   //这个方法也可以迁移数据库，但会将数据摧毁导致数据的丢失*/
                            .openHelperFactory(SAFE_HELPER_FACTORY)
                            .addMigrations(MIGRATION_1_2,MIGRATION_2_3)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE 'CiCertMapping' ('identityId' TEXT not null, 'cert' TEXT not null, PRIMARY KEY('cert'))");
        }

    };
    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE IdentityInfo  ADD COLUMN identity TEXT");
        }
    };


}
