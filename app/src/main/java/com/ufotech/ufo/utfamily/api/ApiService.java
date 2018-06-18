package com.ufotech.ufo.utfamily.api;

import com.ufotech.ufo.utfamily.model.Albums;
import com.ufotech.ufo.utfamily.model.ApkVersion;
import com.ufotech.ufo.utfamily.model.FcmRegister;
import com.ufotech.ufo.utfamily.model.SensorDatas;

import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiService {

    @GET("albums/1")    // 設置一個GET連線，路徑為albums/1
    Call<Albums> getAlbums();    // 取得的回傳資料用Albums物件接收，連線名稱取為getAlbums

    @GET("albums/{id}") // 用{}表示路徑參數，@Path會將參數帶入至該位置
    Call<Albums> getAlbumsById(@Path("id") int id);

    @POST("albums") // 用@Body表示要傳送Body資料
    Call<Albums> postAlbums(@Body Albums albums);

    /**
     * 檢查 APK 版本更新
     * 設置一個 GET 連線，路徑為 updateApk.json
     *
     * @return 取得的回傳資料用 ApkVersion 物件接收，連線名稱取為 getApkVersion
     */
    @GET("UFO/UTfamily/apkVersion.json")
    Call<ApkVersion> getApkVersion();

    /**
     * 發送  RegistrationId 到 server
     * 通過 @FormUrlEncoded、和 @POST 註解，指明訪問的地址
     * 通過 @Field 來指定 key，後面跟上 value
     *
     * @param registrationIds 裝置的 token
     * @return status + message
     */
    @FormUrlEncoded
    @POST("UFO/UTfamily/firebase_register_token.php")
    Call<FcmRegister> postFcmRegister(@Field("registrationIds") String registrationIds);

    /**
     * ResponseBody 接收完整 Html 格式文件，不須特別創建 model
     *
     * @param url 網站 URL
     * @return 網頁原始碼
     */
    @GET
    Call<ResponseBody> getHtmlSourceCode(@Url HttpUrl url);

    @GET("Arduino")
    Call<SensorDatas> getSensorDatas();
}
