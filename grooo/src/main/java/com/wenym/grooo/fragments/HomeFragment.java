package com.wenym.grooo.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wenym.grooo.FecthKuaidiActivity;
import com.wenym.grooo.LingshiActivity;
import com.wenym.grooo.R;
import com.wenym.grooo.RestaurantListActivity;
import com.wenym.grooo.adapters.NewsPagerAdapter;
import com.wenym.grooo.http.model.GetNewsData;
import com.wenym.grooo.http.model.GetNewsSuccessData;
import com.wenym.grooo.http.model.SuggestData;
import com.wenym.grooo.http.model.SuggestSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.model.CategoryEntity;
import com.wenym.grooo.model.New;
import com.wenym.grooo.utils.ViewHolder;
import com.wenym.grooo.widgets.CircleIndicator;
import com.wenym.grooo.widgets.Toasts;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("InflateParams")
public class HomeFragment extends Fragment implements Callback {

    public static final int CHANGE_NEWS = 1;
    public static final int STOP_NEWS = 2;
    private static Runnable pageRunnable;
    NavgationAddapter navAdapter;// 顶部导航适配器(viewpager)
    NavAdapter navAdapter2;// 顶部导航内容适配器(gridview)
    private Handler Handler;
    private ViewPager customViewpager;
    private CircleIndicator customIndicator;
    private NewsPagerAdapter customPagerAdapter;
    private View homeLinearLayout;
    private ViewPager pager;// 顶部的滚动导航信息

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Handler = new Handler(this);
        pageRunnable = new Runnable() {

            @Override
            public void run() {
                if (customViewpager != null) {
                    customViewpager
                            .setCurrentItem(customViewpager.getCurrentItem() < customPagerAdapter
                                    .getCount() - 1 ? customViewpager
                                    .getCurrentItem() + 1 : 0);
                }
                Handler.postDelayed(this, 6000);
            }
        };
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == homeLinearLayout) {
            homeLinearLayout = inflater.inflate(R.layout.fragment_home,
                    null);
            customViewpager = (ViewPager) homeLinearLayout
                    .findViewById(R.id.viewpager_home);
            customIndicator = (CircleIndicator) homeLinearLayout
                    .findViewById(R.id.indicator_home);
            pager = (ViewPager) homeLinearLayout.findViewById(R.id.pager);
            // 设置滚动到最上部（解决在scrollview嵌套listview时，出现listview出现在最上面的bug）
            pager.setFocusable(true);
            pager.setFocusableInTouchMode(true);
            pager.requestFocus();
            initFirstNavgation();
            HttpUtils.MakeAPICall(new GetNewsData(), getActivity(), new HttpCallBack() {
                @Override
                public void onSuccess(Object object) {
                    GetNewsSuccessData getNewsSuccessData = (GetNewsSuccessData) object;
                    ArrayList<New> news = getNewsSuccessData.getNews();
                    if (news.size() == 0) {
                        news.add(new New("抱歉，暂无新闻", "", "", ""));
                    }
                    customPagerAdapter = new NewsPagerAdapter(
                            getFragmentManager(),
                            news);
                    customViewpager.setAdapter(customPagerAdapter);
                    customIndicator.setViewPager(customViewpager);
                }

                @Override
                public void onFailed() {

                }

                @Override
                public void onError(int statusCode) {
                    Toasts.show("news " + statusCode);
                }
            });
        }
        return homeLinearLayout;
    }

    /**
     * 初始化首导航
     */
    private void initFirstNavgation() {
        navAdapter = new NavgationAddapter(initNavViews());
        pager.setAdapter(navAdapter);
        // indicator.setViewPager(pager);
    }

    /**
     * 初始化首导航的view集合
     *
     * @return
     */
    private List<View> initNavViews() {
        List<View> views = new ArrayList<View>();
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        GridView view1 = (GridView) inflater.inflate(
                R.layout.home_fragment_menu1, null);
        List<CategoryEntity> entities = getCategoryEntity();
        navAdapter2 = new NavAdapter(getActivity(), entities);
        view1.setAdapter(navAdapter2);
        view1.setOnItemClickListener(new OnItemClickListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(getActivity(), RestaurantListActivity.class);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), RestaurantListActivity.class);
                        intent.putExtra("isWaiMai", false);
                        break;
                    case 2:
                        intent = new Intent(getActivity(), FecthKuaidiActivity.class);
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(),
                                LingshiActivity.class));
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        new MaterialDialog.Builder(getActivity())
                                .title("SPANK US")
                                .widgetColor(getResources().getColor(R.color.blue))
                                .positiveColor(
                                        getResources().getColor(R.color.blue))
                                .content("您的建议对我们至关重要!")
                                .input(R.string.recallyousoon, 0,
                                        new MaterialDialog.InputCallback() {
                                            @Override
                                            public void onInput(
                                                    MaterialDialog dialog,
                                                    CharSequence input) {
                                                SuggestData suggestData = new SuggestData();
                                                suggestData.setContent(input.toString());
                                                HttpUtils.MakeAPICall(suggestData, getActivity(), new HttpCallBack() {
                                                    @Override
                                                    public void onSuccess(Object object) {
                                                        SuggestSuccessData suggestSuccessData = (SuggestSuccessData) object;
                                                        Toasts.show("建议已提交，我们会尽快反馈");
                                                    }

                                                    @Override
                                                    public void onFailed() {

                                                    }

                                                    @Override
                                                    public void onError(int statusCode) {
                                                        Toasts.show("Suggest " + statusCode);
                                                    }
                                                });
                                            }
                                        }).show();
                        break;
                    case 7:
                        break;
                    default:
                        break;
                }
                if (intent != null) {
                    getActivity().startActivity(intent);
                }
            }
        });
        // View view2 = inflater.inflate(R.layout.home_fragment_menu2, null);
        // views.add(view2);
        views.add(view1);
        return views;
    }

    /**
     * 初始化category
     *
     * @return
     */
    private List<CategoryEntity> getCategoryEntity() {
        List<CategoryEntity> categorys = new ArrayList<CategoryEntity>();
        categorys.add(new CategoryEntity(0, "外卖", R.drawable.ic_category_0));
        categorys.add(new CategoryEntity(1, "预定", R.drawable.ic_category_1));
        categorys.add(new CategoryEntity(2, "快递", R.drawable.ic_category_2));
        categorys.add(new CategoryEntity(3, "超市	", R.drawable.ic_category_3));
        categorys.add(new CategoryEntity(4, "二手", R.drawable.ic_category_4));
        categorys.add(new CategoryEntity(5, "咕噜街", R.drawable.ic_category_5));
        categorys.add(new CategoryEntity(6, "吐槽", R.drawable.ic_category_6));
        categorys
                .add(new CategoryEntity(7, "Gossip", R.drawable.ic_category_7));
        return categorys;
    }

    @Override
    public void onPause() {
        Handler.sendMessage(Handler.obtainMessage(STOP_NEWS));
        HttpUtils.CancelHttpTask();
        super.onPause();
    }

    @Override
    public void onResume() {
        Handler.sendMessage(Handler
                .obtainMessage(CHANGE_NEWS));
        super.onResume();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case CHANGE_NEWS:
//                Handler.postDelayed(pageRunnable, 6000);
                break;
            case STOP_NEWS:
//                Handler.removeCallbacks(pageRunnable);
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 顶部导航view的gridview适配器
     *
     * @author join
     */
    class NavAdapter extends BaseAdapter {

        LayoutInflater inflater;
        List<CategoryEntity> entitys;

        public NavAdapter(Context context, List<CategoryEntity> entitys) {
            this.inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.entitys = entitys;
        }

        @Override
        public int getCount() {
            return entitys.size();
        }

        @Override
        public Object getItem(int position) {
            return entitys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(
                        R.layout.home_fragment_menu_item, null);
            }
            CategoryEntity category = (CategoryEntity) getItem(position);
            ImageView nvImageView = ViewHolder.get(convertView, R.id.nv_img);
            TextView nvTextView = ViewHolder.get(convertView, R.id.nv_text);
            nvImageView.setBackgroundResource(category.resImage);
            nvTextView.setText(category.name);
            return convertView;
        }
    }

    /**
     * 顶部导航的适配器
     *
     * @author join
     */
    class NavgationAddapter extends PagerAdapter {
        List<View> views;

        public NavgationAddapter(List<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views == null || views.size() == 0 ? 0 : views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }
    }


}
