package com.wenym.grooo.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wenym.grooo.R;
import com.wenym.grooo.provider.ExtraActivityKeys;
import com.wenym.grooo.ui.fragments.RegisterFragment;
import com.wenym.grooo.http.model.LoginData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.utils.PreferencesUtil;
import com.wenym.grooo.utils.UpdateAppManager;
import com.wenym.grooo.ui.base.BaseActivity;
import com.wenym.grooo.widgets.Toasts;

public class GroooLoginActivity extends BaseActivity {

    public static final int LOGIN_SUCCESS = 10;
    public static final int LOGIN_FAILED = 11;

    public static final int SHOW_LOGIN = 20;

    public static final int HAS_UPDATE = 30;
    public static final int NO_UPDATE = 31;

    public static final int REGISTER = 40;

    public static final int NO_INTERNET = 50;

    private MaterialDialog dialog;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;
    private EditText editText, editText2;
    private AppCompatButton button;
    private SwitchCompat check1, check2;
    private TextView register;

    @Override
    protected boolean isHideNavigationBar() {
        return false;
    }

    @Override
    protected boolean isDisplayHomeAsUp() {
        return false;
    }

    @Override
    protected boolean isEnableSwipe() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (PreferencesUtil.getInstance().getAppUser() != null) {
            startActivity(new Intent(
                    GroooLoginActivity.this,
                    MainActivity.class));
            finish();
            return;
        }

        dialog = new MaterialDialog.Builder(this)
                .content("正在登录")
                .progress(true, 0).build();

        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        editor = mSharedPreferences.edit();
        check1 = (SwitchCompat) findViewById(R.id.switchView);
        check2 = (SwitchCompat) findViewById(R.id.switchView1);
        editText = (EditText) findViewById(R.id.et_username);
        editText2 = (EditText) findViewById(R.id.et_password);
        register = (TextView) findViewById(R.id.btn_register);
        register.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GroooLoginActivity.this, MyFragmentActivity.class);
                intent.putExtra(ExtraActivityKeys.FRAGMENT.toString(), MyFragmentActivity.regist);
                startActivity(intent);
            }
        });
        check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("isRememberPassword", isChecked);
                editor.commit();
            }
        });
        check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                check1.setChecked(isChecked);
                editor.putBoolean("isAutoLogin", isChecked);
                editor.putBoolean("isRememberPassword", isChecked);
                editor.commit();
            }
        });
        if (mSharedPreferences.getBoolean("isAutoLogin", false)) {
            check2.setChecked(true);
            Login(mSharedPreferences.getString("username", null),
                    mSharedPreferences.getString("password", null));
        }
        if (mSharedPreferences.getBoolean("isRememberPassword", false)) {
            check1.setChecked(true);
            editText.setText(mSharedPreferences.getString("username", null));
            editText2.setText(mSharedPreferences.getString("password", null));
        }
        button = (AppCompatButton) findViewById(R.id.btn_login);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // startActivity(new Intent(GroooLoginActivity.this,
                // MainActivity.class));
                // finish();
                String username = editText.getText().toString();
                String password = editText2.getText().toString();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toasts.show("用户名或密码不能为空");
                } else {
                    Login(username, password);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (dialog.isShowing()) {
            HttpUtils.CancelHttpTask();
            Toasts.show("登录被取消");
            dialog.dismiss();
        }
        super.onBackPressed();
    }

    private void Login(String username, String password) {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        LoginData loginData = new LoginData(username, password);
        HttpUtils.MakeAPICall(loginData, this, new HttpCallBack() {
            @Override
            public void onSuccess(Object object) {
                startActivity(new Intent(
                        GroooLoginActivity.this,
                        MainActivity.class));
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                finish();
            }

            @Override
            public void onFailed() {
                Toasts.show("用户名或密码错误");
            }

            @Override
            public void onError(int statusCode) {
                Toasts.show("Login " + statusCode);
            }
        });
    }
}
