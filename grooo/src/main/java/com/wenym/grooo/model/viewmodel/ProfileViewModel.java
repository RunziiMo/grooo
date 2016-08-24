package com.wenym.grooo.model.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.wenym.grooo.http.NetworkWrapper;
import com.wenym.grooo.model.app.Address;
import com.wenym.grooo.model.app.Profile;
import com.wenym.grooo.ui.activities.EditActivity;
import com.wenym.grooo.ui.activities.MainActivity;
import com.wenym.grooo.ui.profile.SchoolActivity;
import com.wenym.grooo.util.AppPreferences;

import rx.Subscription;

/**
 * Created by runzii on 16-8-23.
 */
public class ProfileViewModel extends BaseViewModel {

    public ObservableField<String> avatar = new ObservableField<>();
    public ObservableField<String> nickname = new ObservableField<>();
    public ObservableField<String> schoolName = new ObservableField<>();
    public ObservableField<String> email = new ObservableField<>();
    public ObservableField<String> score = new ObservableField<>();
    public ObservableField<String> building = new ObservableField<>();
    public ObservableField<String> room = new ObservableField<>();

    public ProfileViewModel(Activity activity) {
        super(activity);
        processProfile(AppPreferences.get().getProfile());
        processAddress(AppPreferences.get().getAddress());
        loadInfo();
    }

    public void logout(View view) {
        AppPreferences.get().clearAll();
        activity.finish();
    }

    public void setSchool(View view) {
        Intent intent = new Intent(activity, SchoolActivity.class);
        activity.startActivityForResult(intent, MainActivity.REQUEST_CODE_SCHOOL);
    }

    public void setEmail(View view) {
        EditActivity.lanuch(activity,"请在此输入邮箱","修改邮箱",EditActivity.EMAIL,MainActivity.REQUEST_CODE_EMAIL);
    }

    private void loadInfo() {
        Subscription s = NetworkWrapper.get()
                .getProfile(AppPreferences.get().getProfile().getId())
                .flatMap(profile -> {
                    AppPreferences.get().setProfile(profile);
                    processProfile(profile);
                    return NetworkWrapper.get().getAddress();
                })
                .subscribe(address -> {
                    AppPreferences.get().setAddress(address);
                    processAddress(address);
                }, errorHandle("获取个人信息"));
        addSubscription(s);
    }

    public void setProfile(Profile profile) {
        Subscription s = NetworkWrapper.get()
                .putProfile(profile)
                .subscribe(s1 -> {
                    Log.d("SetProfile", s1);
                    processProfile(profile);
                }, errorHandle("设置个人信息"));
        addSubscription(s);
    }

    public void setAddress(Address address) {
        Subscription s = NetworkWrapper.get()
                .putAddress(address)
                .subscribe(s1 -> {
                    Log.d("setAddress", s1.toString());
                    processAddress(address);
                }, errorHandle("设置地址"));
        addSubscription(s);
    }

    private void processProfile(Profile profile) {
        if (profile == null)
            return;
        nickname.set(profile.getNickname());
        avatar.set(profile.getAvatar());
        schoolName.set(profile.getSchool().getName());
        email.set(profile.getEmail());
        score.set(String.valueOf(profile.getScore()));
    }

    private void processAddress(Address address) {
        if (address == null)
            return;
        building.set(address.getBuilding());
        room.set(address.getAddress());
    }
}
