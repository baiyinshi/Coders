package com.bys.coder.app;


import com.antfortune.freeline.FreelineCore;
import com.bys.coder.BuildConfig;
import com.bys.coder.common.baseapp.BaseApplication;
import com.bys.coder.data.db.RealmHelper;
import com.bys.coder.data.net.NetConfig;
import com.bys.coder.data.net.NetConfigBuilder;
import com.bys.coder.data.net.NetUtils;
import com.bys.coder.utils.LogUtils;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.rx.RealmObservableFactory;

/**
 * 作者：Bys on 2016/11/9 14:06
 * 邮箱：yinshi.bai@shwilling.com
 */
public class App extends BaseApplication {


    public static final String TAG = "Bys";
    public final static boolean DEBUG = BuildConfig.DEBUG;

    @Override
    protected boolean isDebugLog() {
        return DEBUG;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化logger
        LogUtils.logInit(BuildConfig.DEBUG);
        //初始化蚂蚁Freeline
        FreelineCore.init(this);
        initNetConfig();
//        initRealm();
    }


    /**
     * 初始化网络访问
     */
    private void initNetConfig() {
        NetConfig netConfig = new NetConfigBuilder()
                .context(this)
                .log(DEBUG)
                .logTag(TAG)
                .responseCacheDir(new File(Constants.PATH_CACHE))
                .responseCacheSize(30 * 1024 * 1024)
                .printResponseBody(DEBUG)
                .createNetConfig();
        NetUtils.setNetConfig(netConfig);
    }

    /**
     * 初始化realm
     */
    private void initRealm() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(RealmHelper.DB_NAME)
                .schemaVersion(1)
                .rxFactory(new RealmObservableFactory())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
