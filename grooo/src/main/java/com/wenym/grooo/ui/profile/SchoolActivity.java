package com.wenym.grooo.ui.profile;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.runzii.lib.ui.base.BaseActivity;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ActivityRecyclerViewBinding;
import com.wenym.grooo.http.NetworkWrapper;
import com.wenym.grooo.model.app.School;

import java.util.ArrayList;
import java.util.List;

public class SchoolActivity extends BaseActivity<ActivityRecyclerViewBinding> implements SchoolAdapter.OnItemClickListener {

    @Override
    protected boolean isDisplayHomeAsUp() {
        return true;
    }

    @Override
    protected boolean isEnableSwipe() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recycler_view;
    }

    @Override
    protected boolean isTranslucentStatus() {
        return false;
    }

    private SchoolAdapter adapter;
    private List<School> schools = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new SchoolAdapter(schools, this);
        bind.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bind.recyclerView.setAdapter(adapter);

        NetworkWrapper.get().getSchools()
                .compose(bindToLifecycle())
                .subscribe(schools -> {
                    SchoolActivity.this.schools = schools;
                    adapter.update(schools);
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm_edit, menu);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            MenuItem searchItem = menu.findItem(R.id.search);
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String s) {
                    search(s);
                    return true;
                }


                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        } else {
            menu.findItem(R.id.search).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void search(CharSequence s) {
        if (adapter == null)
            return;
        if (TextUtils.isEmpty(s))
            adapter.update(schools);
        else {
            List<School> tmpList = new ArrayList<>();
            for (School school : schools) {
                if (school.getName().toLowerCase().contains(s.toString().toLowerCase()))
                    tmpList.add(school);
            }
            adapter.update(tmpList);
        }
    }


    @Override
    public void onItemClick(School school) {
        if (school != null) {
            new MaterialDialog.Builder(this)
                    .content("确定选择" + school.getName() + "为你的学校吗？")
                    .positiveText(android.R.string.yes)
                    .negativeText(android.R.string.no)
                    .onPositive((dialog, which) -> {
                        Intent intent = new Intent();
                        intent.putExtra("school", school);
                        setResult(RESULT_OK, intent);
                        finish();
                    })
                    .show();
        }
    }
}
