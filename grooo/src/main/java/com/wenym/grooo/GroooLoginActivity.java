package com.wenym.grooo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.rey.material.widget.Button;
import com.rey.material.widget.Switch;
import com.wenym.grooo.fragments.RegisterFragment;
import com.wenym.grooo.http.model.LoginData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.utils.SharedPreferencesUtil;
import com.wenym.grooo.utils.UpdateAppManager;
import com.wenym.grooo.widgets.Toasts;

public class GroooLoginActivity extends FragmentActivity {

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
    private Button button;
    private Switch check1, check2;
    private TextView register;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SharedPreferencesUtil.getUsername() != null
                && !"".equals(SharedPreferencesUtil.getUsername())) {
            startActivity(new Intent(
                    GroooLoginActivity.this,
                    MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);
        initSystemBar();

        dialog = new MaterialDialog.Builder(this)
                .content("正在登录")
                .progress(true, 0).build();
        new UpdateAppManager(this).checkUpdateInfo();

        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        editor = mSharedPreferences.edit();
        check1 = (Switch) findViewById(R.id.switchView);
        check2 = (Switch) findViewById(R.id.switchView1);
        editText = (EditText) findViewById(R.id.et_username);
        editText2 = (EditText) findViewById(R.id.et_password);
        register = (TextView) findViewById(R.id.btn_register);
        register.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.abc_fade_in,
                                R.anim.abc_fade_out)
                        .replace(R.id.container, new RegisterFragment())
                        .addToBackStack(null).commit();
            }
        });
        check1.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch aSwitch, boolean b) {
                editor.putBoolean("isRememberPassword", b);
                editor.commit();
            }
        });
        check2.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch aSwitch, boolean b) {
                check1.setChecked(b);
                editor.putBoolean("isAutoLogin", b);
                editor.putBoolean("isRememberPassword", b);
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
        button = (Button) findViewById(R.id.btn_login);
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

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        }
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
