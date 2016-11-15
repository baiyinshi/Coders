package com.bys.coder.presenter;


import com.bys.coder.common.baserx.RxSubscriber;
import com.bys.coder.presenter.contract.SplashContract;

import java.util.List;

/**
 * 作者：Bys on 2016/11/10 17:36
 * 邮箱：yinshi.bai@shwilling.com
 */
public class SplashPresenter extends SplashContract.Presenter {

    @Override
    public void getSplashData(int unm, int page) {
        mRxManage.add(mModel.getGirlList(unm, page).subscribe(new RxSubscriber<List<String>>(mContext, false) {
                    @Override
                    protected void _onNext(List<String> strings) {
                        mView.showContent(strings);
                    }

                    @Override
                    protected void _onError(String message) {
                    }
                })
        );
    }
}
