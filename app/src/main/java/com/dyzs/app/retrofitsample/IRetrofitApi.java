package com.dyzs.app.retrofitsample;

import com.dyzs.app.net.IBaseUrl;
import com.dyzs.app.entity.RetrofitSampleBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author dyzs, created on 2018/2/28.
 *
 * base on url {http://gank.io/api/data/Android/10/1}
 * base url {http://gank.io/}
 * api {api/data/Android/10/1}
 */

public interface IRetrofitApi {

    @GET("api/data/Android/10/1")
    Call<ResponseBody> getAndroidInfo();

    /* 重新定义新的接口 */
    @GET(IBaseUrl.GANK_URL_API_1)
    Call<RetrofitSampleBean> getSampleBeanInfo();

    /* GET Path 方式请求使用 page 参数分页查询 */
    @GET(IBaseUrl.GANK_URL_API_USE_PAGE)
    Call<RetrofitSampleBean> getSampleBeanInfoWithPage(@Path("page") int page);

}
