package com.ufotech.ufo.utfamily.app;

import com.socks.library.KLog;
import com.ufotech.ufo.utfamily.BuildConfig;
import com.ufotech.ufo.utfamily.app.base.BaseApp;

import org.litepal.LitePalApplication;

/**
 *  -----------------------------------------------------------------------------------------
 *  @author ：   UFO（陳俊融）Github：https://github.com/jyunrong
 *  @date ：   2018/5/15    下午 07:36
 *  @description ：   Application 類
 *  -----------------------------------------------------------------------------------------
 */
public class MyApp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();

        //**************************************相關第三方SDK的初始化等操作*************************************************
        KLog.init(BuildConfig.DEBUG); // 初始化 KLog
        LitePalApplication.initialize(getApplicationContext()); // 初始化 litePal
    }
}
