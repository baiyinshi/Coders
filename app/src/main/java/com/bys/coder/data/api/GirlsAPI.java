package com.bys.coder.data.api;

import com.bys.coder.data.bean.GirlItemBean;
import com.bys.coder.data.bean.HttpResponse;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * 作者：Bys on 2016/11/14 17:05
 * 邮箱：yinshi.bai@shwilling.com
 */
public interface GirlsAPI {

    String HOST = "http://gank.io/api/";

    /**
     * 福利列表
     */
    @GET("data/福利/{num}/{page}")
    Observable<HttpResponse<List<GirlItemBean>>> getGirlList(@Path("num") int num, @Path("page") int page);
}
