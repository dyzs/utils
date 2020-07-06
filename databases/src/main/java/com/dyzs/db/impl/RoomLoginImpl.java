package com.dyzs.db.impl;

import android.content.Context;

import com.dyzs.db.interfaces.RoomLoginInterface;
import com.dyzs.db.room.RoomDb;
import com.dyzs.db.room.entities.LoginInfo;

import java.util.List;

public class RoomLoginImpl implements RoomLoginInterface {
    @Override
    public LoginInfo queryLoginInfo(Context ctx, String id) {
        return RoomDb.getInstance(ctx).loginInfoDao().queryLoginInfo(id);
    }

    @Override
    public void insertLoginInfo(Context ctx, LoginInfo loginInfo) {
        RoomDb.getInstance(ctx).loginInfoDao().insertOrReplace(loginInfo);
    }

    @Override
    public List<LoginInfo> queryLoginInfoList(Context ctx) {
        return RoomDb.getInstance(ctx).loginInfoDao().queryLoginInfoList();
    }

    @Override
    public void deleteLoginInfo(Context ctx, List<LoginInfo> list) {
        if (list == null || list.size() == 0)return;
        LoginInfo[] arrays = (LoginInfo[]) list.toArray();
        RoomDb.getInstance(ctx).loginInfoDao().deleteLoginInfo(arrays);
    }
}
