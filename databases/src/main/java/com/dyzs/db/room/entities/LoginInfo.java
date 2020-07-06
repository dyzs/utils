package com.dyzs.db.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "LoginInfo")
public class LoginInfo {
    @NotNull
    @PrimaryKey
    public String loginId;

    @NotNull
    public long recordTime = -1L;

    @NotNull
    public long lastOperateTime = -1L;

    public boolean isMain = false;

}
