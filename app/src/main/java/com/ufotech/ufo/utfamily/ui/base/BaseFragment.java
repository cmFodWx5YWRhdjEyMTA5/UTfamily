package com.ufotech.ufo.utfamily.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.github.nukc.stateview.StateView;
import com.ufotech.ufo.utfamily.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 *  -----------------------------------------------------------------------------------------
 *  @author ：   UFO（陳俊融）Github：https://github.com/jyunrong
 *  @date ：   2018/5/15    下午 07:50
 *  @description ：   Fragment 的基類
 *  -----------------------------------------------------------------------------------------
 */
public abstract class BaseFragment<T extends BasePresenter> extends LazyLoadFragment {

    protected T mPresenter;
    private View rootView;
    protected StateView mStateView; // 用於顯示加載中、網絡異常，空佈局、內容佈局
    protected Activity mActivity;

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

    /**
     * 初始化Bundle
     * @param bundle 夾帶參數
     */
    protected abstract void getBundle(Bundle bundle);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundle(getArguments());
        mPresenter = createPresenter();
    }

    /**
     * Inflate the title_layout for this fragment（）
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(provideContentViewId(),container,false);
            ButterKnife.bind(this, rootView);

            mStateView = StateView.inject(getStateViewRoot());
            if (mStateView != null){
                mStateView.setLoadingResource(R.layout.page_loading);
                mStateView.setRetryResource(R.layout.page_net_error);
            }

            initView(rootView, savedInstanceState);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    /** StateView的根佈局，默認是整個界面，如果需要變換可以重寫此方法 */
    public View getStateViewRoot() {
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    /**
     * 初始化控件（依據Id索引組件）
     * @param rootView 根布局
     * @param savedInstanceState
     */
    public void initView(View rootView, Bundle savedInstanceState) {
    }

    /** 初始化數據（加入監聽） */
    public void initData() {
    }

    /** 設置 listener 的操作 */
    public void initListener() {
    }

    /** 當第一次可見的時候，加載數據 */
    @Override
    protected void onFragmentFirstVisible() {
        loadData();
    }

    /** 只有第一次進入時會加載數據，比 initData( ) 更早被執行 */
    protected abstract void loadData();

    /**
     * 獲得全局的，防止使用getActivity（）為空
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        rootView = null;
    }

    /**
     * 獲取當前 Fragment 類名
     *
     * @return 類名字符串
     */
    protected String getName() {
        return getClass().getSimpleName();
    }

    /** 隱藏鍵盤 */
    protected void hideKeyBoard() {
        View view = mActivity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
