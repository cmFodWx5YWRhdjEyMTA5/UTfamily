package com.ufotech.ufo.utfamily.ui.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import ufoUI.bottombar.BottomBarItem;
import ufoUI.bottombar.BottomBarLayout;
import ufoUI.statusbar.Eyes;
import com.ufotech.ufo.utfamily.R;
import com.ufotech.ufo.utfamily.ui.base.BaseActivity;
import com.ufotech.ufo.utfamily.ui.base.BasePresenter;
import com.ufotech.ufo.utfamily.ui.fragment.HomeFragment;
import com.ufotech.ufo.utfamily.ui.fragment.ManagementFragment;
import com.ufotech.ufo.utfamily.ui.fragment.MemberFragment;
import com.ufotech.ufo.utfamily.ui.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();

    // BottomBarLayout
    private List<Fragment> mFragmentList = new ArrayList<>();
    private FrameLayout mFl_content;
    private BottomBarLayout mBottomBarLayout;
    private RotateAnimation mRotateAnimation;
    private Handler mHandler = new Handler();

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mFl_content = (FrameLayout) findViewById(R.id.fl_content);
        mBottomBarLayout = (BottomBarLayout) findViewById(R.id.bbl);
    }

    @Override
    public void initData() {
        initFCM();

        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("str1","HomeFragment");
        homeFragment.setArguments(bundle1);
        mFragmentList.add(homeFragment);

        ManagementFragment videoFragment = new ManagementFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("str1", "视频");
        videoFragment.setArguments(bundle2);
        mFragmentList.add(videoFragment);

        MemberFragment microFragment = new MemberFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString("str1", "微头条");
        microFragment.setArguments(bundle3);
        mFragmentList.add(microFragment);

        MineFragment meFragment = new MineFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putString("str1", "我的");
        meFragment.setArguments(bundle4);
        mFragmentList.add(meFragment);

        changeFragment(0); //默认显示第一页
    }

    @Override
    public void initListener() {
        mBottomBarLayout.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final BottomBarItem bottomBarItem, int previousPosition, final int currentPosition) {
                Log.i(TAG, "fragment position: " + currentPosition);
//                setStatusBarColor(currentPosition);//设置状态栏颜色

                changeFragment(currentPosition);

                if (currentPosition == 0) {
                    //如果点击的是首页
                    if (previousPosition == currentPosition) {
                        //如果是在原来位置上点击,更换首页图标并播放旋转动画
                        bottomBarItem.setIconSelectedResourceId(R.mipmap.tab_loading);//更换成加载图标
                        bottomBarItem.setStatus(true);

                        //播放旋转动画
                        if (mRotateAnimation == null) {
                            mRotateAnimation = new RotateAnimation(0, 360,
                                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                                    0.5f);
                            mRotateAnimation.setDuration(800);
                            mRotateAnimation.setRepeatCount(-1);
                        }
                        ImageView bottomImageView = bottomBarItem.getImageView();
                        bottomImageView.setAnimation(mRotateAnimation);
                        bottomImageView.startAnimation(mRotateAnimation);//播放旋转动画

                        //模拟数据刷新完毕
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                boolean tabNotChanged = mBottomBarLayout.getCurrentItem() == currentPosition; //是否还停留在当前页签
                                bottomBarItem.setIconSelectedResourceId(R.mipmap.tab_home_selected);//更换成首页原来选中图标
                                bottomBarItem.setStatus(tabNotChanged);//刷新图标
                                cancelTabLoading(bottomBarItem);
                            }
                        }, 3000);
                        return;
                    }
                }

                //如果点击了其他条目
                BottomBarItem bottomItem = mBottomBarLayout.getBottomItem(0);
                bottomItem.setIconSelectedResourceId(R.mipmap.tab_home_selected);//更换为原来的图标
                cancelTabLoading(bottomItem);//停止旋转动画
            }
        });

        mBottomBarLayout.setUnread(0, 20);//设置第一个页签的未读数为20
        mBottomBarLayout.setUnread(1, 1001);//设置第二个页签的未读数
        mBottomBarLayout.showNotify(2);//设置第三个页签显示提示的小红点
        mBottomBarLayout.setMsg(3, "NEW");//设置第四个页签显示NEW提示文字
    }

    /**
     * 停止首頁頁籤的旋轉動畫
     */
    private void cancelTabLoading(BottomBarItem bottomItem) {
        Animation animation = bottomItem.getImageView().getAnimation();
        if (animation != null) {
            animation.cancel();
        }
    }

    private void changeFragment(int currentPosition) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 如果當前 Fragment 未被加載過，就 add
        if (!mFragmentList.get(currentPosition).isAdded()) {
            transaction.add(R.id.fl_content, mFragmentList.get(currentPosition));
            Log.d(TAG, "Fragment" + currentPosition + "已被加入");
        }
        // 把全部 Fragment 隱藏
        for (int i=0; i<mFragmentList.size(); i++) {
            transaction.hide(mFragmentList.get(i));
        }
        // 只顯示被點擊的 Fragment
        transaction.show(mFragmentList.get(currentPosition));
        transaction.commit();
    }

    private void initFCM() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}