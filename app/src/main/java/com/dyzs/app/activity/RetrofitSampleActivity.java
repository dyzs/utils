package com.dyzs.app.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.dyzs.app.R;
import com.dyzs.app.base.BaseActivity;
import com.dyzs.app.base.IBaseUrl;
import com.dyzs.app.entity.RetrofitSampleBean;
import com.dyzs.app.entity.WeatherDataBiz;
import com.dyzs.app.retrofitsample.IRetrofitApi;
import com.dyzs.app.retrofitsample.IWeatherApi;
import com.dyzs.common.utils.LogUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author maidou, created on 2018/2/28.
 */

public class RetrofitSampleActivity extends BaseActivity {
    private static final String TAG = RetrofitSampleActivity.class.getSimpleName();

    @BindView(R.id.retrofit_request)
    public TextView retrofit_request;
    @BindView(R.id.retrofit_sample)
    public TextView retrofit_sample;

    @BindView(R.id.tv_result)
    public TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.act_retrofit_sample);

        // ButterKnife.bind(this);

        // initView();
    }

    @Override
    public int initContentView() {
        return R.layout.act_retrofit_sample;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.retrofit_request)
    public void clickRetrofitRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IBaseUrl.BASE_URL_GANK)
                .build();

        IRetrofitApi api = retrofit.create(IRetrofitApi.class);

        Call<ResponseBody> call = api.getAndroidInfo();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                /* main thread */
                try {
                    String result = response.body().string();
                    LogUtils.i(TAG, "thread name:" + Thread.currentThread().getName());

                    LogUtils.i(TAG, "result string:" + result);
                    tv_result.setText(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    /**
     * error: Could not locate ResponseBody converter for class com.dyzs.app.entity.RetrofitSampleBean
     * notice: ResponseBody 不能直接转换为实体类，需要配置一个 Gson 转换器，这里的 Gson 并非 google.gson
     * resource: compile 'com.squareup.retrofit2:converter-gson:2.1.0'
     *
     */
    @OnClick(R.id.retrofit_sample)
    public void clickRetrofitSample() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IBaseUrl.BASE_URL_GANK)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IRetrofitApi api = retrofit.create(IRetrofitApi.class);

        Call<RetrofitSampleBean> call = api.getSampleBeanInfo();
        call.enqueue(new Callback<RetrofitSampleBean>() {
            @Override
            public void onResponse(Call<RetrofitSampleBean> call, Response<RetrofitSampleBean> response) {
                try {
                    RetrofitSampleBean.ResultBean resultBean = response.body().getResults().get(0);
                    tv_result.setText(
                            "_id:" + resultBean.getId() + "\n"
                            + "createdAt：" + resultBean.getCreatedAt() + "\n"
                            + "desc：" + resultBean.getDesc() + "\n"
                            + "images:" + resultBean.getImages() + "\n"
                            + "publishedAt:" + resultBean.getPublishedAt() + "\n"
                            + "source" + resultBean.getSource() + "\n"
                            + "type:" + resultBean.getType() + "\n"
                            + "url: " + resultBean.getUrl() + "\n"
                            + "who:" + resultBean.getWho());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RetrofitSampleBean> call, Throwable t) {

            }
        });
    }


    /* Get 参数请求 */
    private int i = 0;
    @OnClick(R.id.retrofit_sample_with_page)
    public void clickRetrofitSampleWithPage() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IBaseUrl.BASE_URL_GANK)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IRetrofitApi api = retrofit.create(IRetrofitApi.class);

        if (i > 10)i = 1;
        i++;

        Call<RetrofitSampleBean> call = api.getSampleBeanInfoWithPage(i);

        call.enqueue(new Callback<RetrofitSampleBean>() {
            @Override
            public void onResponse(Call<RetrofitSampleBean> call, Response<RetrofitSampleBean> response) {
                tv_result.setText(response.body().getResults().get(0).getDesc());
            }

            @Override
            public void onFailure(Call<RetrofitSampleBean> call, Throwable t) {

            }
        });
    }


    /**
     * retrofit sample with {Get的动态参数} 学习
     */
    @OnClick(R.id.retrofit_request_city_weather)
    public void click2getWeatherData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IBaseUrl.BASE_URL_JUHE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IWeatherApi iWeatherApi = retrofit.create(IWeatherApi.class);
        Call<WeatherDataBiz> call = iWeatherApi.getWeather("4ea58de8a7573377cec0046f5e2469d5");
        call.enqueue(new Callback<WeatherDataBiz>() {
            @Override
            public void onResponse(Call<WeatherDataBiz> call, Response<WeatherDataBiz> response) {
                try {
                    String error_code = response.body().error_code + "";
                    String result = response.body().result;
                    String reason = response.body().reason;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<WeatherDataBiz> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.weather_query_map_method)
    public void click2GetWeatherDataUseMultiParams() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IBaseUrl.BASE_URL_JUHE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IWeatherApi iWeatherApi = retrofit.create(IWeatherApi.class);
        Map<String, String> mapping = new HashMap<>();
        mapping.put("cityname", "北京");
        mapping.put("key", "4ea58de8a7573377cec0046f5e2469d5");

        Call<WeatherDataBiz> call = iWeatherApi.getWeather(mapping);

        call.enqueue(new Callback<WeatherDataBiz>() {
            @Override
            public void onResponse(Call<WeatherDataBiz> call, Response<WeatherDataBiz> response) {
                try {
                    String error_code = response.body().error_code + "";
                    String result = response.body().result;
                    String reason = response.body().reason;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<WeatherDataBiz> call, Throwable t) {

            }
        });

    }
}
