package com.wenym.grooo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.wenym.grooo.fragments.RestaurantListFragment;
import com.wenym.grooo.http.model.GetRestaurantData;
import com.wenym.grooo.http.model.GetRestaurantSuccessData;
import com.wenym.grooo.http.model.InitRestaurantData;
import com.wenym.grooo.http.model.InitRestaurantSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.model.Restaurant;
import com.wenym.grooo.widgets.Toasts;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Wouldyou on 2015/6/4.
 */
public class RestaurantListActivity extends AppCompatActivity {
    private static final int PROFILE_SETTING = 1;

    private MaterialViewPager mViewPager;
    private Toolbar toolbar;
    private ArrayList<Restaurant> restaurants;
    private String[] titles;
    private boolean isWaiMai;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takeorder);
        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        //it's a sample ViewPagerAdapter
        toolbar = mViewPager.getToolbar();


        isWaiMai = getIntent().getBooleanExtra("isWaiMai", true);

        if (!isWaiMai) {
            toolbar.setTitle(R.string.grooo_preorder);
        }

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }

        HttpUtils.MakeAPICall(new InitRestaurantData(), this, new HttpCallBack() {
            @Override
            public void onSuccess(Object object) {
                InitRestaurantSuccessData initRestaurantSuccessData = (InitRestaurantSuccessData) object;
                titles = initRestaurantSuccessData.getTitles();
                getRestaurants(savedInstanceState);
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onError(int statusCode) {
                Toasts.show("Restaurantlsit " + statusCode);
            }
        });


    }

    private void getRestaurants(Bundle savedInstanceState) {

        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("restaurants") != null) {
            restaurants = savedInstanceState.getParcelableArrayList("restaurants");
            setPagerAdapter();
            Toasts.show("yo");
            return;
        }

        HttpUtils.MakeAPICall(new GetRestaurantData(), this, new HttpCallBack() {
            @Override
            public void onSuccess(Object object) {
                GetRestaurantSuccessData restaurantSuccessData = (GetRestaurantSuccessData) object;
                restaurants = restaurantSuccessData.getRestaurants();
                setPagerAdapter();
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onError(int statusCode) {
                Toasts.show("Restaurant " + statusCode);
            }
        });

    }

    private void setPagerAdapter() {
        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            int oldPosition = -1;

            @Override
            public Fragment getItem(int position) {
                ArrayList<Restaurant> temps = new ArrayList<Restaurant>();
                for (int i = 0; i < restaurants.size(); i++) {
                    if (isWaiMai) {
                        if (titles[position].equals(restaurants.get(i).getClass1()) && !"1".equals(restaurants.get(i).getDeliveryClass()))
                            temps.add(restaurants.get(i));
                    } else {
                        if (titles[position].equals(restaurants.get(i).getClass1()) && !"0".equals(restaurants.get(i).getDeliveryClass()))
                            temps.add(restaurants.get(i));
                    }
                }
                return RestaurantListFragment.newInstance().setRestaurants(temps).setIsWaiMai(isWaiMai);
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                super.setPrimaryItem(container, position, object);

                //only if position changed
                if (position == oldPosition)
                    return;
                oldPosition = position;

                Random random = new Random();
                int color = 0xff000000 | random.nextInt(0x00ffffff);
                String imageUrl = "";
                switch (position) {
                    case 0:
                        imageUrl = "http://www.cnwest88.com/uploadfile/2012/0509/20120509094628310.jpg";
                        break;
                    case 1:
                        imageUrl = "http://www.izmzg.com/empirecms/d/file/zmzg/beijing/jingdian/2012-03-16/a60ca4bef8ab6c4343bc486a548610ea.jpg";
                        break;
                    case 2:
                        imageUrl = "http://img.kpkpw.com/201106/27/14696_1309186930X2Mt.jpg";
                        break;
                    case 3:
                        imageUrl = "http://img1.58.com/groupbuy/n_s12275644173422709133%2120811748818944.jpg";
                        break;
                    case 4:
                        imageUrl = "http://www.nmghuana.com/UploadFiles/2014-03/nmghuana/2014032610591940146.jpg";
                        break;
                    case 5:
                        imageUrl = "http://img.pconline.com.cn/images/upload/upc/tx/photoblog/1107/30/c0/8490657_8490657_1311991396343.jpg";
                        break;
                    case 6:
                        imageUrl = "http://www.ctps.cn/PhotoNet/Profiles/BLUE%E6%9D%B0/2009326130172166.jpg";
                        break;
                    case 7:
                        imageUrl = "http://f.hiphotos.bdimg.com/album/w%3D2048/sign=f28844ce7e3e6709be0042ff0fff9e3d/962bd40735fae6cd2e8713880eb30f2442a70f23.jpg";
                        break;
                    case 8:
                        imageUrl = "http://www.cdtianya.com/baike/uploads/201303/1363830542ySdkzSVt.jpg";
                        break;
                    case 9:
                        imageUrl = "http://c.hiphotos.bdimg.com/album/w%3D2048/sign=e14035a13812b31bc76cca29b220347a/63d0f703918fa0ec9a37cdeb279759ee3c6ddb60.jpg";
                        break;
                }

                final int fadeDuration = 400;
                mViewPager.setImageUrl(imageUrl, fadeDuration);
                mViewPager.setColor(color, fadeDuration);

            }

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });

        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
        mViewPager.getViewPager().setCurrentItem(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("restaurants", restaurants);
        super.onSaveInstanceState(outState);
    }
}
