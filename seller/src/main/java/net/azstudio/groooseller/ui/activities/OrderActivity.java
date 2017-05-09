package net.azstudio.groooseller.ui.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;

import net.azstudio.groooseller.R;
import net.azstudio.groooseller.databinding.ActivityOrdercenterBinding;
import net.azstudio.groooseller.http.NetworkWrapper;
import net.azstudio.groooseller.model.app.ObservableCalendar;
import net.azstudio.groooseller.ui.base.BaseActivity;
import net.azstudio.groooseller.ui.widgets.EmptySupportRecyclerView;
import net.azstudio.groooseller.ui.widgets.adapter.SimpleOrderAdapter;
import net.azstudio.groooseller.utils.RxJava.RxNetWorking;

import java.util.Calendar;

/**
 * Created by runzii on 16-5-31.
 */
public class OrderActivity extends BaseActivity<ActivityOrdercenterBinding> {


    private ObservableCalendar calendar;
    private ActivityOrdercenterBinding ordercenterBinding;

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
        return R.layout.activity_ordercenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ordercenterBinding = getDataBinding();
        calendar = new ObservableCalendar(Calendar.getInstance());
        ordercenterBinding.setDate(calendar);
        setUpPager();
        RxSwipeRefreshLayout.refreshes(ordercenterBinding.swipeRefreshLayout)
                .doOnUnsubscribe(() -> {
                    ordercenterBinding.swipeRefreshLayout.setRefreshing(false);
                })
                .compose(bindToLifecycle())
                .subscribe(aVoid -> {
                    ordercenterBinding.swipeRefreshLayout.setRefreshing(false);
                });
        ordercenterBinding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryLight, R.color.colorPrimary);
    }

    private void setUpPager() {
        ordercenterBinding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                ordercenterBinding.swipeRefreshLayout.setEnabled(state == ViewPager.SCROLL_STATE_IDLE);
            }
        });
        ordercenterBinding.pager.setAdapter(new OrderPagerAdapter());
        ordercenterBinding.tabs.setupWithViewPager(ordercenterBinding.pager);
    }

    public void leftClick(View view) {
        calendar.rollMonth(false);
        calendar.notifyChange();
        ordercenterBinding.pager.getAdapter().notifyDataSetChanged();
    }

    public void rightClick(View view) {
        calendar.rollMonth(true);
        calendar.notifyChange();
        ordercenterBinding.pager.getAdapter().notifyDataSetChanged();
    }

    private class OrderPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return calendar.getDays();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = getLayoutInflater().inflate(R.layout.item_viewpager_ordermanage, container, false);
            EmptySupportRecyclerView recyclerView = (EmptySupportRecyclerView) v.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(OrderActivity.this));
            recyclerView.setEmptyView(v.findViewById(R.id.empty_view));
            SimpleOrderAdapter orderAdapter = new SimpleOrderAdapter();
            recyclerView.setAdapter(orderAdapter);
            NetworkWrapper.get()
                    .getOrder(calendar.getDate(position + 1), calendar.getDate(position + 2))
                    .compose(RxNetWorking.bindRefreshing(ordercenterBinding.swipeRefreshLayout))
                    .compose(bindToLifecycle())
                    .subscribe(orders -> orderAdapter.setOrders(orders)
                            , throwable -> Snackbar.make(recyclerView, throwable.getMessage(), Snackbar.LENGTH_SHORT).show());
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "" + (position + 1);
        }
    }
}
