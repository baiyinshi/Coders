package com.bys.coder.app;

import java.io.File;

/**
 * 作者：Bys on 2016/11/9 15:26
 * 邮箱：yinshi.bai@shwilling.com
 */
public class Constants {

    public static final String PATH_DATA = App.getAppContext().getCacheDir().getAbsolutePath() + File.separator + "data";
    public static final String PATH_CACHE = PATH_DATA + File.separator + "NetCache";
}
