package com.ufotech.ufo.utfamily.api;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ufotech.ufo.utfamily.app.ApiConstant;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRetrofit {

    private final String TAG = this.getClass().getSimpleName();

    // 以 Singleton 模式建立
    private static ApiRetrofit mApiRetrofit = new ApiRetrofit();
    private ApiService mApiService;

    private ApiRetrofit() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        // 建立 OkHttpClient
        OkHttpClient mClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)   // 設置連線 Timeout
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .authenticator(new Authenticator() {
                    @Nullable
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        return null;
                    }
                })
                .cookieJar(new CookieJar() {    // 這裡可以做 cookie 傳遞，保存等操作
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        // 可以做保存 cookies 操作
                        Log.d(TAG, "cookies url: " + url.toString());
                        for (Cookie cookie : cookies) {
                            Log.d(TAG, "cookies: " + cookie.toString());
                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        // 加载新的 cookies
                        ArrayList<Cookie> cookies = new ArrayList<>();
                        Cookie cookie = new Cookie.Builder()
                                .hostOnlyDomain(url.host())
                                .name("SESSION").value("zyao89")
                                .build();
                        cookies.add(cookie);
                        return cookies;
                    }
                })
                .build();

        // 創建 Retrofit 對象
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiConstant.BASE_SERVER_URL)  // 設置 網絡請求 Url
                .addConverterFactory(GsonConverterFactory.create(gson))  // 用 Gson 作為資料解析的 Converter
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// 支持 RxJava
                .client(mClient)  // 將 okHttpClient 加入連線基底
                .build();

        // 創建網絡請求接口的實例
        mApiService = mRetrofit.create(ApiService.class);
    }

    public static ApiRetrofit getInstance() {
        if (mApiRetrofit == null) {
            synchronized (Object.class) {
                if (mApiRetrofit == null) {
                    mApiRetrofit = new ApiRetrofit();
                }
            }
        }
        return mApiRetrofit;
    }

    public ApiService getApiService() {
        return mApiService;
    }
}
