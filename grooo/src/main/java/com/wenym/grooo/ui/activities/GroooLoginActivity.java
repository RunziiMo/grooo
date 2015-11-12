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

import butterknife.InjectView;

public class GroooLoginActivity extends BaseActivity {

    private MaterialDialog dialog;

    @InjectView(R.id.et_username)
    EditText editText;
    @InjectView(R.id.et_password)
    EditText editText2;
    @InjectView(R.id.btn_login)
    AppCompatButton button;
    @InjectView(R.id.btn_register)
    TextView register;

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
                .cancelable(false)
                .content("正在登录")
                .progress(true, 0).build();

        register.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GroooLoginActivity.this, MyFragmentActivity.class);
                intent.putExtra(ExtraActivityKeys.FRAGMENT.toString(), MyFragmentActivity.regist);
                startActivity(intent);
            }
        });
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
            public void onFailed(String reason) {
                Toasts.show(reason);
            }

            @Override
            public void onError(int statusCode) {
                Toasts.show("用户名或密码错误,错误码" + statusCode);
            }
        });
    }
}
