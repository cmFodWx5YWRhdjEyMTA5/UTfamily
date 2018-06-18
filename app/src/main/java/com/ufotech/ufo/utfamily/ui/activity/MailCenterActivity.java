package com.ufotech.ufo.utfamily.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ufotech.ufo.utfamily.R;
import com.ufotech.ufo.utfamily.ui.base.BaseActivity;
import com.ufotech.ufo.utfamily.ui.base.BasePresenter;

/**
 *  -----------------------------------------------------------------------------------------
 *  @author ：   UFO（陳俊融）Github：https://github.com/jyunrong
 *  @date ：   2018/5/15    下午 06:05
 *  @description ：   訊息中心
 *  -----------------------------------------------------------------------------------------
 */
public class MailCenterActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    ImageView iv_top_bar_start, iv_top_bar_end;
    TextView tv_top_bar_title;
    private RelativeLayout rl_share_device, rl_firebase_message, rl_dynamic_share;
    private Intent intent;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_mail_center;
    }

    @Override
    public void initView() {
        iv_top_bar_start = (ImageView) findViewById(R.id.iv_top_bar_start);
        iv_top_bar_end = (ImageView) findViewById(R.id.iv_top_bar_end);
        iv_top_bar_end.setImageResource(R.drawable.selector_setting);
        tv_top_bar_title = (TextView) findViewById(R.id.tv_top_bar_title);
        tv_top_bar_title.setText(getText(R.string.label_MailCenter));
        rl_share_device = (RelativeLayout) findViewById(R.id.rl_share_device);
        rl_firebase_message = (RelativeLayout) findViewById(R.id.rl_firebase_message);
        rl_dynamic_share = (RelativeLayout) findViewById(R.id.rl_dynamic_share);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        iv_top_bar_start.setOnClickListener(this);
        iv_top_bar_end.setOnClickListener(this);
        rl_share_device.setOnClickListener(this);
        rl_firebase_message.setOnClickListener(this);
        rl_dynamic_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_top_bar_start:
                Log.d(TAG, "返回");
                finish();
                break;
            case R.id.iv_top_bar_end:
                Log.d(TAG, "訊息設定");
                break;
            case R.id.rl_share_device:
                Log.d(TAG, "共享設備");
                break;
            case R.id.rl_firebase_message:
                intent = new Intent(MailCenterActivity.this, CloudMessagingActivity.class);
                startActivity(intent);
                Log.d(TAG, "Firebase 訊息");
                break;
            case R.id.rl_dynamic_share:
                intent = new Intent(MailCenterActivity.this, DynamicShareActivity.class);
                startActivity(intent);
                Log.d(TAG, "動態分享");
                break;
        }
    }
}
