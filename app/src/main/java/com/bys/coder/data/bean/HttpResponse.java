package com.bys.coder.data.bean;

/**
 * 作者：Bys on 2016/11/14 17:07
 * 邮箱：yinshi.bai@shwilling.com
 */
public class HttpResponse<T> {

    private boolean error;
    private T results;

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
