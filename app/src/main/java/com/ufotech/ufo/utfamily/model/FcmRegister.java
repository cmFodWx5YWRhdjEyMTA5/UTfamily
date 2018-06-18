package com.ufotech.ufo.utfamily.model;

import com.google.gson.Gson;

/**
 *  -----------------------------------------------------------------------------------------
 *  @author ：   UFO（陳俊融）Github：https://github.com/jyunrong
 *  @date ：   2018/5/26    上午 05:28
 *  @description ：   發送 Refreshed token 到 Server
 *  -----------------------------------------------------------------------------------------
 */
public class FcmRegister {

    /**
     * {"message":"Update success !!","status":true}
     */

    private boolean status;
    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
