package net.azstudio.groooseller.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import net.azstudio.groooseller.R;
import net.azstudio.groooseller.databinding.FragmentEditBinding;
import net.azstudio.groooseller.provider.ExtraActivityKeys;
import net.azstudio.groooseller.ui.base.BaseFragment;
import net.azstudio.groooseller.utils.Toasts;


public class EditFragment extends BaseFragment<FragmentEditBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnConfirm.setOnClickListener(v -> {
            String suggestData = binding.etSuggest.getText().toString().trim();
            if (TextUtils.isEmpty(suggestData)) {
                Toasts.show("输入文本不能为空");
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(ExtraActivityKeys.EDITCONTENT.toString(), suggestData);

            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        });
    }

}
