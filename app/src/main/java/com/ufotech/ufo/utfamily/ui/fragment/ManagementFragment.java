package com.ufotech.ufo.utfamily.ui.fragment;

import android.app.Service;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Vibrator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.ufotech.ufo.utfamily.R;
import com.ufotech.ufo.utfamily.api.ApiRetrofit;
import com.ufotech.ufo.utfamily.api.ApiService;
import com.ufotech.ufo.utfamily.model.SensorDatas;
import com.ufotech.ufo.utfamily.ui.base.BaseFragment;
import com.ufotech.ufo.utfamily.ui.base.BasePresenter;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ufoUI.AmountView;

import static com.ufotech.ufo.utfamily.utils.NetWorkUtils.isNetworkAvailable;
import static com.ufotech.ufo.utfamily.utils.UIUtils.getStringArr;
import static com.ufotech.ufo.utfamily.utils.UIUtils.showToast;

public class ManagementFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        AmountView.OnChangeListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private final String TAG = getName();
    private static final String ARG_PARAM1 = "str1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private SwipeRefreshLayout swipe_management;
    private Spinner spinner_field, spinner_situation, spinner_dormancy;
    private String[] select_field, select_situation, select_dormancy;
    private TextView tv_temperature, tv_humidity, tv_brightness;
    private AmountView av_temperature, av_humidity, av_brightness;
    private RadioGroup radioGroup_situation;
    private RadioButton radioBtn_comfortable_pointer, radioBtn_custom_setting;
    private Button btn_start_control;
    private Handler netHandler = null;//宣告特約工人的經紀人
    private HandlerThread networkThread = null;//宣告特約工人
    private Handler handler_1 = null;
    private HandlerThread handlerThread_1 = null;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_management;
    }

    @Override
    protected void getBundle(Bundle bundle) {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public static ManagementFragment newInstance(String param1, String param2) {
        ManagementFragment fragment = new ManagementFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView(View view, Bundle bundle) {
        swipe_management = (SwipeRefreshLayout) view.findViewById(R.id.swipe_management);
        swipe_management.setColorSchemeColors(getResources().getColor(R.color.colorPrimary)); // 重整圖示顏色
        spinner_field = (Spinner) view.findViewById(R.id.spinner_field);
        spinner_situation = (Spinner) view.findViewById(R.id.spinner_situation);
        spinner_dormancy = (Spinner) view.findViewById(R.id.spinner_dormancy);
        tv_temperature = (TextView) view.findViewById(R.id.tv_temperature);
        tv_humidity = (TextView) view.findViewById(R.id.tv_humidity);
        tv_brightness = (TextView) view.findViewById(R.id.tv_brightness);
        av_temperature = (AmountView) view.findViewById(R.id.av_temperature);
        av_humidity = (AmountView) view.findViewById(R.id.av_humidity);
        av_brightness = (AmountView) view.findViewById(R.id.av_brightness);
        radioGroup_situation = (RadioGroup) view.findViewById(R.id.radioGroup_situation);
        radioGroup_situation.check(R.id.radioBtn_custom_setting);
        radioBtn_comfortable_pointer = (RadioButton) view.findViewById(R.id.radioBtn_comfortable_pointer);
        radioBtn_custom_setting = (RadioButton) view.findViewById(R.id.radioBtn_custom_setting);
        btn_start_control = (Button) view.findViewById(R.id.btn_start_control);
    }

    @Override
    public void initData() {
        // Spinner 1
        select_field = getStringArr(R.array.select_field);
        ArrayAdapter<String> select_field_List = new ArrayAdapter<>(mActivity,
                android.R.layout.simple_spinner_dropdown_item,
                select_field);
        spinner_field.setAdapter(select_field_List);
        // Spinner 2
        select_situation = getStringArr(R.array.select_situation);
        ArrayAdapter<String> select_situation_List = new ArrayAdapter<>(mActivity,
                android.R.layout.simple_spinner_dropdown_item,
                select_situation);
        spinner_situation.setAdapter(select_situation_List);
        // Spinner 3
        select_dormancy = getStringArr(R.array.select_dormancy);
        ArrayAdapter<String> select_dormancy_List = new ArrayAdapter<>(mActivity,
                android.R.layout.simple_spinner_dropdown_item,
                select_dormancy);
        spinner_dormancy.setAdapter(select_dormancy_List);
        // AmountView
        av_temperature.setCurrentValue(26);
        av_humidity.setCurrentValue(60);
        av_brightness.setCurrentValue(35);
    }

    @Override
    public void initListener() {
        swipe_management.setOnRefreshListener(this);
        av_temperature.setOnChangeListener(this);
        av_humidity.setOnChangeListener(this);
        av_brightness.setOnChangeListener(this);
        radioBtn_comfortable_pointer.setOnCheckedChangeListener(this);
        radioBtn_custom_setting.setOnCheckedChangeListener(this);
        btn_start_control.setOnClickListener(this);
        spinner_field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //
//                showToast("你選的是" + select_field[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_situation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_dormancy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void loadData() {
        //
    }

    /**
     * 檢查網路連線
     */
    private boolean netFlag, toastFlag;
    private void startCheckNetwork() {
        // 聘請一個特約工人，有其經紀人派遣其工人做事 (另起一個有Handler的Thread)
        networkThread = new HandlerThread(getString(R.string.networkThread_name));
        // 讓Worker待命，等待其工作 (開啟Thread)
        networkThread.start();
        // 找到特約工人的經紀人，這樣才能派遣工作 (找到Thread上的Handler)
        netHandler = new Handler(networkThread.getLooper());
        // 請經紀人指派工作名稱 runnable，給工人做
        netHandler.post(runnable_network);
        Log.d(TAG, "開始檢查網路連線 ~~~~~");
        netFlag = true;
        toastFlag = true;
    }
    private Runnable runnable_network = new Runnable() {
        @Override
        public void run() {
            if (isNetworkAvailable(mActivity)) {
                while (netFlag) {
                    startThread();
                    netFlag = false;
                    toastFlag = true;
                }
            } else { // 如果網路不通
                while (toastFlag) {
                    stopThread();
                    Log.d(TAG, "網路不給力 ~~~~~~~");
                    netFlag = true;
                    toastFlag = false;
                }
            }
            // .............................
            // 做了很多事
            // 老闆指定每隔幾秒要做一次工作1 (單位毫秒:1000等於1秒)
            netHandler.postDelayed(this, 5000);
        }
    };
    private void stopCheckNetwork() {
        try {
            // 移除工人上的工作
            if (netHandler != null) {
                netHandler.removeCallbacks(runnable_network);
            }
            // 解聘工人 (關閉Thread)
            if (networkThread != null) {
                networkThread.quit();
            }
            Log.d(TAG, "停止檢查網路連線 ~~~~~");
            // 停止收資料
            stopThread();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 下面是 Debug 用的
            try {
                Thread.sleep(1500);
                Log.d(TAG, "networkThread : " + networkThread.getState().toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 網路連線成功時會重複做的事
     */
    private void startThread() {
        handlerThread_1 = new HandlerThread(getString(R.string.handlerThread_1_name));
        handlerThread_1.start();
        handler_1 = new Handler(handlerThread_1.getLooper());
        handler_1.post(runnable_1);
        Log.d(TAG, "開始收資料 ~~~~~~~");
    }
    private Runnable runnable_1 = new Runnable() {
        @Override
        public void run() {
            getData();
            handler_1.postDelayed(this, 5000);
        }
    };
    private void stopThread() {
        try {
            if (handler_1 != null) {
                handler_1.removeCallbacks(runnable_1);
            }
            if (handlerThread_1 != null) {
                handlerThread_1.quit();
            }
            Log.d(TAG, "停止收資料 ~~~~~");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向樹莓派 Request Sensor 資料
     */
    private void getData() {
        ApiService mApiService = ApiRetrofit.getInstance().getApiService();
        Call<SensorDatas> call = mApiService.getSensorDatas();
        call.enqueue(new Callback<SensorDatas>() {
            @Override
            public void onResponse(Call<SensorDatas> call, Response<SensorDatas> response) {
                try {
                    SensorDatas sensorDatas = response.body();
                    Log.d(TAG, sensorDatas.toString());
                    String[] arr = sensorDatas.getArduinoSensorData().split(",");
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_temperature.setText(arr[0]);
                            tv_humidity.setText(arr[1]);
                            tv_brightness.setText(arr[2]);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    stopThread();
                    showToast("資料格式不正確");
                }
            }

            @Override
            public void onFailure(Call<SensorDatas> call, Throwable t) {
                Log.d(TAG, "onFailure = " + t.getMessage());
                // 如果不是這個通俗的毛病
                if (!t.getMessage().equals("unexpected end of stream")) {
                    // 就停止 APP
                    stopThread();
                    t.printStackTrace();
                }
            }
        });
    }

    /**
     * 發送設定的數值給樹莓派
     */
    private void sendData() {
        //
    }

    @Override
    public void onRefresh() {
        // TODO: 下載並更新列表的資料
        // loadData();
        swipe_management.setRefreshing(false); //結束更新動畫
    }

    @Override
    public void onChanged(int value) {
        radioGroup_situation.check(R.id.radioBtn_custom_setting);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.radioBtn_comfortable_pointer:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        av_temperature.setCurrentValue(26);
                        av_humidity.setCurrentValue(60);
                        av_brightness.setCurrentValue(35);
                    }
                }).run();
                break;
            case R.id.radioBtn_custom_setting:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_control:
                Vibrator vibrator = (Vibrator) mActivity.getSystemService(Service.VIBRATOR_SERVICE);
                Objects.requireNonNull(vibrator).vibrate(new long[]{0,100}, -1);
                // 發送資料
                sendData();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 停止檢查網路
        stopCheckNetwork();
    }
    @Override
    public void onResume(){
        super.onResume();
        // 檢查網路連線
        startCheckNetwork();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        // 停止檢查網路
        stopCheckNetwork();
    }
}
