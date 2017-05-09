package net.azstudio.groooseller.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;

import net.azstudio.groooseller.R;
import net.azstudio.groooseller.provider.ExtraActivityKeys;
import net.azstudio.groooseller.ui.base.BaseFragment;
import net.azstudio.groooseller.utils.Toasts;

import butterknife.BindView;
import butterknife.OnClick;


public class EditFragment extends BaseFragment {

    @BindView(R.id.et_suggest)
    MaterialEditText suggest;

    @BindView(R.id.btn_confirm)
    Button confirm;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.btn_confirm)
    void onClick(View v) {
        String suggestData = suggest.getText().toString().trim();
        if (TextUtils.isEmpty(suggestData)) {
            Toasts.show("输入文本不能为空");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(ExtraActivityKeys.EDITCONTENT.toString(), suggestData);

        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
