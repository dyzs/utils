package com.dyzs.app.retrofitsample;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author dyzs, created on 2018/2/28.
 *
 * Retrofit 官方案例
 *
 * 这个是文档上提供的例子，我们可用发现，他同样的是GET请求，只不过他的返回值是一个List，类型是repo，
 * repo就是他的实体类，传了一个path是一个参数，user的参数，这样也同样的可以和他的baseUrl拼接了他的
 * baseUrl是什么呢，我们等下再说
 */

public interface GithubService {

    @GET("users/{user}/repos")
    Call<List<Repo>> listRepos(@Path("user") String user);

    class Repo {

    }
}
