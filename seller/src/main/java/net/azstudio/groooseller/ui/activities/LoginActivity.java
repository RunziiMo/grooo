package net.azstudio.groooseller.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import net.azstudio.groooseller.R;
import net.azstudio.groooseller.databinding.ActivityLoginBinding;
import net.azstudio.groooseller.http.NetworkWrapper;
import net.azstudio.groooseller.model.app.ShopInfo;
import net.azstudio.groooseller.model.http.AuthToken;
import net.azstudio.groooseller.model.http.AuthUser;
import net.azstudio.groooseller.ui.base.BaseActivity;
import net.azstudio.groooseller.utils.AppManager;
import net.azstudio.groooseller.utils.AppPreferences;

import rx.Subscriber;


public class LoginActivity extends BaseActivity<ActivityLoginBinding>  {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void attemptLogin() {
        // Reset errors.
        binding.etUsername.setError(null);
        binding.etPassword.setError(null);

        // Store values at the time of the login attempt.
        String phone = binding.etPassword.getText().toString();
        String password = this.binding.etPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            cancel = true;
//        }

        if (TextUtils.isEmpty(phone)) {
            Snackbar.make(binding.etUsername, R.string.error_field_required, Snackbar.LENGTH_SHORT).show();
            focusView = binding.etUsername;
            cancel = true;
        }
//        } else if (!isPhoneValid(phone)) {
//            Snackbar.make(username, R.string.error_invalid_phone, Snackbar.LENGTH_SHORT).show();
//            focusView = username;
//            cancel = true;
//        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            final AuthUser user = new AuthUser(phone, password);
            Subscriber<AuthToken> subscriber = new Subscriber<AuthToken>() {
                @Override
                public void onCompleted() {
                    showProgress(false);
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    Snackbar.make(binding.etUsername, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onNext(AuthToken authToken) {
                    ShopInfo info = new ShopInfo();
                    info.setId(authToken.getId());
                    AppManager.setShopInfo(info);
                    AppPreferences.get().setAuthUser(user);
                    AppPreferences.get().setAuth(authToken.getToken());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            };
            NetworkWrapper.get().getAuthToken(subscriber, user);
        }
    }

    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.matches("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            binding.loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
            binding.loginForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    binding.loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            binding.loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            binding.loginProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    binding.loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            binding.loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            binding.loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void onClickLogin(View view) {
        attemptLogin();
    }

    public void onClickContact(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:18716036890"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
