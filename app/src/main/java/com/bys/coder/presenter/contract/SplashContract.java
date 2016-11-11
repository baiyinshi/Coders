package com.bys.coder.presenter.contract;

import com.bys.coder.common.base.BasePresenter;
import com.bys.coder.common.base.BaseView;

import java.util.List;

/**
 * 作者：Bys on 2016/11/10 17:38
 * 邮箱：yinshi.bai@shwilling.com
 */
public interface SplashContract {

    interface View extends BaseView {
        boolean isActive();

        void showContent(List<String> list);

        void jumpToMain();
    }

    interface Presenter extends BasePresenter {
        void getSplashData();
    }
}
