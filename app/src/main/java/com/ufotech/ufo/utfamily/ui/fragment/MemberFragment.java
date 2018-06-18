package com.ufotech.ufo.utfamily.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ufoUI.colortrackview.ColorTrackTabLayout;
import com.ufotech.ufo.utfamily.R;
import com.ufotech.ufo.utfamily.SuggestFragment;
import com.ufotech.ufo.utfamily.ui.base.BaseFragment;
import com.ufotech.ufo.utfamily.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

public class MemberFragment extends BaseFragment {
    private final String TAG = getName();
    private ColorTrackTabLayout mTabChannel;
    private ViewPager mVpContent;
    private ImageView iv_operation;
    private List<Fragment> pageList = new ArrayList<>();

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_member;
    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        mTabChannel = (ColorTrackTabLayout) view.findViewById(R.id.tab_channel);
        mVpContent = (ViewPager) view.findViewById(R.id.vp_content);
        iv_operation = (ImageView) view.findViewById(R.id.iv_operation);
    }

    @Override
    public void initData() {
        String[] titles = getResources().getStringArray(R.array.channel);
        mTabChannel.setTabPaddingLeftAndRight(20, 20);
        mTabChannel.setSelectedTabIndicatorHeight(0);
        for (int i = 0; i < titles.length; i++) {
            pageList.add(SuggestFragment.newInstance());
        }

        mVpContent.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return pageList.get(position);
            }

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        mTabChannel.setupWithViewPager(mVpContent);

        //默认选中第??个
        mTabChannel.setLastSelectedTabPosition(0);
        //移动到第1个
        mTabChannel.setCurrentItem(0);
        mVpContent.setOffscreenPageLimit(titles.length);
    }

    @Override
    public void initListener() {
//        mTabChannel.setTabPaddingLeftAndRight(UIUtils.dip2Px(10), UIUtils.dip2Px(10));
        mTabChannel.setupWithViewPager(mVpContent);
        mTabChannel.post(new Runnable() {
            @Override
            public void run() {
                //设置最小宽度，使其可以在滑动一部分距离
                ViewGroup slidingTabStrip = (ViewGroup) mTabChannel.getChildAt(0);
                slidingTabStrip.setMinimumWidth(slidingTabStrip.getMeasuredWidth() + iv_operation.getMeasuredWidth());
            }
        });
        //隐藏指示器
        mTabChannel.setSelectedTabIndicatorHeight(0);

        mVpContent.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //当页签切换的时候，如果有播放视频，则释放资源
//                JCVideoPlayer.releaseAllVideos();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void loadData() {

    }
}
