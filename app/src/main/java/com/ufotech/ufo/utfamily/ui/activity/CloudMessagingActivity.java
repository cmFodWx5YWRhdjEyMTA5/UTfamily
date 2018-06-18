package com.ufotech.ufo.utfamily.ui.activity;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ufotech.ufo.utfamily.R;
import com.ufotech.ufo.utfamily.service.message.MyNotice;
import com.ufotech.ufo.utfamily.ui.base.BaseActivity;
import com.ufotech.ufo.utfamily.ui.base.BasePresenter;
import com.ufotech.ufo.utfamily.utils.DateUtils;
import com.ufotech.ufo.utfamily.utils.PreUtils;

import java.util.Date;

/**
 *  -----------------------------------------------------------------------------------------
 *  @author ：   UFO（陳俊融）Github：https://github.com/jyunrong
 *  @date ：   2018/5/15    下午 06:16
 *  @description ：   接收 FCM 的消息並立即顯示
 *  -----------------------------------------------------------------------------------------
 */
public class CloudMessagingActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    ImageView iv_top_bar_start, iv_top_bar_end;
    TextView tv_top_bar_title, tv_cloud_message_body;
    Button btn_clean_cloud_message_body;
    ScrollView scrollview_cloud_message_body;
    ImageButton imgBtn_bottom_cloud_message_body;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_clould_messaging;
    }

    @Override
    public void initView() {
        iv_top_bar_start = (ImageView) findViewById(R.id.iv_top_bar_start);
        iv_top_bar_end = (ImageView) findViewById(R.id.iv_top_bar_end);
        tv_top_bar_title = (TextView) findViewById(R.id.tv_top_bar_title);
        tv_top_bar_title.setText(getText(R.string.label_FCMMessage));
        tv_cloud_message_body = (TextView) findViewById(R.id.tv_cloud_message_body);
        btn_clean_cloud_message_body = (Button) findViewById(R.id.btn_clean_cloud_message_body);
        scrollview_cloud_message_body = (ScrollView) findViewById(R.id.scrollview_cloud_message_body);
        scrollview_cloud_message_body.post(new Runnable() {
            @Override
            public void run() {
                scrollview_cloud_message_body.fullScroll(ScrollView.FOCUS_DOWN);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    scrollview_cloud_message_body.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            if (scrollY > 200) {
                                imgBtn_bottom_cloud_message_body.setVisibility(View.VISIBLE);
                            } else {
                                imgBtn_bottom_cloud_message_body.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
        imgBtn_bottom_cloud_message_body = (ImageButton) findViewById(R.id.imgBtn_bottom_cloud_message_body);
    }

    @Override
    public void initData() {
        // 顯示舊的訊息
        new Thread(new Runnable() {
            @Override
            public void run() {
                String body = PreUtils.getString("FCM_total_body", "");
                Log.d(TAG, "Total message body : " + body);
                tv_cloud_message_body.setText(body);
            }
        }).start();
    }

    @Override
    public void initListener() {
        iv_top_bar_start.setOnClickListener(this);
        iv_top_bar_end.setOnClickListener(this);
        btn_clean_cloud_message_body.setOnClickListener(this);
        imgBtn_bottom_cloud_message_body.setOnClickListener(this);
        // 訊息一來就顯示
        MyNotice.getInstance().setOnMessageReceivedListener(new MyNotice.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived(final String messageBody) {
                Log.d(TAG, "Cloud Message Body : " + messageBody);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_cloud_message_body.append("\n\n" + DateUtils.toDateStr(new Date()) + "\n" + messageBody);
                        scrollview_cloud_message_body.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_top_bar_start:
                Log.d(TAG, "返回");
                finish();
                break;
            case R.id.iv_top_bar_end:
                break;
            case R.id.btn_clean_cloud_message_body:
                PreUtils.putString("FCM_total_body", "");
                tv_cloud_message_body.setText("");
                break;
            case R.id.imgBtn_bottom_cloud_message_body:
                scrollview_cloud_message_body.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollview_cloud_message_body.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                break;
        }
    }
}
