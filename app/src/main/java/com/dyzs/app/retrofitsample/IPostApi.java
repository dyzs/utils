package com.dyzs.app.retrofitsample;


import com.dyzs.app.entity.UserBean;
import com.dyzs.app.entity.UserBeanResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author dyzs, created on 2018/2/28.
 * retrofit post 方式请求
 */

public interface IPostApi {

    @POST("user/new")
    Call<UserBeanResult> postUser(@Body UserBean userBean);


    /* 这里的两个接口，一个是登录，传参用户名和密码，还有一个是用id去查找用户信息的，那我们继续 */
    /* Retrofit + RxJava */
    /* form 表单提交 */
    @POST("user/login")
    Call<UserBeanResult> login(@Field("username") String user, @Field("password") String password);

    @GET("user/info")
    Call<UserBeanResult> getUser(@Query("id") String id);

    /* RxJava的写法，我们这里需要重新定义两个接口了 */
    @POST("user/login")
    rx.Observable<UserBeanResult> loginForRX(@Body UserBean user);

    @GET("user/info")
    rx.Observable<UserBeanResult> getUserForRX(@Query("id") String id);



}

