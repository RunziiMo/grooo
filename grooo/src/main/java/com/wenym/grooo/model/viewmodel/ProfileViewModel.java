package com.wenym.grooo.model.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import com.wenym.grooo.http.NetworkWrapper;
import com.wenym.grooo.model.app.Address;
import com.wenym.grooo.model.app.Profile;
import com.wenym.grooo.ui.activity.EditActivity;
import com.wenym.grooo.ui.activity.MainActivity;
import com.wenym.grooo.ui.profile.BuildingActivity;
import com.wenym.grooo.util.AppPreferences;
import com.wenym.grooo.util.Toasts;

import cn.jpush.android.api.JPushInterface;
import rx.Observable;
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
        Profile profile = AppPreferences.get().getProfile();
        processProfile(profile);
        processAddress(AppPreferences.get().getAddress());
        //如果推送id和本机不一样就先设置推送id
        profile.setPush_id(JPushInterface.getRegistrationID(activity));
        NetworkWrapper.get().putProfile(profile)
                .flatMap(s -> loadInfo())
                .subscribe(address -> {
                    AppPreferences.get().setAddress(address);
                    processAddress(address);
                }, errorHandle("获取个人信息"));
    }

    public void logout(View view) {
        AppPreferences.get().clearAll();
        activity.finish();
    }

    public void setNick(View view) {
        EditActivity.lanuch(activity, "请在此输入昵称", "修改昵称", EditActivity.NICK, MainActivity.REQUEST_CODE_NICK);
    }

    public void setSchool(View view) {
        Toasts.show("暂不支持更改学校");
//        Intent intent = new Intent(activity, SchoolActivity.class);
//        activity.startActivityForResult(intent, MainActivity.REQUEST_CODE_SCHOOL);
    }

    public void setEmail(View view) {
        EditActivity.lanuch(activity, "请在此输入邮箱", "修改邮箱", EditActivity.EMAIL, MainActivity.REQUEST_CODE_EMAIL);
    }

    public void setBuilding(View view) {
        Intent intent = new Intent(activity, BuildingActivity.class);
        activity.startActivityForResult(intent, MainActivity.REQUEST_CODE_BUILDING);
    }

    public void setRoom(View view) {
        EditActivity.lanuch(activity, "请在此输入寝室号", "修改寝室号", EditActivity.ROOM, MainActivity.REQUEST_CODE_ROOM);
    }

    private Observable<Address> loadInfo() {
        return NetworkWrapper.get()
                .getProfile(AppPreferences.get().getProfile().getId())
                .flatMap(profile -> {
                    AppPreferences.get().setProfile(profile);
                    processProfile(profile);
                    return NetworkWrapper.get().getAddress();
                });
    }

    public void setProfile(Profile profile) {
        Subscription s = NetworkWrapper.get()
                .putProfile(profile)
                .subscribe(s1 -> {
                    Log.d("SetProfile", s1);
                    processProfile(profile);
                    AppPreferences.get().setProfile(profile);
                }, errorHandle("设置个人信息"));
        addSubscription(s);
    }

    public void setAddress(Address address) {
        Subscription s = NetworkWrapper.get()
                .putAddress(address)
                .subscribe(s1 -> {
                    Log.d("setAddress", s1);
                    processAddress(address);
                    AppPreferences.get().setAddress(address);
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
