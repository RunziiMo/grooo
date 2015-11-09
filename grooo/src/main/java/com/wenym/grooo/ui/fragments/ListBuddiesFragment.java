package com.wenym.grooo.ui.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.MaterialViewPagerImageHelper;
import com.jpardogo.listbuddies.lib.provider.ScrollConfigOptions;
import com.jpardogo.listbuddies.lib.views.ListBuddiesLayout;
import com.wenym.grooo.R;
import com.wenym.grooo.adapters.CircularAdapter;
import com.wenym.grooo.http.model.GetBaiduPictureData;
import com.wenym.grooo.http.model.GetBaiduPictureSuccessData;
import com.wenym.grooo.http.model.GetOrderData;
import com.wenym.grooo.http.model.GetOrderSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.model.FoodOrder;
import com.wenym.grooo.provider.ExtraActivityKeys;
import com.wenym.grooo.provider.ExtraArgumentKeys;
import com.wenym.grooo.provider.HomeItem;
import com.wenym.grooo.ui.activities.AboutActivity;
import com.wenym.grooo.ui.activities.FecthKuaidiActivity;
import com.wenym.grooo.ui.activities.LookOrderActivity;
import com.wenym.grooo.ui.activities.RestaurantListActivity;
import com.wenym.grooo.ui.models.HomeItemEntity;
import com.wenym.grooo.utils.GroooAppManager;
import com.wenym.grooo.utils.OrderStatus;
import com.wenym.grooo.utils.SmallTools;
import com.wenym.grooo.utils.stackblur.StackBlurManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ListBuddiesFragment extends Fragment implements ListBuddiesLayout.OnBuddyItemClickListener {

    private static int[] images = {R.drawable.food1, R.drawable.food2,
            R.drawable.food3, R.drawable.food4, R.drawable.food5,
            R.drawable.food6, R.drawable.food7, R.drawable.food8,
            R.drawable.food9};

    private static final String TAG = ListBuddiesFragment.class.getSimpleName();
    int mMarginDefault;
    int[] mScrollConfig;
    private boolean isOpenActivities;
    private CircularAdapter mAdapterLeft;
    private CircularAdapter mAdapterRight;
    @InjectView(R.id.listbuddies)
    ListBuddiesLayout mListBuddies;
    private List<HomeItemEntity> mImagesLeft = new ArrayList<HomeItemEntity>();
    private List<HomeItemEntity> mImagesRight = new ArrayList<HomeItemEntity>();
    private HomeItemEntity currOrder;

    public static ListBuddiesFragment newInstance(boolean isOpenActivitiesActivated) {
        ListBuddiesFragment fragment = new ListBuddiesFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ExtraArgumentKeys.OPEN_ACTIVITES.toString(), isOpenActivitiesActivated);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isOpenActivities = getArguments().getBoolean(ExtraArgumentKeys.OPEN_ACTIVITES.toString(), false);
        mMarginDefault = getResources().getDimensionPixelSize(R.dimen.default_margin_between_lists);
        mScrollConfig = getResources().getIntArray(R.attr.scrollFaster);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listbuddy, container, false);
        ButterKnife.inject(this, rootView);

        initHomeItems();
        mAdapterLeft = new CircularAdapter(getActivity(), mImagesLeft);
        mAdapterRight = new CircularAdapter(getActivity(), mImagesRight);
        mListBuddies.setAdapters(mAdapterLeft, mAdapterRight);
        mListBuddies.setOnItemClickListener(this);
        return rootView;
    }

    private void initHomeItems() {

        HomeItemEntity takeout, market, delivery,about,suggest;
        takeout = new HomeItemEntity();
        takeout.setHomeitem_avator(SmallTools.resourceIdToUri(R.drawable.ic_category_0));
        takeout.setHomeitem_back(SmallTools.resourceIdToUri(images[new Random().nextInt(images.length)]));
        takeout.setHomeitem_content("come and try our new specials");
        takeout.setHomeitem_title("咕噜外卖");
        takeout.setHomeiten_height(new Random().nextInt(300) + 220);
        takeout.setKind(HomeItem.TAKEOUT);

        market = new HomeItemEntity();
        market.setHomeitem_avator(SmallTools.resourceIdToUri(R.drawable.ic_category_3));
        market.setHomeitem_back(SmallTools.resourceIdToUri(images[new Random().nextInt(images.length)]));
        market.setHomeitem_content("we have thousands of eats here,fit your mouth!");
        market.setHomeitem_title("咕噜超市");
        market.setHomeiten_height(new Random().nextInt(300) + 220);
        market.setKind(HomeItem.MARKET);

        delivery = new HomeItemEntity();
        delivery.setHomeitem_avator(SmallTools.resourceIdToUri(R.drawable.ic_category_2));
        delivery.setHomeitem_back(SmallTools.resourceIdToUri(images[new Random().nextInt(images.length)]));
        delivery.setHomeitem_content("fetch the goddame deliveries for assholes");
        delivery.setHomeitem_title("咕噜快递");
        delivery.setHomeiten_height(new Random().nextInt(300) + 220);
        delivery.setKind(HomeItem.DELIVERY);

        about = new HomeItemEntity();
        about.setHomeitem_avator(SmallTools.resourceIdToUri(R.mipmap.ic_launcher));
        about.setHomeitem_back(SmallTools.resourceIdToUri(images[new Random().nextInt(images.length)]));
        about.setHomeitem_content("详情请拨１１０");
        about.setHomeitem_title("关于我们");
        about.setHomeiten_height(new Random().nextInt(300) + 220);
        about.setKind(HomeItem.ABOUT);

        suggest = new HomeItemEntity();
        suggest.setHomeitem_avator(SmallTools.resourceIdToUri(R.drawable.ic_category_6));
        suggest.setHomeitem_back(SmallTools.resourceIdToUri(images[new Random().nextInt(images.length)]));
        suggest.setHomeitem_content("建议、投诉……什么都可以哦");
        suggest.setHomeitem_title("吐槽");
        suggest.setHomeiten_height(new Random().nextInt(300) + 220);
        suggest.setKind(HomeItem.SUGGEST);
        //If we do this we need to uncomment the container on the xml layout
        //createListBuddiesLayoutDinamically(rootView);

        FoodOrder order = GroooAppManager.getTakeouts().get(0);
        String des = "单号:" + order.getId() + "\n状态:" + OrderStatus.getStatus(order.getStatus());
        currOrder = new HomeItemEntity();
        currOrder.setKind(HomeItem.ORDER);
        currOrder.setHomeitem_avator(order.getSellerImageURL());
        currOrder.setHomeitem_back(SmallTools.resourceIdToUri(images[new Random().nextInt(images.length)]));
        currOrder.setHomeitem_title(order.getSeller_name());
        currOrder.setHomeitem_content(des);

        addHomeItem(suggest);
        addHomeItem(takeout);
        addHomeItem(market);
        addHomeItem(delivery);
        addHomeItem(about);
        addHomeItem(currOrder);

    }

    private void addHomeItem(HomeItemEntity homeItemEntity) {
        if (new Random().nextInt(2) == 0) {
            mImagesLeft.add(new Random().nextInt(mImagesLeft.size() + 1), homeItemEntity);
        } else {
            mImagesRight.add(new Random().nextInt(mImagesRight.size() + 1), homeItemEntity);
        }
    }

    private void removeHomeItem(HomeItemEntity homeItemEntity) {
        mImagesLeft.remove(homeItemEntity);
        mImagesRight.remove(homeItemEntity);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void createListBuddiesLayoutDinamically(View rootView) {
        mListBuddies = new ListBuddiesLayout(getActivity());
        resetLayout();
        //Once the container is created we can add the ListViewLayout into it
        //((FrameLayout)rootView.findViewById(R.id.<container_id>)).addView(mListBuddies);
    }

    @Override
    public void onBuddyItemClicked(AdapterView<?> parent, View view, int buddy, int position, long id) {

        if (isOpenActivities) {
            Intent intent = null;
            switch (getHomeItem(buddy, position)) {
                case TAKEOUT:
                    intent = new Intent(getActivity(), RestaurantListActivity.class);
                    intent.putExtra(ExtraActivityKeys.SHOPKIND.toString(), RestaurantListActivity.TAKEOUT);
                    break;
                case MARKET:
                    intent = new Intent(getActivity(), RestaurantListActivity.class);
                    intent.putExtra(ExtraActivityKeys.SHOPKIND.toString(), RestaurantListActivity.MARKET);
                    break;
                case DELIVERY:
                    intent = new Intent(getActivity(), FecthKuaidiActivity.class);
                    break;
                case ORDER:
                    intent = new Intent(getActivity(), LookOrderActivity.class);
                    break;
                case ABOUT:
                    intent = new Intent(getActivity(), AboutActivity.class);
                    break;
                case SUGGEST:
                    intent = new Intent(getActivity(), AboutActivity.class);
                    break;
                default:
                    break;
            }

            if (intent != null) {
                startActivity(intent);
            }
        } else {
            Resources resources = getResources();
            Toast.makeText(getActivity(), "列表: " + buddy + " 位置: " + position, Toast.LENGTH_SHORT).show();
        }
    }

    private HomeItem getHomeItem(int buddy, int postion) {
        return buddy == 0 ? mImagesLeft.get(postion).getKind() : mImagesRight.get(postion).getKind();
    }


    public void setGap(int value) {
        mListBuddies.setGap(value);
    }

    public void setSpeed(int value) {
        mListBuddies.setSpeed(value);
    }

    public void setDividerHeight(int value) {
        mListBuddies.setDividerHeight(value);
    }

    public void setGapColor(int color) {
        mListBuddies.setGapColor(color);
    }

    public void setAutoScrollFaster(int option) {
        mListBuddies.setAutoScrollFaster(option);
    }

    public void setScrollFaster(int option) {
        mListBuddies.setManualScrollFaster(option);
    }

    public void setDivider(Drawable drawable) {
        mListBuddies.setDivider(drawable);
    }

    public void setOpenActivities(Boolean openActivities) {
        this.isOpenActivities = openActivities;
    }

    public void resetLayout() {
        mListBuddies.setGap(mMarginDefault)
                .setSpeed(ListBuddiesLayout.DEFAULT_SPEED)
                .setDividerHeight(mMarginDefault)
                .setGapColor(getResources().getColor(R.color.black))
                .setAutoScrollFaster(mScrollConfig[ScrollConfigOptions.RIGHT.getConfigValue()])
                .setManualScrollFaster(mScrollConfig[ScrollConfigOptions.LEFT.getConfigValue()])
                .setDivider(getResources().getDrawable(R.drawable.list_divider));
    }

}