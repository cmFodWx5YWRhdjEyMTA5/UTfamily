package com.ufotech.ufo.utfamily.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ufoUI.statusbar.Eyes;
import com.ufotech.ufo.utfamily.R;
import com.ufotech.ufo.utfamily.api.ApiRetrofit;
import com.ufotech.ufo.utfamily.api.ApiService;
import com.ufotech.ufo.utfamily.model.ApkVersion;
import com.ufotech.ufo.utfamily.update.utils.Tools;
import com.ufotech.ufo.utfamily.update.view.CommonProgressDialog;
import com.ufotech.ufo.utfamily.update.view.MaterialDialog;
import com.ufotech.ufo.utfamily.update.view.OnBtnClickL;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import static com.ufotech.ufo.utfamily.utils.PreUtils.getBoolean;
import static com.ufotech.ufo.utfamily.utils.PreUtils.putBoolean;

/**
 *  -----------------------------------------------------------------------------------------
 *  @author ：   UFO（陳俊融）Github：https://github.com/jyunrong
 *  @date ：   2018/5/15    下午 06:10
 *  @description ：   閃屏頁，不配置 layout，以 Theme 主題配置呈現，加速進入畫面
 * 目前拿來做為檢查 APK 版本更新使用
 *  -----------------------------------------------------------------------------------------
 */
public class SplashActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private int vision;
    private ApkVersion.DataBean dataBean;
    private ApkVersion.DataBean.ContentBean contentBean;
    private String id, api_key, msg, status, apkName, versionCode, serverVersion, whetherForce, updateUrl, upgradeInfo;
    private CommonProgressDialog pBar; // 更新進度條

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 移除狀態欄
        Eyes.translucentStatusBar(this,false);
        // 獲取本版本號，是否更新
        vision = Tools.getVersion(this);
        // 檢查版本更新
        check_version();
    }

    /**
     * 判斷是否第一次進入，
     * 如果是就跳轉到導覽頁面，
     * 如果不是就直接進入。
     *
     * 注：除非清空應用數據或者卸載軟件重新安裝才能再次進入第一次
     */
    private void splash() {
        Intent intent;
        boolean isFirstEnter = getBoolean("isFirstEnter",true);
        if (isFirstEnter) {
            // 第一次進入跳轉
            putBoolean("isFirstEnter", false );
            intent = new Intent(SplashActivity.this, OnboardingActivity.class);
        } else {
            // 非第一次進入跳轉
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }

    /**
     * 使用 Retrofit  向服務器請求最新版本資訊
     */
    private void check_version() {
        // 1. 宣告 APIService  2. 透過 ApiRetrofit 取得連線基底
        ApiService mApiService = ApiRetrofit.getInstance().getApiService();
        // 3. 建立連線的 Call，此處設置 call 為 mAPIService 中的 getApkVersion() 連線
        Call<ApkVersion> call = mApiService.getApkVersion();
        // 4. 執行call
        call.enqueue(new Callback<ApkVersion>() {
            // 請求成功時候的回調
            @Override
            public void onResponse(@NonNull Call<ApkVersion> call, @NonNull Response<ApkVersion> response) {
                // 回傳的資料已轉成 ApkVersion 物件，可直接用 get 方法取得特定欄位
                ApkVersion apkVersion = response.body();
                // 第一層
                dataBean = Objects.requireNonNull(apkVersion).getData();
                msg = apkVersion.getMsg();
                status = String.valueOf(apkVersion.getStatus());
                // 第二層
                id = String.valueOf(dataBean.getId());
                api_key = dataBean.getApi_key();
                contentBean = dataBean.getContent();
                // 第三層
                apkName = contentBean.getApkName();                            // APK 名稱
                versionCode = String.valueOf(contentBean.getVersionCode());     // 更新新的版本號
                serverVersion = String.valueOf(contentBean.getServerVersion()); // Server 版本
                whetherForce = String.valueOf(contentBean.getWhetherForce());   // 強制更新
                updateUrl = contentBean.getUpdateUrl();                        // 安裝包下載地址
                upgradeInfo = contentBean.getUpgradeInfo();                    // 更新内容

                // 取消請求
                call.cancel();

                Log.d(TAG, apkVersion.toString());

                getVersion(vision);
            }

            // 請求失敗時候的回調
            @Override
            public void onFailure(@NonNull Call<ApkVersion> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure = " + t.getMessage());
                // 執行閃屏跳轉
                splash();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 從服務器獲取當前版本號和下載鏈接
     *
     * @param vision 當前版本號
     */
    private void getVersion(final int vision) {

        double newversioncode = Double
                .parseDouble(versionCode);
        int cc = (int) (newversioncode);

        System.out.println(versionCode + "v" + vision + ",,"
                + cc);
        if (cc != vision) {
            if (vision < cc) {
                System.out.println(versionCode + "v"
                        + vision);
                // 版本號不同
                ShowDialog(upgradeInfo, updateUrl);
            }
        } else {
            // 執行閃屏跳轉
            splash();
        }
    }

    // int progressValue = 0;

    /**
     * 升级系统
     *
     * @param content  更新内容。
     * @param url          安装包下载地址。
     */
    private void ShowDialog(String content, final String url) {
        final MaterialDialog dialog = new MaterialDialog(this);//自定义的对话框，可以呀alertdialog
        dialog.content(content).btnText(getString(R.string.CheckAppUpdate_update_no), getString(R.string.CheckAppUpdate_update_yes)).title(getString(R.string.CheckAppUpdate_update_version))
                .titleTextSize(15f).show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnBtnClickL(new OnBtnClickL() {// left btn click listener
            @Override
            public void onBtnClick() {
                dialog.dismiss();
                // 執行閃屏跳轉
                splash();
            }
        }, new OnBtnClickL() {// right btn click listener

            @SuppressLint("InflateParams")
            @Override
            public void onBtnClick() {
                dialog.dismiss();
                // pBar = new ProgressDialog(MainActivity.this,
                // R.style.dialog);
                pBar = new CommonProgressDialog(SplashActivity.this);
                pBar.setCanceledOnTouchOutside(false);
                pBar.setTitle(getString(R.string.CheckAppUpdate_downloading));
                pBar.setCustomTitle(LayoutInflater.from(
                        SplashActivity.this).inflate(
                        R.layout.update_title_dialog, null));
                pBar.setMessage(getString(R.string.CheckAppUpdate_downloading));
                pBar.setIndeterminate(true);
                pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pBar.setCancelable(true);
                // downFile(URLData.DOWNLOAD_URL);
                final DownloadTask downloadTask = new DownloadTask(
                        SplashActivity.this);
                downloadTask.execute(url);
                pBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        downloadTask.cancel(true);
                    }
                });
            }
        });

//        // 原生的按钮：
//        new android.app.AlertDialog.Builder(this)
//                .setTitle("版本更新")
//                .setMessage(content)
//                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        pBar = new CommonProgressDialog(SplashActivity.this);
//                        pBar.setCanceledOnTouchOutside(false);
//                        pBar.setTitle("正在下载");
//                        pBar.setCustomTitle(LayoutInflater.from(
//                                SplashActivity.this).inflate(
//                                R.layout.update_title_dialog, null));
//                        pBar.setMessage("正在下载");
//                        pBar.setIndeterminate(true);
//                        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                        pBar.setCancelable(true);
//                        // downFile(URLData.DOWNLOAD_URL);
//                        final DownloadTask downloadTask = new DownloadTask(
//                                SplashActivity.this);
//                        downloadTask.execute(url);
//                        pBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                            @Override
//                            public void onCancel(DialogInterface dialog) {
//                                downloadTask.cancel(true);
//                            }
//                        });
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();
    }

    /**
     *下載應用
     *
     * @author Administrator
     */
    @SuppressLint("StaticFieldLeak")
    class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        private DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            File file = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error
                // report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP "
                            + connection.getResponseCode() + " "
                            + connection.getResponseMessage();
                }
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    file = new File(Environment.getExternalStorageDirectory(),
                            apkName);

                    if (!file.exists()) {
                        // 判断父文件夹是否存在
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                    }

                } else {
                    Toast.makeText(SplashActivity.this, getString(R.string.CheckAppUpdate_sdcard_not_mounted),
                            Toast.LENGTH_LONG).show();
                }
                input = connection.getInputStream();
                output = new FileOutputStream(file);
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);

                }
            } catch (Exception e) {
                System.out.println(e.toString());
                return e.toString();

            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire(10*60*1000L /*10 minutes*/);
            pBar.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            pBar.setIndeterminate(false);
            pBar.setMax(100);
            pBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            pBar.dismiss();
            if (result != null) {

//                // 申请多个权限。大神的界面
//                AndPermission.with(MainActivity.this)
//                        .requestCode(REQUEST_CODE_PERMISSION_OTHER)
//                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
//                        .rationale(new RationaleListener() {
//                                       @Override
//                                       public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
//                                           // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
//                                           AndPermission.rationaleDialog(MainActivity.this, rationale).show();
//                                       }
//                                   }
//                        )
//                        .send();
                // 申请多个权限。
                AndPermission.with(SplashActivity.this)
                        .requestCode(REQUEST_CODE_PERMISSION_SD)
                        .permission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                        .rationale(rationaleListener
                        )
                        .send();


                Toast.makeText(context, getString(R.string.CheckAppUpdate_sdcard_permissions_not_opened) + result, Toast.LENGTH_LONG).show();
            } else {
                // Toast.makeText(context, "File downloaded",
                // Toast.LENGTH_SHORT)
                // .show();
                update();
            }

        }
    }

    private static final int REQUEST_CODE_PERMISSION_SD = 101;

    private static final int REQUEST_CODE_SETTING = 300;
    private RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            // 这里使用自定义对话框，如果不想自定义，用AndPermission默认对话框：
            // AndPermission.rationaleDialog(Context, Rationale).show();

            // 自定义对话框。
            com.yanzhenjie.alertdialog.AlertDialog.build(SplashActivity.this)
                    .setTitle(R.string.CheckAppUpdate_friendly_reminder)
                    .setMessage(R.string.CheckAppUpdate_sdcard_permission_need)
                    .setPositiveButton(R.string.CheckAppUpdate_set_permission_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            rationale.resume();
                        }
                    })

                    .setNegativeButton(R.string.CheckAppUpdate_set_permission_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            rationale.cancel();
                        }
                    })
                    .show();
        }
    };
    //----------------------------------SD权限----------------------------------//


    @PermissionYes(REQUEST_CODE_PERMISSION_SD)
    private void getMultiYes(List<String> grantedPermissions) {
        Toast.makeText(this, R.string.CheckAppUpdate_sdcard_permission_get_succeed, Toast.LENGTH_SHORT).show();
    }

    @PermissionNo(REQUEST_CODE_PERMISSION_SD)
    private void getMultiNo(List<String> deniedPermissions) {
        Toast.makeText(this, R.string.CheckAppUpdate_sdcard_permission_get_failed, Toast.LENGTH_SHORT).show();

        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
                    .setTitle(R.string.CheckAppUpdate_friendly_reminder)
                    .setMessage(R.string.CheckAppUpdate_sdcard_permission_no)
                    .setPositiveButton(R.string.CheckAppUpdate_set_permission_yes)
                    .setNegativeButton(R.string.CheckAppUpdate_set_permission_no, null)
                    .show();

            // 更多自定dialog，请看上面。
        }
    }

    //----------------------------------权限回调处理----------------------------------//

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /**
         * 轉給AndPermission分析結果。
         *
         * @param object            要接受結果的Activity、Fragment。
         * @param requestCode  請求碼。
         * @param permissions   權限數組，一個或者多個。
         * @param grantResults   請求結果。
         */
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    /**
     * 請求結果。
     *
     * @param requestCode 請求碼。
     * @param resultCode    權限數組，一個或者多個。
     * @param data              請求結果。
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SETTING: {
                Toast.makeText(this, R.string.CheckAppUpdate_setting_back, Toast.LENGTH_LONG).show();
                //设置成功，再次请求更新
                getVersion(Tools.getVersion(SplashActivity.this));
                break;
            }
        }
    }

    /**
     * 安裝應用
     */
    private void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), apkName)),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
