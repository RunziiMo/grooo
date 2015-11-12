package com.wenym.grooo.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wenym.grooo.R;
import com.wenym.grooo.http.model.RegistData;
import com.wenym.grooo.http.model.RegistSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.ui.base.BaseFragment;
import com.wenym.grooo.utils.GroooAppManager;
import com.wenym.grooo.utils.SmallTools;
import com.wenym.grooo.widgets.Toasts;

import butterknife.InjectView;

public class RegisterFragment extends BaseFragment {

    public static String phonePattern = "^1[0-9]{10}$";
    public static String emailPattern = "[a-zA-Z0-9_]{6,12}+@[a-zA-Z]+(\\.[a-zA-Z]+){1,3}";
    @InjectView(R.id.btn_confirmfetch)
    Button confirmRegister;
    @InjectView(R.id.register_phonenumber)
    MaterialEditText phone;
    @InjectView(R.id.register_building)
    MaterialEditText building;
    @InjectView(R.id.register_roomnumber)
    MaterialEditText roomnumber;
    @InjectView(R.id.register_email)
    MaterialEditText email;
    @InjectView(R.id.register_password)
    MaterialEditText password;
    @InjectView(R.id.register_password_confirm)
    MaterialEditText confirmpassword;
    private StringBuilder sb;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        building.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new MaterialDialog.Builder(getActivity())
                            .items(SmallTools.toArrayBuildings(GroooAppManager.getBuildings()))
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog,
                                                        View view, int which, CharSequence text) {
                                    building.setText(text);
                                }
                            }).show();
                }
            }
        });
        confirmRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getData()) {
                    RegistData registData = new RegistData();
                    registData.setRegistdata(sb.toString());
                    HttpUtils.MakeAPICall(registData, getActivity(), new HttpCallBack() {
                        @Override
                        public void onSuccess(Object object) {
                            RegistSuccessData registSuccessData = (RegistSuccessData) object;
                            Toasts.show(registSuccessData.getInfo());
                        }

                        @Override
                        public void onFailed(String reason) {
                            Toasts.show(reason);
                        }

                        @Override
                        public void onError(int statusCode) {
                            Toasts.show("Registe " + statusCode);
                        }
                    });
                }
            }
        });
    }

    private boolean getData() {
        sb = new StringBuilder();
        String string = phone.getText().toString().trim();
        if (string.matches(phonePattern)) {
            sb.append("username=" + string);
        } else {
            phone.setError("请填写正确的手机号");
            phone.requestFocus();
            return false;
        }
        string = email.getText().toString().trim();
        if (string.matches(emailPattern)) {
            sb.append("&email=" + string);
        } else {
            email.setError("请填写正确的邮箱");
            email.requestFocus();
            return false;
        }
        string = building.getText().toString().trim();
        if (!"".equals(string)) {
            sb.append("&buildingNum=" + SmallTools.buildingtextToId(GroooAppManager.getBuildings(), string));
        } else {
            building.setError("请选择楼层");
            building.requestFocus();
            return false;
        }
        string = roomnumber.getText().toString().trim();
        if (!"".equals(string)) {
            sb.append("&roomNum=" + string);
        } else {
            roomnumber.setError("请填写宿舍号");
            roomnumber.requestFocus();
            return false;
        }
        string = password.getText().toString().trim();
        if (string.length() >= 4
                && string.equals(confirmpassword.getText().toString().trim())) {
            sb.append("&password1=" + string);
            sb.append("&password2=" + string);
        } else {
            password.setText("");
            confirmpassword.setText("");
            password.setError("两次密码不一致");
            password.requestFocus();
            return false;
        }
        return true;
    }
}
