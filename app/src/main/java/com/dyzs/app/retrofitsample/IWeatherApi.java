package com.dyzs.app.retrofitsample;

import com.dyzs.app.net.IBaseUrl;
import com.dyzs.app.entity.WeatherDataBiz;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * @author dyzs, created on 2018/2/28.
 * http://op.juhe.cn/onebox/weather/query?cityname=深圳&key=您申请的KEY
 *
 * 天气的请求参数说明：
String cityName;//	要查询的城市，如：温州、上海、北京，需要utf8 urlencode
String key;// 应用APPKEY(应用详细页查询)
String dtype;// 返回数据的格式,xml或json，默认json
 */

public interface IWeatherApi {
    /**
     *
     * @param key 接口查询参数
     * @return
     */
    @GET(IBaseUrl.JUHE_WEATHER_DATA)
    Call<WeatherDataBiz> getWeather(@Query("key") String key);

    /* GET 参数拼接 QueryMap 方式请求使用 */
    @GET("onebox/weather/query?")
    Call<WeatherDataBiz> getWeather(@QueryMap Map<String, String> params);
}
