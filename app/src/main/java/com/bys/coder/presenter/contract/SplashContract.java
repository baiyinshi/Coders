package com.bys.coder.presenter.contract;

import com.bys.coder.common.base.BaseModel;
import com.bys.coder.common.base.BasePresenter;
import com.bys.coder.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * 作者：Bys on 2016/11/10 17:38
 * 邮箱：yinshi.bai@shwilling.com
 */
public interface SplashContract {


    interface View extends BaseView {
        void showContent(List<String> list);
    }

    interface Model extends BaseModel {
        Observable<List<String>> getGirlList(int num, int page);
    }

    abstract class Presenter extends BasePresenter<View, Model> {

        public abstract void getSplashData(int num, int page);
    }
}
