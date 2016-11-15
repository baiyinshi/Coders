package com.bys.coder.common.basebean;

import java.io.Serializable;

/**
 * des:封装服务器返回数据
 * Created by xsf
 * on 2016.09.9:47
 */
public class BaseRespose<T> implements Serializable {


    /**
     * errcode : 0
     * errmsg : Success.
     * data : {}
     */

    private int errcode;
    private String errmsg;
    private T data;

    public boolean success() {
        return "0".equals(errcode);
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseRespose{" +
                "code='" + errcode + '\'' +
                ", msg='" + errmsg + '\'' +
                ", data=" + data +
                '}';
    }
}
