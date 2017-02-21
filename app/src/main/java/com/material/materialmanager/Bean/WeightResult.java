package com.material.materialmanager.Bean;

/**
 * Created by Doing on 2017/2/21 0021.
 */
public class WeightResult {

    private boolean success;
    private String msg;

    public WeightResult(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "WeightResult{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                '}';
    }
}
