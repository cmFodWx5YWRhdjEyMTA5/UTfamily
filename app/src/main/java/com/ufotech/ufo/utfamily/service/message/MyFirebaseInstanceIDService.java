package com.ufotech.ufo.utfamily.service.message;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ufotech.ufo.utfamily.utils.NetWorkUtils;

import static com.ufotech.ufo.utfamily.app.ApiConstant.BASE_SERVER_URL;
import static com.ufotech.ufo.utfamily.constants.Constant.REGISTER_TOKEN;

/**
 * -----------------------------------------------------------------
 * @author ：UFO（陳俊融）Github：https://github.com/jyunrong
 * @date ：2018/5/15 上午 11:48
 * @description ：處裡裝置註冊Token
 * -----------------------------------------------------------------
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

//        替代方案 之 普通 httpUrlConnection 方法 : (暫時先交給 Apache 處理)
        String registrationIds = "registrationIds=" + token;
        NetWorkUtils.sendPost(BASE_SERVER_URL + REGISTER_TOKEN, registrationIds);

//        ApiService mApiService = ApiRetrofit.getInstance().getApiService();
//        Call<FcmRegister> call = mApiService.postFcmRegister(token);
//        call.enqueue(new Callback<FcmRegister>() {
//            @Override
//            public void onResponse(Call<FcmRegister> call, Response<FcmRegister> response) {
//                FcmRegister fcmRegister = response.body();
//                Log.d(TAG, fcmRegister.toString());
//                call.cancel();
//            }
//
//            @Override
//            public void onFailure(Call<FcmRegister> call, Throwable t) {
//                Log.d(TAG, "onFailure = " + t.getMessage());
//            }
//        });
    }
}