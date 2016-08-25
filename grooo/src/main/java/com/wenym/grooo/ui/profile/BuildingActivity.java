package com.wenym.grooo.ui.profile;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;

import com.afollestad.materialdialogs.MaterialDialog;
import com.runzii.lib.ui.base.BaseActivity;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ActivityRecyclerViewBinding;
import com.wenym.grooo.http.NetworkWrapper;
import com.wenym.grooo.util.AppPreferences;

import java.util.ArrayList;
import java.util.List;

public class BuildingActivity extends BaseActivity<ActivityRecyclerViewBinding> implements BuildingAdapter.OnItemClickListener {

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

    private BuildingAdapter adapter;
    private List<String> buildings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new BuildingAdapter(buildings, this);
        bind.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bind.recyclerView.setAdapter(adapter);

        NetworkWrapper.get().getBuildings(AppPreferences.get().getProfile().getSchool().getId())
                .compose(bindToLifecycle())
                .subscribe(buildings -> {
                    BuildingActivity.this.buildings = buildings;
                    adapter.update(buildings);
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm_edit, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
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
            adapter.update(buildings);
        else {
            List<String> tmpList = new ArrayList<>();
            for (String building : buildings) {
                if (building.toLowerCase().contains(s.toString().toLowerCase()))
                    tmpList.add(building);
            }
            adapter.update(tmpList);
        }
    }


    @Override
    public void onItemClick(String building) {
        if (!TextUtils.isEmpty(building)) {
            new MaterialDialog.Builder(this)
                    .content("确定选择" + building + "为你的楼层吗？")
                    .positiveText(android.R.string.yes)
                    .negativeText(android.R.string.no)
                    .onPositive((dialog, which) -> {
                        Intent intent = new Intent();
                        intent.putExtra("building", building);
                        setResult(RESULT_OK, intent);
                        finish();
                    })
                    .show();
        }
    }
}
