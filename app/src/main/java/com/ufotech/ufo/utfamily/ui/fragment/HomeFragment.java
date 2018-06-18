package com.ufotech.ufo.utfamily.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ufotech.ufo.utfamily.R;
import com.ufotech.ufo.utfamily.api.ApiRetrofit;
import com.ufotech.ufo.utfamily.api.ApiService;
import com.ufotech.ufo.utfamily.ui.base.BaseFragment;
import com.ufotech.ufo.utfamily.ui.base.BasePresenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.ufotech.ufo.utfamily.constants.Constant.CentralWeatherBureau;
import static com.ufotech.ufo.utfamily.constants.Constant.ChanghuaList;
import static com.ufotech.ufo.utfamily.constants.Constant.ChiayiCityList;
import static com.ufotech.ufo.utfamily.constants.Constant.ChiayiList;
import static com.ufotech.ufo.utfamily.constants.Constant.CurrentWeather;
import static com.ufotech.ufo.utfamily.constants.Constant.CurrentWeatherPicture;
import static com.ufotech.ufo.utfamily.constants.Constant.HsinchuCityList;
import static com.ufotech.ufo.utfamily.constants.Constant.HsinchuList;
import static com.ufotech.ufo.utfamily.constants.Constant.HualienList;
import static com.ufotech.ufo.utfamily.constants.Constant.KaohsiungCityList;
import static com.ufotech.ufo.utfamily.constants.Constant.KeelungList;
import static com.ufotech.ufo.utfamily.constants.Constant.KinmenList;
import static com.ufotech.ufo.utfamily.constants.Constant.MatsuList;
import static com.ufotech.ufo.utfamily.constants.Constant.MiaoliList;
import static com.ufotech.ufo.utfamily.constants.Constant.NantouList;
import static com.ufotech.ufo.utfamily.constants.Constant.PenghuList;
import static com.ufotech.ufo.utfamily.constants.Constant.PingtungList;
import static com.ufotech.ufo.utfamily.constants.Constant.TaichungList;
import static com.ufotech.ufo.utfamily.constants.Constant.TainanList;
import static com.ufotech.ufo.utfamily.constants.Constant.TaipeiCityList;
import static com.ufotech.ufo.utfamily.constants.Constant.TaipeiList;
import static com.ufotech.ufo.utfamily.constants.Constant.TaitungList;
import static com.ufotech.ufo.utfamily.constants.Constant.TaoyuanList;
import static com.ufotech.ufo.utfamily.constants.Constant.WeeklyWeather;
import static com.ufotech.ufo.utfamily.constants.Constant.YilanList;
import static com.ufotech.ufo.utfamily.constants.Constant.YunlinList;

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private final String TAG = getName();
    private static final String ARG_PARAM1 = "str1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private LinearLayout ll_weather;
    private TextView tv_outside_temperature, tv_outside_temperature_text, tv_location, tv_weather,
            tv_today_suggested, tv_update_diary, informationTextView;
    private ImageView iv_weather;
    private Button subscribeButton, logTokenButton, btn_view_content, btn_view_content2;
    LocationManager locationManager = null;
    Location myLocation = null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss.SSSZ", Locale.getDefault());
    private String currentLocation;
    private Document weatherRssDoc, weatherImgDoc;
    private String aver_temp, area, situation;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void getBundle(Bundle bundle) {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView(View view, Bundle bundle) {
        ll_weather = (LinearLayout) view.findViewById(R.id.ll_weather);
        tv_outside_temperature = (TextView) view.findViewById(R.id.tv_outside_temperature);
        tv_outside_temperature_text = (TextView) view.findViewById(R.id.tv_outside_temperature_text);
        tv_location = (TextView) view.findViewById(R.id.tv_location);
        tv_weather = (TextView) view.findViewById(R.id.tv_weather);
        tv_today_suggested = (TextView) view.findViewById(R.id.tv_today_suggested);
        tv_update_diary = (TextView) view.findViewById(R.id.tv_update_diary);
        informationTextView = (TextView) view.findViewById(R.id.informationTextView);
        informationTextView.setText(mParam1);
        iv_weather = (ImageView) view.findViewById(R.id.iv_weather);
        subscribeButton = (Button) view.findViewById(R.id.subscribeButton);
        logTokenButton = (Button) view.findViewById(R.id.logTokenButton);
        btn_view_content = (Button) view.findViewById(R.id.btn_view_content);
        btn_view_content2 = (Button) view.findViewById(R.id.btn_view_content2);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        btn_view_content.setOnClickListener(this);
        btn_view_content2.setOnClickListener(this);
        subscribeButton.setOnClickListener(this);
        logTokenButton.setOnClickListener(this);
    }

    /**
     * 只有第一次進入時會加載的數據
     * 比 initData( ) 更早被執行
     */
    @Override
    public void loadData() {
        if (checkPermissions()) {
            locationServiceInitial();
        }
    }

    /**
     * 檢查權限取得狀態
     *
     * @return 是否取得
     */
    public Boolean checkPermissions(){

        if(Build.VERSION.SDK_INT >= 23) {

            String permissions[] = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION};

            List<String> pm_list = new ArrayList<>();

            for (String permission : permissions) {
                int pm = ActivityCompat.checkSelfPermission(mActivity, permission);
                if (pm != PackageManager.PERMISSION_GRANTED) {
                    pm_list.add(permission);
                }
            }
            if (pm_list.size() > 0) {
                for (int i = 0; i < pm_list.size(); i++) {
                    Log.d(TAG, pm_list.get(i));
                }
                Log.d(TAG, pm_list.size() + "");

                // 在 Fragment 中申請權限，
                // 不要使用 ActivityCompat.requestPermissions,
                // 直接使用 Fragment 的 requestPermissions 方法，
                // 否則在 Fragment 中無法回調 onRequestPermissionsResult
                requestPermissions(pm_list.toArray(new String[pm_list.size()]), 1);

                // Activity 寫法如下:
                // ActivityCompat.requestPermissions(mActivity, pm_list.toArray(new String[pm_list.size()]), 1);

                return false;
            }
        }
        return true;
    }

    /**
     * 返回權限取得結果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case 1:
                if (grantResults.length > 0){
                    for (int i = 0; i < permissions.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                            Log.d(TAG,permissions[i] +" allow");
                            locationServiceInitial();
                        }
                        else {
//                            Snackbar.make(ll_weather, "請開啟定位服務", Snackbar.LENGTH_LONG)
//                                    .setAction("Action", new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            // 開啟設定頁面
//                                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                                        }
//                                    })
//                                    .setActionTextColor(Color.YELLOW)
//                                    .show();
                            Log.d(TAG,permissions[i] + " not allow");
                        }
                    }
                } else {
                    Log.d(TAG," no pm allow");
                }
                return;
            default:
                Log.d(TAG, "沒進去 switch");
                break;
        }
    }

    /**
     * 初始化定位服務
     */
    public void locationServiceInitial() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 取得locationManager
                locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);

                // 取得所有可使用的方式
                List<String> providerList = Objects.requireNonNull(locationManager).getProviders(true);
                String provider;
                if (providerList.contains(LocationManager.GPS_PROVIDER)) {
                    provider = LocationManager.GPS_PROVIDER;
                } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
                    provider = LocationManager.NETWORK_PROVIDER;
                } else {
                    Log.d(TAG, "沒有定位權限");
                    return;
                }

                Log.d(TAG, provider + "可使用");

                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(mActivity, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                // 取得上一個位置
                Location location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    getLocation(location);
                } else {
                    // 無法獲得當前位置
                    String info = "努力載入中...";
                    Toast.makeText(mActivity, info, Toast.LENGTH_SHORT).show();
                }

                // 監聽位置變化 (使用哪種方式, 毫秒, 距離, 監聽器)
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
            }
        }).run();
    }

    /**
     * 取得當前位置座標地址
     * Key = AIzaSyD59Upgo3-875f2I602dc1kA8Z0I7ggg0o
     *
     * @param location 經緯度
     */
    private void getLocation(Location location) {
        // 實際上報時間
        // String time = sdf.format(new Date(location.getTime()));
        // timeText.setText("實際上報時間：" + time);
        if (isBetterLocation(location, myLocation)) {

            double lat = location.getLatitude(); // 獲取緯度
            double lon = location.getLongitude(); // 獲取經度
            String provider = location.getProvider(); // 位置提供者
            float accuracy = location.getAccuracy(); // 位置的準確性
            double altitude = location.getAltitude(); // 高度信息
            float bearing = location.getBearing(); // 方向角
            float speed = location.getSpeed(); // 速度 米/秒

            String locationTime = sdf.format(new Date(location.getTime()));
            String currentTime = null;

            if (myLocation != null) {
                currentTime = sdf.format(new Date(myLocation.getTime()));
                myLocation = location;

            } else {
                myLocation = location;
            }

            // 獲取當前詳細地址
            StringBuilder sb = new StringBuilder();
            if (myLocation != null) {
                Geocoder geo = new Geocoder(getActivity(), Locale.TRADITIONAL_CHINESE);
                List<Address> addresses = null;
                String returnAddress, CountryName, AdminArea, Locality, Thoroughfare, FeatureName, PostalCode;

                try {
                    addresses = geo.getFromLocation(myLocation.getLatitude(), myLocation.getLongitude(), 1);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (addresses != null && addresses.size() > 0) {

                    Address address = addresses.get(0);
                    returnAddress = address.getAddressLine(0); // 100台灣台北市中正區信陽街33號
                    CountryName = address.getCountryName();  // 台灣省
                    AdminArea = address.getAdminArea();  // 台北市
                    Locality = address.getLocality();  // 中正區
                    Thoroughfare = address.getThoroughfare();  // 信陽街(包含路巷弄)
                    FeatureName = address.getFeatureName();  // 33(號)
                    PostalCode = address.getPostalCode();  // 100(郵遞區號)

                    sb.append(returnAddress);

                    if (CountryName.equals("台灣")) {
                        selectAdminArea(AdminArea);
                    } else {
                        tv_location.setText("目前只有台灣的氣象資訊");
                    }

                } else {
                    Toast.makeText(mActivity, "找不到您的所在地區", Toast.LENGTH_SHORT).show();
                }
            }

            currentLocation = "經度：" + lon + "\n緯度：" + lat + "\n服務商：" + provider
                    + "\n準確性：" + accuracy + "\n高度：" + altitude + "\n方向角：" + bearing
                    + "\n速度：" + speed + "\n上次上報時間：" + currentTime + "\n最新上報時間："
                    + locationTime + "\n您所在的城市：" + sb.toString();
            Log.d(TAG, currentLocation);
        }
    }

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {  // 定位狀態改變
            Log.i(TAG, "onStatusChanged: " + provider);
            // status = OUT_OF_SERVICE 供應商停止服務
            // status = TEMPORARILY_UNAVAILABLE 供應商暫停服務
            if (LocationProvider.OUT_OF_SERVICE == status) {
                Toast.makeText(mActivity, "GPS服務丟失,切換至網絡定位",
                        Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(mActivity, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }
        }
        @Override
        public void onProviderEnabled(String provider) {  // 當 GPS 或網路定位功能開啟
            Log.i(TAG, "onProviderEnabled: " + provider);
            locationServiceInitial();
        }
        @Override
        public void onProviderDisabled(String provider) {  // 當 GPS 或網路定位功能關閉時
            Log.i(TAG, "onProviderDisabled: " + provider);
        }
        @Override
        public void onLocationChanged(Location location) {  // 當地點改變時
            Log.i(TAG, "onLocationChanged");
            getLocation(location);
        }
    };

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    /** Determines whether one Location reading is better than the current Location fix
     * @param location The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new one
     */
    protected  boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null ) {
            // A new location is always better than no location
            return  true ;
        }
        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0 ;
        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return  true ;
            // If the new location is more than two minutes older, it must be worse
        } else  if (isSignificantlyOlder) {
            return  false ;
        }
        // Check whether the new location fix is more or less accurate
        int accuracyDelta = ( int ) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0 ;
        boolean isMoreAccurate = accuracyDelta < 0 ;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200 ;
        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());
        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return  true ;
        } else  if (isNewer && !isLessAccurate) {
            return  true ;
        } else  if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return  true ;
        }
        return  false ;
    }

    /** Checks whether two providers are the same */
    private  boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null ) {
            return provider2 == null ;
        }
        return provider1.equals(provider2);
    }

    /**
     * 依所在地區選取 RSS 資源
     * 資料來源：交通部中央氣象局（https://www.cwb.gov.tw/V7/service/eservice/rss.htm）
     *
     * @param AdminArea 縣市
     */
    private void selectAdminArea(String AdminArea) {
        String xml = "", img = "";
        switch (AdminArea) {
            case "臺北市": xml = TaipeiCityList; img = "TaipeiCityList"; break;
            case "高雄市": xml = KaohsiungCityList; img = "KaohsiungCityList"; break;
            case "基隆市": xml = KeelungList; img = "KeelungList"; break;
            case "新北市": xml = TaipeiList; img = "TaipeiList"; break;
            case "桃園市": xml = TaoyuanList; img = "TaoyuanList"; break;
            case "新竹縣": xml = HsinchuList; img = "HsinchuList"; break;
            case "苗栗縣": xml = MiaoliList; img = "MiaoliList"; break;
            case "臺中市": xml = TaichungList; img = "TaichungList"; break;
            case "彰化縣": xml = ChanghuaList; img = "ChanghuaList"; break;
            case "南投縣": xml = NantouList; img = "NantouList"; break;
            case "雲林縣": xml = YunlinList; img = "YunlinList"; break;
            case "嘉義縣": xml = ChiayiList; img = "ChiayiList"; break;
            case "臺南市": xml = TainanList; img = "TainanList"; break;
            case "新竹市": xml = HsinchuCityList; img = "HsinchuCityList"; break;
            case "屏東縣": xml = PingtungList; img = "PingtungList"; break;
            case "嘉義市": xml = ChiayiCityList; img = "ChiayiCityList"; break;
            case "宜蘭縣": xml = YilanList; img = "YilanList"; break;
            case "花蓮縣": xml = HualienList; img = "HualienList"; break;
            case "臺東縣": xml = TaitungList; img = "TaitungList"; break;
            case "澎湖縣": xml = PenghuList; img = "PenghuList"; break;
            case "金門縣": xml = KinmenList; img = "KinmenList"; break;
            case "連江縣": xml = MatsuList; img = "MatsuList"; break;
        }
        getWeatherRSS(xml);
        getWeatherIMG(img);
    }

    /**
     * 取得中央氣象局RSS原始碼
     */
    private void getWeatherRSS(String xml) {
        ApiService mApiService = ApiRetrofit.getInstance().getApiService();
        Call<ResponseBody> call = mApiService.getHtmlSourceCode(HttpUrl.parse(CentralWeatherBureau + CurrentWeather + xml + ".xml"));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    String html = Objects.requireNonNull(response.body()).string();
                    weatherRssDoc = Jsoup.parse(html);

                    currentWeather(); // 顯示當前天氣預報

                    // 當所有視圖創建完成，也可以正確取得天氣資料後
                    // 即可把溫度區塊 setOnClickListener
                    ll_weather.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 爬蟲線程繁重，開個執行緒給他跑
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    futureWeather();
                                }
                            }).run();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    call.cancel();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d(TAG, "onFailure = " + t.getMessage());
                mActivity.runOnUiThread(new Runnable() {
                    public void run(){
                        // 狀況1 : URL 錯誤
                        // @Url parameter is null.
                        // 狀況2 : 網路連不上或連線超時
                        // Unable to resolve host"www.cwb.gov.tw":No address associated with hostname
                        // 所以...網路錯誤，一言以蔽之
                        tv_location.setText("網路錯誤");
                        tv_weather.setText(null);
                        tv_outside_temperature.setText(null);
                        tv_outside_temperature_text.setText(null);
                    }
                });
            }
        });
    }

    /**
     * 顯示當前天氣預報
     */
    private void currentWeather() {
        try {
            Elements itemTags = weatherRssDoc.getElementsByTag("item");
            Elements titleTags = itemTags.get(0).getElementsByTag("title");
            String[] weathers = titleTags.get(0).text().split(" ");

            // 高雄市溫度
            aver_temp = String.valueOf((Integer.valueOf(weathers[5])
                    + Integer.valueOf(weathers[7])) / 2) + "℃";
            // 高雄市
            area = weathers[1].substring(0,3);
            // 高雄市天氣狀況
            situation = weathers[3];

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            // 節點出錯會顯示 GG
            if (aver_temp.equals("℃")) {
                aver_temp = "GG";
            }
            if (area == null) {
                area = "GG";
            }
            if (situation == null) {
                situation = "GG";
            }
            Log.d(TAG, aver_temp + ", " + area + ", " + situation);
            // 將內容交給 UI 執行緒做顯示
            mActivity.runOnUiThread(new Runnable() {
                public void run(){
                    tv_outside_temperature.setText(aver_temp);
                    tv_location.setText(area);
                    tv_weather.setText(situation);
                    tv_outside_temperature_text.setText(getString(R.string.current_temperature));
                }
            });
        }
    }

    /**
     * 顯示一周天氣預報
     */
    private void futureWeather() {
        StringBuilder weekWeather = new StringBuilder();
        try {
            Elements itemTags = weatherRssDoc.getElementsByTag("item");
            Elements descriptionTags = itemTags.get(1).getElementsByTag("description");
            int i = descriptionTags.get(0).text().indexOf("CDATA");
            String container = descriptionTags.get(0).text().substring(i+1);
            String [] weathers = container.split("<BR>");

            for (String weather : weathers) {
                weekWeather.append(weather.trim()).append("\n");
                Log.d(TAG, weather.trim());  // trim():刪除空格
            }
            weekWeather.append("\n").append(currentLocation).append("\n\n詳情請見中央氣象局~");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 開一個 Dialog 做顯示
                    new AlertDialog.Builder(mActivity)
                            .setTitle("一週氣象預報")
                            .setMessage(weekWeather.toString())
                            .setIcon(R.drawable.icon)
                            .setPositiveButton("關閉",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                        }
                                    })
                            .setNegativeButton("前往",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            Uri uri= Uri.parse(CentralWeatherBureau + WeeklyWeather);
                                            Intent i=new Intent(Intent.ACTION_VIEW,uri);
                                            startActivity(i);
                                        }
                                    }).show();
                }
            });
        }
    }

    private void getWeatherIMG(String img) {
        ApiService mApiService = ApiRetrofit.getInstance().getApiService();
        Call<ResponseBody> call = mApiService.getHtmlSourceCode(HttpUrl.parse(CentralWeatherBureau + CurrentWeatherPicture));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    String html = Objects.requireNonNull(response.body()).string();
                    weatherImgDoc = Jsoup.parse(html);
                    try {
                        Elements trTags = weatherImgDoc.select("tr#" + img);
                        Element imgTag = trTags.select("img[src]").first();
                        String relSrc = imgTag.attr("src");
                        Log.d(TAG, relSrc);

                        // 顯示天氣圖片
                        Glide
                                .with(mActivity)
                                .load(CentralWeatherBureau + relSrc)
                                .apply(new RequestOptions().placeholder(R.color.colorPrimary).error(R.drawable.icon))
                                .into(iv_weather);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "Jsoup parse WeatherIMG url error !!");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Jsoup parse WeatherIMG html document error !!");
                } finally {
                    call.cancel();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d(TAG, "onFailure = " + t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view_content:
                tv_today_suggested.setText(R.string.today_suggested_content);
                break;
            case R.id.btn_view_content2:
                tv_update_diary.setText(R.string.update_diary_content);
                break;
            case R.id.subscribeButton:
                // [START subscribe_topics]
                FirebaseMessaging.getInstance().subscribeToTopic("news");
                // [END subscribe_topics]

                // Log and toast
                String msg_subscribed = getString(R.string.msg_subscribed);
                Log.d(TAG, msg_subscribed);
                Toast.makeText(mActivity, msg_subscribed, Toast.LENGTH_SHORT).show();
                break;
            case R.id.logTokenButton:
                // Get token
                String token = FirebaseInstanceId.getInstance().getToken();

                // Log and toast
                String msg_token_fmt = getString(R.string.msg_token_fmt, token);
                Log.d(TAG, msg_token_fmt);
                Toast.makeText(mActivity, msg_token_fmt, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(locationManager!=null)
            locationManager.removeUpdates(locationListener);
        Log.i(TAG, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mActivity, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // 監聽位置變化 (使用哪種方式, 毫秒, 距離, 監聽器)
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        Log.i(TAG, "onResume");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(locationManager!=null)
            locationManager.removeUpdates(locationListener);
        Log.i(TAG, "onDestroy");
    }
}
