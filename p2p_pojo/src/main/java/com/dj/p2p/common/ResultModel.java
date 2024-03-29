package com.dj.p2p.common;

import java.io.Serializable;

public class ResultModel<T> implements Serializable {

	public ResultModel(){}
	
    private Integer code;
    private String msg;
    private T data;

    public ResultModel success(T data,String msg) {
        this.code = 200;
        this.msg = msg;
        this.data = data;
        return this;
    }

    public ResultModel error(T data,String msg) {
        this.code = -1;
        this.msg = msg;
        this.data = data;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
