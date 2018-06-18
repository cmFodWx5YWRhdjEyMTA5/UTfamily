package com.ufotech.ufo.utfamily.ui.activity;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ufotech.ufo.utfamily.R;
import com.ufotech.ufo.utfamily.ui.base.BaseActivity;
import com.ufotech.ufo.utfamily.ui.base.BasePresenter;
import com.ufotech.ufo.utfamily.utils.PreUtils;

import static com.ufotech.ufo.utfamily.utils.UIUtils.showToast;

/**
 *  -----------------------------------------------------------------------------------------
 *  @author ：   UFO（陳俊融）Github：https://github.com/jyunrong
 *  @date ：   2018/5/15    下午 09:01
 *  @description ：   通知的自訂對話框
 *  -----------------------------------------------------------------------------------------
 */
public class NotifyDialogActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    TextView tv_notify_dialog_title, tv_notify_dialog_content;
    Button btn_notify_dialog_show, btn_notify_dialog_close, btn_notify_dialog_setting;
    Intent intent;
    String title, message;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_notify_dialog;
    }

    @Override
    public void initBSCV() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void initView() {
        tv_notify_dialog_title  = (TextView)findViewById(R.id.tv_notify_dialog_title);
        tv_notify_dialog_content = (TextView)findViewById(R.id.tv_notify_dialog_content);
        btn_notify_dialog_show = (Button)findViewById(R.id.btn_notify_dialog_show);
        btn_notify_dialog_close = (Button)findViewById(R.id.btn_notify_dialog_close);
        btn_notify_dialog_setting = (Button)findViewById(R.id.btn_notify_dialog_setting);
    }

    @Override
    public void initData() {
        message = PreUtils.getString("FCM_latest_body", "");
        tv_notify_dialog_content.setText(message);
    }

    @Override
    public void initListener() {
        btn_notify_dialog_show.setOnClickListener(this);
        btn_notify_dialog_close.setOnClickListener(this);
        btn_notify_dialog_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_notify_dialog_show:
                intent = new Intent(NotifyDialogActivity.this, CloudMessagingActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_notify_dialog_close:
                finish();
                break;
            case R.id.btn_notify_dialog_setting:
                showToast("you click setting!");
                break;
        }
    }
}