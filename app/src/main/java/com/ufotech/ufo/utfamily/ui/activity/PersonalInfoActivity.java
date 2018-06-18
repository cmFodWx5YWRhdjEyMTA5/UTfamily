package com.ufotech.ufo.utfamily.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.twitter.sdk.android.core.TwitterCore;
import ufoUI.PowerfulEditText;
import com.ufotech.ufo.utfamily.R;
import com.ufotech.ufo.utfamily.ui.base.BaseActivity;
import com.ufotech.ufo.utfamily.ui.base.BasePresenter;
import com.ufotech.ufo.utfamily.ui.fragment.MineFragment;

import java.lang.reflect.Field;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *  -----------------------------------------------------------------------------------------
 *  @author ：   UFO（陳俊融）Github：https://github.com/jyunrong
 *  @date ：   2018/5/15    下午 06:04
 *  @description ：   個人資訊頁面
 *  -----------------------------------------------------------------------------------------
 */
public class PersonalInfoActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    private ImageView iv_top_bar_start;
    private RelativeLayout rl_avatar_personal_info, rl_firebase_user_name, rl_firebase_user_email, rl_modify_password;
    public static CircleImageView iv_avatar_personal_info;
    private TextView tv_top_bar_title, tv_firebase_user_account, tv_firebase_user_name, tv_firebase_user_phone, tv_firebase_user_email;
    private Switch switch_wechat_account;
    private Button btn_log_out;
    private Intent intent;
    private int max_length = 20;
    private FirebaseUser user;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_personal_info;
    }

    @Override
    public void initView() {
        iv_top_bar_start = (ImageView) findViewById(R.id.iv_top_bar_start);
        rl_avatar_personal_info = (RelativeLayout) findViewById(R.id.rl_avatar_personal_info);
        rl_firebase_user_name = (RelativeLayout) findViewById(R.id.rl_firebase_user_name);
        rl_firebase_user_email = (RelativeLayout) findViewById(R.id.rl_firebase_user_email);
        rl_modify_password = (RelativeLayout) findViewById(R.id.rl_modify_password);
        iv_avatar_personal_info = (CircleImageView) findViewById(R.id.iv_avatar_personal_info);
        tv_top_bar_title = (TextView) findViewById(R.id.tv_top_bar_title);
        tv_top_bar_title.setText(getText(R.string.label_PersonalInfo));
        tv_firebase_user_account = (TextView) findViewById(R.id.tv_firebase_user_account);
        tv_firebase_user_name = (TextView) findViewById(R.id.tv_firebase_user_name);
        tv_firebase_user_phone = (TextView) findViewById(R.id.tv_firebase_user_phone);
        tv_firebase_user_email = (TextView) findViewById(R.id.tv_firebase_user_email);
        switch_wechat_account = (Switch) findViewById(R.id.switch_wechat_account);
        btn_log_out = (Button) findViewById(R.id.btn_log_out);
    }

    @Override
    public void initData() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        /*
         * The user's ID, unique to the Firebase project. Do NOT use this value to
         * authenticate with your backend server, if you have one. Use
         * FirebaseUser.getIdToken() instead.
         **/
        user = mAuth.getCurrentUser();
        // 取得使用者頭像位址
        Uri photoUrl = Objects.requireNonNull(user).getPhotoUrl();
        if (photoUrl != null) {
            Log.d(TAG, "photoUrl : " + photoUrl.toString());
            Glide
                    .with(this)
                    .load(photoUrl)
                    .apply(new RequestOptions().placeholder(R.mipmap.user).error(R.mipmap.ic_launcher_round))
                    .into(iv_avatar_personal_info);
        }
        // 取得使用者 uid
        String uid = user.getUid();
        tv_firebase_user_account.setText(uid);
        // 取得使用者名稱
        String name = user.getDisplayName();
        tv_firebase_user_name.setText(name);
        // 取得使用者電話
        String phone = user.getPhoneNumber();
        tv_firebase_user_phone.setText(phone);
        // 取得使用者 email
        String email = user.getEmail();
        tv_firebase_user_email.setText(email);
    }

    @Override
    public void initListener() {
        iv_top_bar_start.setOnClickListener(this);
        rl_avatar_personal_info.setOnClickListener(this);
        rl_firebase_user_name.setOnClickListener(this);
        rl_firebase_user_email.setOnClickListener(this);
        rl_modify_password.setOnClickListener(this);
        btn_log_out.setOnClickListener(this);
        // 如果使用者有新增頭像才能點擊預覽
        if (user.getPhotoUrl() != null) {
            iv_avatar_personal_info.setOnClickListener(this);
        }

        switch_wechat_account.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    // If the switch button is on

                    // Show the switch button checked status as toast message
                    Toast.makeText(getApplicationContext(),
                            "Switch is on", Toast.LENGTH_SHORT).show();
                }
                else {
                    // If the switch button is off

                    // Show the switch button checked status as toast message
                    Toast.makeText(getApplicationContext(),
                            "Switch is off", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_top_bar_start:
                Log.d(TAG, "返回");
                finish();
                break;
            case R.id.rl_avatar_personal_info:
                startActivity(new Intent(PersonalInfoActivity.this, ImageActivity.class));
                Log.d(TAG, "頭像");
                break;
            case R.id.rl_firebase_user_name:
                // 加入要顯示的 layout 布局
                @SuppressLint("InflateParams")
                final View item = LayoutInflater.from(PersonalInfoActivity.this).inflate(R.layout.change_name_layout, null);
                // 新增 AlertDialog
                AlertDialog.Builder dialog = new AlertDialog.Builder(PersonalInfoActivity.this);

                // initView
                TextInputLayout display_name_layout = (TextInputLayout) item.findViewById(R.id.display_name_layout);
                PowerfulEditText displayName = (PowerfulEditText) item.findViewById(R.id.display_name_edit);
                displayName.setText(user.getDisplayName());
                display_name_layout.setCounterEnabled(true);
                display_name_layout.setCounterMaxLength(max_length);
                display_name_layout.setErrorEnabled(true);

                //建立三個按鈕的監聽式
                DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String name = displayName.getText().toString();

                        /*
                         * Android中的彈出框在被點擊時， 無論點擊哪個按鈕都會關閉視窗。
                         * 但是有的情況下我們不需要立即關閉視窗。下面的情況是彈出框中要求使用者輸入檔案名，
                         * 並在點擊確定時檢查檔案名的合法性， 不合法則提示使用者重新輸入， 彈出框要保持在介面上
                         * 使用反射
                         * */
                        Field field = null;
                        try {
                            field = dialog.getClass().getSuperclass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }

                        //which可以用來分辨是按下哪一個按鈕
                        switch (which) {
                            case Dialog.BUTTON_POSITIVE:
                                // 判斷字數
                                if(TextUtils.isEmpty(name)){ // 不關閉
                                    try {
                                        Objects.requireNonNull(field).set(dialog, false);
                                        dialog.dismiss();
                                        display_name_layout.setError(getString(R.string.plz_input_name));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (name.length() > max_length) { // 不關閉
                                    try {
                                        Objects.requireNonNull(field).set(dialog, false);
                                        dialog.dismiss();
                                        display_name_layout.setError(getString(R.string.string_overflow));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else { // 關閉
                                    try {
                                        Objects.requireNonNull(field).set(dialog, true);
                                        dialog.dismiss();
                                        // 更新姓名
                                        changeName(name);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    dialog.dismiss();
                                }
                                break;
                            case Dialog.BUTTON_NEGATIVE: // 關閉
                                try {
                                    Objects.requireNonNull(field).set(dialog, true);
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                dialog.setView(item);
                dialog.setPositiveButton(R.string.change_name_yes, dialogListener );
                dialog.setNegativeButton(R.string.change_name_no, dialogListener );
                dialog.show();
                Log.d(TAG, "姓名");
                break;
            case R.id.rl_firebase_user_email:
                Log.d(TAG, "修改信箱");
                break;
            case R.id.rl_modify_password:
                changePassword();
                Log.d(TAG, "修改密碼");
                break;
            case R.id.iv_avatar_personal_info:
                startActivity(new Intent(PersonalInfoActivity.this, AvatarActivity.class));
                Log.d(TAG, "頭像圖片");
                break;
            case R.id.btn_log_out:
                mAuth.signOut();
                // Facebook logout
                LoginManager.getInstance().logOut();
                // Twitter sign out
                TwitterCore.getInstance().getSessionManager().clearActiveSession();
                // 回到登入頁面
                intent = new Intent(PersonalInfoActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Log.d(TAG, "登出");
                finish();
                break;
        }
    }

    /**
     * Update user's  name. ( 更新姓名 )
     *
     * @param new_name 新的使用者名稱
     */
    private void changeName(String new_name) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(new_name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PersonalInfoActivity.this, "修改成功",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PersonalInfoActivity.this, "修改:"
                                    + Objects.requireNonNull(task.getException()).toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // Update UI
        new Thread(new Runnable() {
            @Override
            public void run() {
                tv_firebase_user_name.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_firebase_user_name.setText(new_name);
                    }
                });
            }
        }).start();
        // Update other pages.
        MineFragment.updateName(new_name);
    }

    private void changeEmail() {
        AuthCredential credential = EmailAuthProvider.getCredential("abc@gamil.com", "qazxsw");
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.updateEmail("def@gmail.com")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(PersonalInfoActivity.this, "Email 修改成功",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(PersonalInfoActivity.this,
                                                    "修改email:"+ Objects.requireNonNull(task.getException()).toString(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
    }

    private void changePassword() {
        @SuppressLint("InflateParams")
        final View item = LayoutInflater.from(PersonalInfoActivity.this).inflate(R.layout.change_pw_layout, null);
        new AlertDialog.Builder(PersonalInfoActivity.this)
                .setView(item)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText displayName = (EditText) item.findViewById(R.id.new_pw);
                        String newPassword = displayName.getText().toString();
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(PersonalInfoActivity.this, "Password 修改成功",
                                                    Toast.LENGTH_SHORT).show();
                                        } else{
                                            Toast.makeText(PersonalInfoActivity.this,
                                                    "password 修改:"+ Objects.requireNonNull(task.getException()).toString(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                })
                .show();
    }

    private void reAuth() {
        AuthCredential credential = EmailAuthProvider.getCredential("def@gamil.com", "wsxzaq");
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(PersonalInfoActivity.this, "重新認證成功",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void delUser() {
        AuthCredential credential = EmailAuthProvider.getCredential("def@gamil.com", "wsxzaq");
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PersonalInfoActivity.this, "User刪除成功",
                                            Toast.LENGTH_SHORT).show();
                                } else{
                                    Toast.makeText(PersonalInfoActivity.this,
                                            "User刪除:"+ Objects.requireNonNull(task.getException()).toString(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
    }
}
