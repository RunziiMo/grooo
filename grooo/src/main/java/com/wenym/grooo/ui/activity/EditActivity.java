package com.wenym.grooo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.runzii.lib.ui.base.BaseActivity;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ActivityEditBinding;
import com.wenym.grooo.ui.fragments.RegisterFragment;
import com.wenym.grooo.util.Toasts;

/**
 * Created by runzii on 15-11-1.
 */
public class EditActivity extends BaseActivity<ActivityEditBinding> {

    public static final String EMAIL = "EMAIL";
    public static final String ROOM = "ROOM";
    public static final String NICK = "NICK";
    public static final String REMARK = "REMARK";

    public static void lanuch(Activity activity, String hint, String title, String editType, int requestCode) {
        Intent intent = new Intent(activity, EditActivity.class);
        intent.putExtra("hint", hint);
        intent.putExtra("edit_type", editType);
        intent.putExtra("title", title);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected boolean isEnableSwipe() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit;
    }

    @Override
    protected boolean isTranslucentStatus() {
        return false;
    }

    @Override
    protected boolean isDisplayHomeAsUp() {
        return true;
    }

    private String editType = EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String hint = intent.getStringExtra("hint");
        editType = intent.getStringExtra("edit_type");
        setTitle(title);
        bind.setHint(hint);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send_msg, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.send_msg)
            confirmEdit();
        return super.onOptionsItemSelected(item);
    }


    public void confirmEdit() {
        String text = bind.etEdit.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            Toasts.show("输入文本不能为空");
            return;
        }
        Intent intent = new Intent();
        switch (editType) {
            case EMAIL:
                if (!text.matches(RegisterFragment.emailPattern)) {
                    Snackbar.make(bind.getRoot(), "请输入正确格式的邮箱地址", Snackbar.LENGTH_SHORT).show();
                    return;
                } else intent.putExtra("edit_result", text);
                break;
            case ROOM:
                intent.putExtra("room", text);
                break;
            case NICK:
                intent.putExtra("nick", text);
                break;
            case REMARK:
                intent.putExtra("remark", text);
                break;
        }
        setResult(RESULT_OK, intent);
        finish();
    }

}
