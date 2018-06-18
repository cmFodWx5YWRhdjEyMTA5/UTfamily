package com.ufotech.ufo.utfamily.ui.activity;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ufoUI.RZIndicatorView;
import com.ufotech.ufo.utfamily.R;
import com.ufotech.ufo.utfamily.ui.base.BaseActivity;
import com.ufotech.ufo.utfamily.ui.base.BasePresenter;

/**
 *  -----------------------------------------------------------------------------------------
 *  @author ：   UFO（陳俊融）Github：https://github.com/jyunrong
 *  @date ：   2018/5/15    下午 09:00
 *  @description ：   導覽頁面，第一次啟動APP時進入，結束後跳轉至登入頁面
 *  -----------------------------------------------------------------------------------------
 */
public class OnboardingActivity extends BaseActivity implements View.OnClickListener {
    /**
     * Material Design Onboarding
     */
    private final String TAG = this.getClass().getSimpleName();
    private ViewPager mViewPager;
    private TextView mTextSkip, mTextNext;
    private RZIndicatorView mRZIndicatorView;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Intent mIntent;
    // 設定過渡顏色的Array，數量是總頁數+1
    private int[] colors = {Color.parseColor("#ff9a96"), Color.parseColor("#74ceff"), Color.parseColor("#f1ce40"),
            Color.parseColor("#5b183f"), Color.parseColor("#ddeba1")};

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_onboarding;
    }

    @Override
    public void initView() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mTextSkip = (TextView) findViewById(R.id.mTextSkip);
        mTextNext = (TextView) findViewById(R.id.mTextNext);
        mTextSkip.setOnClickListener(this);
        mTextNext.setOnClickListener(this);
        // 自定義的Indicator View
        mRZIndicatorView = (RZIndicatorView) findViewById(R.id.mRZIndicatorView);
        mRZIndicatorView.setIndiCount(3);
        mRZIndicatorView.setIndicatorColor(Color.WHITE);
        mRZIndicatorView.setCurPositon(0, true);
        mIntent = new Intent(OnboardingActivity.this, LoginActivity.class);
    }

    @Override
    public void initListener() {
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /**
                 * 使用顏色過渡類別 ArgbEvaluator()
                 *
                 * 3個參數分別為
                 * 1. fraction : 過渡的起始&結束值
                 * 2. startValue : 起始的顏色
                 * 3. endValue : 結束的顏色
                 */
                // 這裡的結束顏色，就是下一頁的顏色。然而在最後一頁時，如果顏色Array數量沒+1，就會crash。也就是顏色Array數量要+1的原因
                int colorUpdate = (int) new ArgbEvaluator().evaluate(positionOffset, colors[position], colors[position + 1]);
                // 又或者去判斷是否為最後一頁，如果是就把顏色指定為第一頁的顏色
                //int colorUpdate = (int) new ArgbEvaluator().evaluate(positionOffset, colors[position],
                //colors[position + 1 == mViewPager.getChildCount() ? 0 : position + 1]);

                // 將過渡顏色設定為ViewPager的背景色
                mViewPager.setBackgroundColor(colorUpdate);
                // 當SDK >= 21時，將狀態列一併改變顏色
                if (Build.VERSION.SDK_INT >= 21) {
                    getWindow().setStatusBarColor(colorUpdate);
                }
            }

            @Override
            public void onPageSelected(int position) {
                // 對應每個頁面時，textView就做相對應處理。並將IndicatorView的顯示，指定該頁面
                mTextSkip.setVisibility(position > 1 ? View.GONE : View.VISIBLE);
                mTextNext.setText(position > 1 ? "START" : "NEXT");
                mRZIndicatorView.setCurPositon(position, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mTextSkip:
                Log.d(TAG, "跳過");
                startActivity(mIntent);
                finish();
                break;
            case R.id.mTextNext:
                int position = mViewPager.getCurrentItem();
                if (position == 0 || position == 1) {
                    mViewPager.setCurrentItem(position + 1);
                } else {
                    Log.d(TAG, "下頁");
                    startActivity(mIntent);
                    finish();
                }
                break;
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        // 每頁圖片
        private int[] logos = {R.drawable.ic_twitter, R.drawable.ic_chrome, R.drawable.ic_android};

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_onboarding, container, false);

            ImageView mImgView = (ImageView) rootView.findViewById(R.id.mImgView);
            mImgView.setImageResource(logos[getArguments().getInt(ARG_SECTION_NUMBER)]);

            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
