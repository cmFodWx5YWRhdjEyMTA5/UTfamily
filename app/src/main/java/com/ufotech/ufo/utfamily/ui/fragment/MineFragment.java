package com.ufotech.ufo.utfamily.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ufotech.ufo.utfamily.ui.activity.NotifyDialogActivity;
import com.ufotech.ufo.utfamily.R;
import com.ufotech.ufo.utfamily.SettingsActivity;
import com.ufotech.ufo.utfamily.ui.activity.FileManagerActivity;
import com.ufotech.ufo.utfamily.ui.activity.MailCenterActivity;
import com.ufotech.ufo.utfamily.ui.activity.PersonalInfoActivity;
import com.ufotech.ufo.utfamily.ui.base.BaseFragment;
import com.ufotech.ufo.utfamily.ui.base.BasePresenter;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = MineFragment.class.getName();
    private static final String ARG_PARAM1 = "str1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private LinearLayout ll_personal_info;
    private RelativeLayout rl_small_program, rl_mall, rl_forum, rl_feed_back, rl_setting;
    private CircleImageView iv_avatar_mine;
    @SuppressLint("StaticFieldLeak")
    private static TextView tv_user_name;
    Intent intent;
    FirebaseUser user;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_mine;
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
        ll_personal_info = (LinearLayout) view.findViewById(R.id.ll_personal_info);
        rl_small_program = (RelativeLayout) view.findViewById(R.id.rl_small_program);
        rl_mall = (RelativeLayout) view.findViewById(R.id.rl_mall);
        rl_forum = (RelativeLayout) view.findViewById(R.id.rl_forum);
        rl_feed_back = (RelativeLayout) view.findViewById(R.id.rl_feed_back);
        rl_setting = (RelativeLayout) view.findViewById(R.id.rl_setting);
        iv_avatar_mine = (CircleImageView) view.findViewById(R.id.iv_avatar_mine);
        tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
    }

    @Override
    public void initData() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        //取得使用者頭像位址
        Uri photoUrl = Objects.requireNonNull(user).getPhotoUrl();
        if (photoUrl != null) {
            String imgUrl = photoUrl.toString();
            Log.d(TAG, "photoUrl : " + imgUrl);
            Glide
                    .with(this)
                    .load(photoUrl)
                    .apply(new RequestOptions().placeholder(R.mipmap.user).error(R.mipmap.ic_launcher_round))
                    .into(iv_avatar_mine);
        }
        // 取得使用者名稱
        String name = user.getDisplayName();
        tv_user_name.setText(name);
    }

    @Override
    public void initListener() {
        ll_personal_info.setOnClickListener(this);
        rl_small_program.setOnClickListener(this);
        rl_mall.setOnClickListener(this);
        rl_forum.setOnClickListener(this);
        rl_feed_back.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_personal_info:
                intent = new Intent(mActivity, PersonalInfoActivity.class);
                Log.d(TAG, "個人資料");
                startActivity(intent);
                break;
            case R.id.rl_small_program:
                intent = new Intent(mActivity, FileManagerActivity.class);
                Log.d(TAG, "我的微信小程式");
                startActivity(intent);
                break;
            case R.id.rl_mall:
                intent = new Intent(mActivity, NotifyDialogActivity.class);
                startActivity(intent);
                Snackbar.make(v, "我的商城", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Log.d(TAG, "我的商城");
                break;
            case R.id.rl_forum:
                intent = new Intent(mActivity, NotifyDialogActivity.class);
                try {
                    Thread.sleep(5000);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Snackbar.make(v, "論壇", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Log.d(TAG, "論壇");
                break;
            case R.id.rl_feed_back:
                intent = new Intent(mActivity, MailCenterActivity.class);
                newInstance(mParam1, null);
                Log.d(TAG, "幫助與反應");
                startActivity(intent);
                break;
            case  R.id.rl_setting:
                intent = new Intent(mActivity, SettingsActivity.class);
                Log.d(TAG, "設定");
                startActivity(intent);
                break;
        }
    }

    /**
     * Plot id in screen
     * @param new_name 更改後的名字
     */
    public static void updateName(final String new_name) {
        try {
            new Thread(){
                public void run() {
                    tv_user_name.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_user_name.setText(new_name);
                        }
                    });
                }
            }.start();
        } catch (Exception e) {
            Log.d(TAG, "Failed to update UI Thread: " + e.getMessage());
        }
    }
}
