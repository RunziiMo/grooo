package com.wenym.grooo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.Button;
import com.wenym.grooo.GroooApplication;
import com.wenym.grooo.R;
import com.wenym.grooo.http.model.FetchKuaidiData;
import com.wenym.grooo.http.model.FetchKuaidiSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.utils.SharedPreferencesUtil;
import com.wenym.grooo.utils.SmallTools;
import com.wenym.grooo.widgets.Toasts;

public class FetchKuaidiFragment extends Fragment {

    private View fetchLayout;
    private Button fecthButton;
    private MaterialEditText[] editTexts = new MaterialEditText[7];
    private int[] editTextIds = {R.id.kuaidi_company, R.id.kuaidi_groupid,
            R.id.kuaidi_who, R.id.kuaidi_phone, R.id.kuaidi_building,
            R.id.kuaidi_roomnumber, R.id.kuaidi_remark};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fetchLayout = inflater.inflate(
                R.layout.fragment_fetchkuaidi, container, false);
        fecthButton = (Button) fetchLayout
                .findViewById(R.id.btn_confirmfetch);
        for (int i = 0; i < editTexts.length; i++) {
            editTexts[i] = (MaterialEditText) fetchLayout
                    .findViewById(editTextIds[i]);
        }
        editTexts[4].setText(SharedPreferencesUtil.getUserBuilding());
        editTexts[4].setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new MaterialDialog.Builder(getActivity())
                            .items(SmallTools.toArrayBuildings(GroooApplication.buildings))
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog,
                                                        View view, int which, CharSequence text) {
                                    editTexts[4].setText(text);
                                }
                            }).show();
                }
            }
        });
        fecthButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String[] vaules = new String[7];
                for (int i = 0; i < vaules.length; i++) {
                    vaules[i] = editTexts[i].getText().toString().trim();
                }
                for (int i = 0; i < vaules.length - 1; i++) {
                    if ("".equals(vaules[i])) {
                        editTexts[i].requestFocus();
                        editTexts[i].setError("请输入必要信息！");
                        return;
                    }
                }
                if (!vaules[3].matches(RegisterFragment.phonePattern)) {
                    editTexts[3].requestFocus();
                    editTexts[3].setError("请输入正确格式的手机号！");
                    return;
                }
                FetchKuaidiData fetchKuaidiData = new FetchKuaidiData();
                fetchKuaidiData.setBuildingNum(SmallTools.buildingtextToId(GroooApplication.buildings, vaules[4]));
                fetchKuaidiData.setCompanyName(vaules[0]);
                fetchKuaidiData.setTophonenumber(vaules[3]);
                fetchKuaidiData.setPid(vaules[1]);
                fetchKuaidiData.setRemark(vaules[6]);
                fetchKuaidiData.setRoomNum(vaules[5]);
                fetchKuaidiData.setToname(vaules[2]);
                HttpUtils.MakeAPICall(fetchKuaidiData, getActivity(), new HttpCallBack() {

                    @Override
                    public void onSuccess(Object object) {
                        FetchKuaidiSuccessData fetchKuaidiSuccessData = (FetchKuaidiSuccessData) object;
                        Toasts.show(fetchKuaidiSuccessData.getInfo());
                        getActivity().onBackPressed();
                    }

                    @Override
                    public void onFailed() {

                    }

                    @Override
                    public void onError(int statusCode) {
                        Toasts.show("fetchkuaidi " + statusCode);
                    }
                });
            }
        });
        return fetchLayout;
    }
}
