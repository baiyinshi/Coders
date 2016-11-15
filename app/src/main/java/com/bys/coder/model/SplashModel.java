package com.bys.coder.model;

import com.bys.coder.common.baserx.RxSchedulers;
import com.bys.coder.data.api.GirlsAPI;
import com.bys.coder.data.bean.GirlItemBean;
import com.bys.coder.data.bean.HttpResponse;
import com.bys.coder.data.net.NetUtils;
import com.bys.coder.presenter.contract.SplashContract;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 作者：Bys on 2016/11/14 14:12
 * 邮箱：yinshi.bai@shwilling.com
 */
public class SplashModel implements SplashContract.Model {
    @Override
    public Observable<List<String>> getGirlList(int num, int page) {
        return NetUtils.createApi(GirlsAPI.class, GirlsAPI.HOST)
                .getGirlList(num, page)
                .map(new Func1<HttpResponse<List<GirlItemBean>>, List<String>>() {
                    @Override
                    public List<String> call(HttpResponse<List<GirlItemBean>> response) {
                        List<GirlItemBean> girlsList = response.getResults();
                        List<String> strList = new ArrayList<String>();
                        for (GirlItemBean item : girlsList) {
                            strList.add(item.getUrl());
                        }
                        return strList;
                    }
                })
                .compose(RxSchedulers.<List<String>>io_main());
    }
}
