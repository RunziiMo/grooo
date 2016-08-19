package com.wenym.grooo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.runzii.lib.ui.base.BaseActivity;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ActivityCommentBinding;
import com.wenym.grooo.model.http.CommentForm;

/**
 * Created by runzii on 15-11-1.
 */
public class CommentActivity extends BaseActivity<ActivityCommentBinding> {

    @Override
    protected boolean isEnableSwipe() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comment;
    }

    @Override
    protected boolean isTranslucentStatus() {
        return false;
    }

    @Override
    protected boolean isDisplayHomeAsUp() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        CommentForm commentForm = new CommentForm();
        if (!TextUtils.isEmpty(title))
            getSupportActionBar().setTitle(title);
        String id = intent.getStringExtra("order_id");
        if (!TextUtils.isEmpty(id))
            commentForm.setOrder_id(id);
        bind.setComment(commentForm);

        RxTextView.textChanges(bind.etComment)
                .map(charSequence -> 140 - charSequence.length())
                .subscribe(integer -> bind.tvAbleCount
                        .setText(String.format(getString(R.string.commentFormat), integer)));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send_msg, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.send_msg)
            completeComment();
        return super.onOptionsItemSelected(item);
    }

    public void completeComment() {
        CommentForm form = bind.getComment();
        if (form == null || TextUtils.isEmpty(form.getRating_remark())) {
            Snackbar.make(bind.coordinatorLayout, "订单评论为空", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (form.getOrder_id() == null) {
            Snackbar.make(bind.coordinatorLayout, "缺少所评价的订单id", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (form.getRating() == 0) {
            Snackbar.make(bind.coordinatorLayout, "你不能给0分", Snackbar.LENGTH_SHORT).show();
            return;
        }
        Log.e("completeComment", form.toString());

        Intent intent = getIntent();
        intent.putExtra("comment", form);
        setResult(RESULT_OK, intent);
        finish();
    }
}
