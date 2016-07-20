package com.wenym.grooo.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.runzii.lib.ui.base.BaseActivity;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ActivityLoginBinding;
import com.wenym.grooo.http.NetworkWrapper;
import com.wenym.grooo.model.app.Profile;
import com.wenym.grooo.model.http.AuthUser;
import com.wenym.grooo.provider.ExtraActivityKeys;
import com.wenym.grooo.util.AppPreferences;
import com.wenym.grooo.util.GroooAppManager;

import rx.Observable;
import rx.functions.Func2;

public class GroooLoginActivity extends BaseActivity<ActivityLoginBinding> {


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

        if (AppPreferences.get().getProfile() != null) {
            startActivity(new Intent(
                    GroooLoginActivity.this,
                    MainActivity.class));
            finish();
            return;
        }

        bind.setLoginForm(new AuthUser());

        Observable.combineLatest(RxTextView.textChanges(bind.etUsername).skip(1)
                , RxTextView.textChanges(bind.etPassword).skip(1)
                , (charSequence, charSequence2) -> !TextUtils.isEmpty(charSequence) && !TextUtils.isEmpty(charSequence2))
                .subscribe(aBoolean -> bind.btnLogin.setEnabled(aBoolean));
    }


    private void Login() {
        NetworkWrapper.get().getAuthToken(bind.getLoginForm())
                .compose(bindToLifecycle())
                .flatMap(authToken -> {
                    GroooAppManager.setAuthToken(authToken.getToken());
                    int id = authToken.getId();
                    return NetworkWrapper.get().getProfile(id);
                }).subscribe(profile -> {
                    GroooAppManager.setProfile(profile);
                    Intent intent = new Intent(GroooLoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                , throwable -> Snackbar.make(bind.loginForm, throwable.getMessage(), Snackbar.LENGTH_SHORT).show()
                , () -> showProgress(false));

    }


    public void startRegist(View view) {

        Intent intent = new Intent(GroooLoginActivity.this, MyFragmentActivity.class);
        intent.putExtra(ExtraActivityKeys.FRAGMENT.toString(), MyFragmentActivity.regist);
        startActivity(intent);
    }

    public void startLogin(View view) {
        // startActivity(new Intent(GroooLoginActivity.this,
        // MainActivity.class));
        // finish();
        showProgress(true);
        Login();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            bind.loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
            bind.loginForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    bind.loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            bind.loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            bind.loginProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    bind.loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            bind.loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            bind.loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
