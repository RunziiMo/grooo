package com.wenym.grooo.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wenym.grooo.R;
import com.wenym.grooo.http.model.FetchKuaidiData;
import com.wenym.grooo.http.model.FetchKuaidiSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.model.app.AppUser;
import com.wenym.grooo.ui.base.BaseFragment;
import com.wenym.grooo.utils.GroooAppManager;
import com.wenym.grooo.utils.SmallTools;
import com.wenym.grooo.widgets.Toasts;

import butterknife.InjectView;

public class FetchKuaidiFragment extends BaseFragment {

    @InjectView(R.id.btn_confirmfetch)
    Button fecthButton;
    private MaterialEditText[] editTexts = new MaterialEditText[7];
    private int[] editTextIds = {R.id.kuaidi_company, R.id.kuaidi_groupid,
            R.id.kuaidi_who, R.id.kuaidi_phone, R.id.kuaidi_building,
            R.id.kuaidi_roomnumber, R.id.kuaidi_remark};

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fetchkuaidi;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (int i = 0; i < editTexts.length; i++) {
            editTexts[i] = (MaterialEditText) findViewById(editTextIds[i]);
        }
        AppUser appUser = GroooAppManager.getAppUser();
        editTexts[4].setText(appUser.getUserBuilding());
        editTexts[4].setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new MaterialDialog.Builder(getActivity())
                            .items(SmallTools.toArrayBuildings(GroooAppManager.getBuildings()))
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
                fetchKuaidiData.setBuildingNum(SmallTools.buildingtextToId(GroooAppManager.getBuildings(), vaules[4]));
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
    }

}
