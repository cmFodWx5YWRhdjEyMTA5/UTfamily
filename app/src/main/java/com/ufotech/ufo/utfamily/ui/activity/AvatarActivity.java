package com.ufotech.ufo.utfamily.ui.activity;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.ufotech.ufo.utfamily.R;
import com.ufotech.ufo.utfamily.ui.base.BaseActivity;
import com.ufotech.ufo.utfamily.ui.base.BasePresenter;

import java.util.Objects;

public class AvatarActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_avatar;
    }

    @Override
    public void initView() {
        ImageView iv_avatar_large = (ImageView) findViewById(R.id.iv_avatar_large);
        Uri photoUrl = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl();
        Glide
                .with(this)
                .load(photoUrl)
                .into(iv_avatar_large);
    }
}
