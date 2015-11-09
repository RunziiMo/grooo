package com.wenym.grooo.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.wenym.grooo.R;
import com.wenym.grooo.provider.ExtraActivityKeys;
import com.wenym.grooo.ui.base.BaseFragment;
import com.wenym.grooo.widgets.Toasts;

import butterknife.InjectView;

public class SuggestFragment extends BaseFragment implements View.OnClickListener {

    @InjectView(R.id.et_suggest)
    MaterialEditText suggest;

    @InjectView(R.id.btn_confirm)
    Button confirm;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_suggest;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String suggestData = suggest.getText().toString().trim();
        if (TextUtils.isEmpty(suggestData)) {
            Toasts.show("输入文本不能为空");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(ExtraActivityKeys.SUGGESTION.toString(), suggestData);

        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
