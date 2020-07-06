package com.dyzs.db.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dyzs.db.room.entities.LoginInfo;

import java.util.List;


@Dao
public interface LoginInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(LoginInfo info);

    @Query("SELECT * FROM LoginInfo WHERE loginId == :id")
    LoginInfo queryLoginInfo(String id);

    @Query("SELECT * FROM LoginInfo")
    List<LoginInfo> queryLoginInfoList();

    @Delete(entity = LoginInfo.class)
    int deleteLoginInfo(LoginInfo... users);

}
