package com.bys.coder.ui.activity;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bys.coder.R;
import com.bys.coder.common.base.BaseActivity;
import com.bys.coder.common.utils.ImageLoaderUtils;
import com.bys.coder.common.utils.StringUtils;
import com.bys.coder.model.SplashModel;
import com.bys.coder.presenter.SplashPresenter;
import com.bys.coder.presenter.contract.SplashContract;

import java.util.List;

import butterknife.BindView;

public class SplashActivity extends BaseActivity<SplashPresenter, SplashModel> implements SplashContract.View {

    //设置界面跳转时间为5秒
    private final static int time = 3;
    private final static Handler mHandler = new Handler();

    @BindView(R.id.tv_welcome_time)
    TextView tvWelcomeTime;
    @BindView(R.id.iv_welcome_bg)
    ImageView ivWelcomeBg;
    @BindView(R.id.tv_welcome_title)
    TextView tvWelcomeTitle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }


    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tvWelcomeTime.setText("剩余" + time + "秒");
        tvWelcomeTime.setTextSize(10);
        tvWelcomeTime.setTextColor(Color.BLACK);
        tvWelcomeTime.setPadding(8, 3, 6, 2);
        new TimeCountDownTask(tvWelcomeTime, time).execute();
        tvWelcomeTitle.setText("正在启动,请稍后...");
        tvWelcomeTitle.setGravity(Gravity.CENTER);
        tvWelcomeTitle.setTextColor(Color.WHITE);
        mPresenter.getSplashData(10,1);
    }
    @Override
    public void showContent(List<String> list) {
        if (list != null) {
            int page = StringUtils.getRandomNumber(0, list.size() - 1);
            ImageLoaderUtils.load(mContext, list.get(page), ivWelcomeBg);
            ivWelcomeBg.animate().scaleX(1.12f).scaleY(1.12f).setDuration(2000).setStartDelay(100).start();
        }
    }

    @Override
    public void showLoading(String title) {
    }

    @Override
    public void stopLoading() {
    }

    @Override
    public void showErrorTip(String msg) {
    }

    private class TimeCountDownTask extends AsyncTask<Void, Void, Boolean> {

        TextView timeView;
        int limit_time = 0;

        TimeCountDownTask(TextView timeView, int time) {
            this.timeView = timeView;
            this.limit_time = time;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            while (limit_time > 0) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        timeView.setText("剩余" + limit_time + "秒");
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                limit_time--;
            }
            return null;
        }
    }

}