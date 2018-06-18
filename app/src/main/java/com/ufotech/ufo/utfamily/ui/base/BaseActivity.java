package com.ufotech.ufo.utfamily.ui.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ufotech.ufo.utfamily.R;
import com.ufotech.ufo.utfamily.ui.activity.MainActivity;
import com.yanzhenjie.permission.PermissionListener;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import ufoUI.statusbar.Eyes;

/**
 *  -----------------------------------------------------------------------------------------
 *  @author ：   UFO（陳俊融）Github：https://github.com/jyunrong
 *  @date ：   2018/5/15    下午 04:15
 *  @description ：   Activity 的基類
 *  -----------------------------------------------------------------------------------------
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    protected T mPresenter;
    private Activity mCurrentActivity; // 對所有 Activity 進行管理
    public static final List<Activity> mActivities = new LinkedList<Activity>();
    protected Bundle savedInstanceState;
    public PermissionListener mPermissionListener;

    /**
     * 用於創建 Presenter 和判斷是否使用 MVP 模式（由子類實現）
     * @return ??
     */
    protected abstract T createPresenter();

    /**
     * 得到當前界面的佈局文件id（由子類實現）
     * @return R.layout.XXX
     */
    protected abstract int provideContentViewId();

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.savedInstanceState = savedInstanceState;

        // 初始化的時候將其添加到集合中
        synchronized (mActivities) {
            mActivities.add(this);
        }

        mPresenter = createPresenter();

        initBSCV();
        setContentView(provideContentViewId());
        Eyes.translucentStatusBar(this, false);

        initView();
        initData();
        initListener();
    }

    /**
     * 在 setContentView 之前要做的事情
     * ex : Twitter Config 身分驗證
     */
    public void initBSCV() {
    }

    /** 初始化控件（依據Id索引組件） */
    public void initView() {
    }

    /** 初始化數據（加入監聽） */
    public void initData() {
    }

    /** 設置 listener 的操作 */
    public void initListener() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCurrentActivity = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 銷毀的時候從集合中移除
        synchronized (mActivities) {
            mActivities.remove(this);
        }

        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    /** FCM 登入進度條 */
    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    /** 顯示進度條 */
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    /** 隱藏進度條 */
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /** 隱藏鍵盤 */
    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 統一退出控制
     * 雙擊返回鍵退出應用程式
     *
     * @param keyCode   按鍵代碼。
     * @param event         按鍵事件。
     * @return                   回傳true。
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 是否觸發按鍵為back鍵
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else { // 如果不是back鍵正常響應
            return super.onKeyDown(keyCode, event);
        }
    }

    // 退出時間
    private static long currentBackPressedTime = 0;
    // 退出間隔
    private static final int BACK_PRESSED_INTERVAL = 2000;

    @Override
    public void onBackPressed() {
        if (mCurrentActivity instanceof MainActivity) {
            // 如果是主頁面
            // 這里處理邏輯代碼，大家注意：該方法僅適用於2.0或更新版的sdk
            if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL ) {
                currentBackPressedTime = System. currentTimeMillis ();
                Toast.makeText( this, getString(R.string.click_again_to_exit), Toast.LENGTH_SHORT).show();
                return;
            } else {
                // 按下返回鍵回到桌面，再打開不會再看到啟動頁（除非你手動清了後台或者被系統 kill 了）
                // super.onBackPressed(); 	不要调用父类的方法
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                return;
            }
        }
        super.onBackPressed(); // finish()
    }

    public boolean isEventBusRegisted(Object subscribe) {
        return EventBus.getDefault().isRegistered(subscribe);
    }

    public void registerEventBus(Object subscribe) {
        if (!isEventBusRegisted(subscribe)) {
            EventBus.getDefault().register(subscribe);
        }
    }

    public void unregisterEventBus(Object subscribe) {
        if (isEventBusRegisted(subscribe)) {
            EventBus.getDefault().unregister(subscribe);
        }
    }
}
