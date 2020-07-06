package com.dyzs.db.interfaces;

import android.content.Context;

import com.dyzs.db.room.entities.LoginInfo;

import java.util.List;

public interface RoomLoginInterface {
    LoginInfo queryLoginInfo(Context ctx, String id);

    void insertLoginInfo(Context ctx, LoginInfo loginInfo);

    List<LoginInfo> queryLoginInfoList(Context ctx);

    void deleteLoginInfo(Context ctx, List<LoginInfo> list);
}
