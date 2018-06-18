package com.ufotech.ufo.utfamily.ui.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ufotech.ufo.utfamily.R;
import com.ufotech.ufo.utfamily.ui.base.BaseActivity;
import com.ufotech.ufo.utfamily.ui.base.BasePresenter;
import com.ufotech.ufo.utfamily.utils.NetWorkUtils;

import java.util.Objects;

/**
 *  -----------------------------------------------------------------------------------------
 *  @author ：   UFO（陳俊融）Github：https://github.com/jyunrong
 *  @date ：   2018/5/15    下午 06:06
 *  @description ：   動態分享
 *  -----------------------------------------------------------------------------------------
 */
public class DynamicShareActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    ImageView iv_top_bar_start, iv_top_bar_end;
    TextView tv_top_bar_title, tv_DynamicShare;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_dynamic_share;
    }

    @Override
    public void initView() {
        iv_top_bar_start = (ImageView) findViewById(R.id.iv_top_bar_start);
        iv_top_bar_end = (ImageView) findViewById(R.id.iv_top_bar_end);
        tv_top_bar_title = (TextView) findViewById(R.id.tv_top_bar_title);
        tv_top_bar_title.setText(getText(R.string.label_DynamicShare));
        tv_DynamicShare = (TextView) findViewById(R.id.tv_DynamicShare);
    }

    @Override
    public void initData() {
        String data = NetWorkUtils.sharingIntent(this, getIntent());
        String type = getIntent().getType();
        if(!TextUtils.isEmpty(data)) {
            if ("text/plain".equals(type)) {
                Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(type).startsWith("image/")) {
                Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void initListener() {
        iv_top_bar_start.setOnClickListener(this);
        iv_top_bar_end.setOnClickListener(this);
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
        }
    }
}
