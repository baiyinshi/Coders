package com.bys.coder.common.base;

/******************************************
 * 类名称：BaseView
 * 类描述：
 *
 * @author: Bys
 * @time: 2016/9/12 10:18
 ******************************************/
public interface BaseView {
    /*******
     * 内嵌加载
     *******/
    void showLoading(String title);

    void stopLoading();

    void showErrorTip(String msg);
}
