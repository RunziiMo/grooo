package com.wenym.grooo.ui.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ArrayAdapter;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.FragmentRegisterBinding;
import com.wenym.grooo.http.NetworkWrapper;
import com.wenym.grooo.model.app.School;
import com.wenym.grooo.model.http.RegistForm;
import com.wenym.grooo.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

public class RegisterFragment extends BaseFragment {

    private static final String phonePattern = "^1[0-9]{10}$";
    private static final String emailPattern = "[a-zA-Z0-9_]+@.+([a-zA-Z]+){2,5}";
    private static final String SCHOOLS = "SCHOOLS";

    private FragmentRegisterBinding binding;

    private ArrayList<School> schools = new ArrayList<>();

    private ObservableBoolean enable = new ObservableBoolean(false);
    private RegistForm form = new RegistForm();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            schools = savedInstanceState.getParcelableArrayList(SCHOOLS);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SCHOOLS, schools);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = DataBindingUtil.bind(view);
        binding.setRegistForm(form);

        int layoutItemId = android.R.layout.simple_dropdown_item_1line;
        ArrayAdapter<School> adapter = new ArrayAdapter<>(getContext(), layoutItemId, schools);
        binding.etSchool.setAdapter(adapter);

        if (schools.size() == 0) {
            Subscription s = NetworkWrapper.get().getSchools()
                    .subscribe(schools1 -> {
                        schools = schools1;
                        adapter.notifyDataSetChanged();
                    });
            addSubscription(s);
        }

        Observable<String> phoneOb = RxTextView.textChanges(binding.etPhone).skip(1).map(charSequence -> charSequence.toString());
        Observable<String> passwordOb = RxTextView.textChanges(binding.etPassword).skip(1).map(charSequence -> charSequence.toString());
        Observable<String> confirmPasswordOb = RxTextView.textChanges(binding.etConfirmPassword).skip(1).map(charSequence -> charSequence.toString());
        Observable<School> schoolOb = RxTextView.textChanges(binding.etSchool).skip(1).map(charSequence -> schools.get(binding.etSchool.getListSelection()));
        Observable.combineLatest(phoneOb, passwordOb, confirmPasswordOb, schoolOb,
                (phone, password, confirmPassword, school) -> {
                    binding.etPhone.setError(null);
                    binding.etPassword.setError(null);
                    binding.etSchool.setError(null);
                    if (!phone.matches(phonePattern)) {
                        binding.etPhone.setError("手机号格式不正确");
                        return false;
                    } else if (!password.equals(confirmPassword)) {
                        binding.etConfirmPassword.setError("两次输入密码不一样");
                    } else if (school == null) {
                        binding.etSchool.setError("必须得选择学校");
                        return false;
                    }
                    binding.getRegistForm().setSchool_id(school.getId());
                    return true;
                }).subscribe(aBoolean -> {
            enable.set(aBoolean);
        });

        Subscription regist = RxView.clicks(binding.btnRegist)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> regist(binding.getRegistForm()));
        addSubscription(regist);
    }

    private void regist(RegistForm registForm) {
        Subscription s = NetworkWrapper.get().regist(registForm)
                .subscribe(s1 -> Snackbar.make(getView(), s1, Snackbar.LENGTH_SHORT).show());
        addSubscription(s);
    }

}
