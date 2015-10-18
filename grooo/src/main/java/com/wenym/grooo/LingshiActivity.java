/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wenym.grooo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wenym.grooo.adapters.LingshiListAdapter;
import com.wenym.grooo.fragments.ProgressFragment;
import com.wenym.grooo.http.model.GetMenuData;
import com.wenym.grooo.http.model.GetMenuSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.model.LingshiClass;
import com.wenym.grooo.model.Menu;
import com.wenym.grooo.widgets.BadgeView;
import com.wenym.grooo.widgets.Toasts;

import java.util.ArrayList;

public class LingshiActivity extends ActionBarActivity {

    public static boolean isChild = false;
    private GridView gridView;
    private LingshiListAdapter adapter;
    private FrameLayout content;
    private TextView order_cart;
    private ImageView order_car;
    private TextView goto_pay;
    private BadgeView buyNumView;
    private TextView shop_introduce;
    private LinearLayout headerView;
    private Toolbar toolbar;
    private float density;
    private FragmentManager manager;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lingshi);

        manager = getSupportFragmentManager();
        ProgressFragment progressFragment = new ProgressFragment();
        Bundle bundle = new Bundle();
        bundle.putString("currTask", "");
        bundle.putBoolean("isShow", true);
        progressFragment.setArguments(bundle);
        manager.beginTransaction().add(R.id.lingshi_container, progressFragment).addToBackStack("Market").commit();

        density = getResources().getDisplayMetrics().density;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        gridView = (GridView) findViewById(R.id.lingshi_class_detail);
        order_cart = (TextView) findViewById(R.id.food_list_shipping_fee);
        order_car = (ImageView) findViewById(R.id.food_list_order_cart);
        goto_pay = (TextView) findViewById(R.id.food_list_take_order_button);

        goto_pay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                MainActivity.handler.sendMessage(MainActivity.handler
                        .obtainMessage(MainActivity.PAY_LINGSHI));
            }
        });
        buyNumView = new BadgeView(this, order_car);
        buyNumView.setBadgePosition(BadgeView.POSITION_CENTER);
        headerView = (LinearLayout) getLayoutInflater().inflate(
                R.layout.restaurant_list_header, null, false);
        shop_introduce = (TextView) headerView
                .findViewById(R.id.headview_introduce);
        shop_introduce.setText("傻逼，又得做接口");
        toolbar.setNavigationIcon(R.drawable.lingshi);
        toolbar.setNavigationOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
        toolbar.setTitle("咕噜超市");
        toolbar.setSubtitle("3号楼小卖部");

        setSupportActionBar(toolbar);
        GetMenuData getMenuData = new GetMenuData();
        getMenuData.setId("99");
        HttpUtils.MakeAPICall(getMenuData, this, new HttpCallBack() {
            @Override
            public void onSuccess(Object object) {
                GetMenuSuccessData getMenuSuccessData = (GetMenuSuccessData) object;
                setListener(getMenuSuccessData.getMenus());
                if (manager != null) {
                    manager.popBackStack();
                }
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onError(int statusCode) {
                Toasts.show("Grooosupermarket " + statusCode);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void setListener(ArrayList<Menu> menus) {
        final LingshiClass lingshiClass = new LingshiClass(menus);


        // initialize your items array
        adapter = new LingshiListAdapter(this, lingshiClass);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final ArrayList<Menu> menu = lingshiClass.getClasses().get(lingshiClass.getClasses().keySet().toArray()[position].toString());
                String[] menus = new String[menu.size()];
                for (int i = 0; i < menus.length; i++) {
                    menus[i] = menu.get(i).getFoodclass();
                }
                new MaterialDialog.Builder(LingshiActivity.this).items(menus)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog,
                                                    View view, int which, CharSequence text) {
//                                getSupportFragmentManager()
//                                        .beginTransaction()
//                                        .setCustomAnimations(
//                                                android.R.anim.slide_in_left,
//                                                android.R.anim.fade_out)
//                                        .add(R.id.lingshi_container,
//                                                childFragment).commit();
                                isChild = true;
                            }
                        }).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (isChild) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .setCustomAnimations(android.R.anim.slide_in_left,
//                            android.R.anim.fade_out).remove(childFragment)
//                    .commit();
            isChild = false;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        HttpUtils.CancelHttpTask();
        super.onDestroy();
    }
}