package com.wenym.grooo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.rey.material.app.DialogFragment;
import com.rey.material.app.TimePickerDialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.Spinner;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.wenym.grooo.GroooApplication;
import com.wenym.grooo.R;
import com.wenym.grooo.RestaurantDetailActivity;
import com.wenym.grooo.http.model.OrderFoodData;
import com.wenym.grooo.http.model.OrderFoodSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.model.Food;
import com.wenym.grooo.model.OrderItem;
import com.wenym.grooo.model.ShoppingBasket;
import com.wenym.grooo.utils.SharedPreferencesUtil;
import com.wenym.grooo.utils.SmallTools;
import com.wenym.grooo.widgets.Toasts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;

@SuppressWarnings("deprecation")
public class BasketStatusFragment extends Fragment {

    private Button confirm;
    private EditText room, remark, yudingtime;
    private Spinner building;
    private boolean isWaiMai;


    public static BasketStatusFragment newInstance(boolean isWaiMai) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isWaiMai", isWaiMai);
        BasketStatusFragment fragment = new BasketStatusFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        isWaiMai = getArguments().getBoolean("isWaiMai");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pay_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        remark = (EditText) view.findViewById(R.id.pay_remark);
        room = (EditText) view.findViewById(R.id.pay_room);
        room.setText(SharedPreferencesUtil.getUserRoom());
        building = (Spinner) view.findViewById(R.id.pay_building);
        String[] items = SmallTools.toArrayBuildings(GroooApplication.buildings);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.row_spn, items);
        adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
//        String[] items = new String[20];
//        for(int i = 0; i < items.length; i++)
//            items[i] = "Item " + String.valueOf(i + 1);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.row_spn, items);
//        adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
//        spn_label.setAdapter(adapter);
        building.setAdapter(adapter);
        building.setSelection(Arrays.binarySearch(items, SharedPreferencesUtil.getUserBuilding()));
        yudingtime = (EditText) view.findViewById(R.id.pay_time);
        if (isWaiMai) {
            yudingtime.setVisibility(View.GONE);
        } else {
            room.setVisibility(View.GONE);
            building.setVisibility(View.GONE);
        }
        yudingtime.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    TimePickerDialog.Builder builder = new TimePickerDialog.Builder(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                            , Calendar.getInstance().get(Calendar.MINUTE)) {
                        @Override
                        public void onPositiveActionClicked(DialogFragment fragment) {
                            TimePickerDialog dialog = (TimePickerDialog) fragment.getDialog();
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                            yudingtime.setText(dialog.getFormattedTime(format));
                            yudingtime.clearFocus();
                            super.onPositiveActionClicked(fragment);
                        }

                        @Override
                        public void onNegativeActionClicked(DialogFragment fragment) {
                            yudingtime.clearFocus();
                            super.onNegativeActionClicked(fragment);
                        }
                    };

                    builder.positiveAction("确定")
                            .negativeAction("取消");

                    DialogFragment fragment = DialogFragment.newInstance(builder);
                    fragment.show(getFragmentManager(), null);
                }
            }
        });
        confirm = (Button) view.findViewById(R.id.confirm_pay);
        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RestaurantDetailActivity) getActivity()).toggleSlidingUpLayout(SlidingUpPanelLayout.PanelState.COLLAPSED);
                ShoppingBasket basket = ShoppingBasket.getInstance();
                switch (basket.isOkToPay()) {
                    case OK:
                        break;
                    case RESTING:
                        Toasts.show("商家已休息");
                        return;
                    case NOTENOUGH:
                        Toasts.show("未达到起送价");
                        return;
                }
                String building1 = building.getSelectedItem().toString();
                String roomnum = room.getText().toString();
                String rem = remark.getText().toString();
                String time = yudingtime.getText().toString().trim();
                if (!isWaiMai) {
                    if ("".equals(time)) {
                        yudingtime.setError("请填写用餐时间");
                        yudingtime.requestFocus();
                        return;
                    } else {
                        rem = "（用餐时间：" + time + ")" + rem;
                    }
                }
                if (roomnum.length() == 4) {
                    OrderFoodData orderFoodData = new OrderFoodData();
                    orderFoodData.setBuildingNum(SmallTools.buildingtextToId(GroooApplication.buildings, building1));
                    if (isWaiMai)
                        orderFoodData.setMethod("0");
                    else
                        orderFoodData.setMethod("1");
                    orderFoodData.setRemark(rem);
                    orderFoodData.setRoomNum(roomnum);
                    orderFoodData.setSid(basket.getShopid());
                    Iterator<Food> foodlists = basket.getOrder().keySet()
                            .iterator();
                    Iterator<Integer> foodnums = basket.getOrder().values()
                            .iterator();
                    ArrayList<OrderItem> foodlist = new ArrayList<OrderItem>();
                    while (foodnums.hasNext()) {
                        OrderItem food = new OrderItem();
                        food.setCount(foodnums.next());
                        food.setId(String.valueOf(foodlists.next().getId()));
                        foodlist.add(food);
                    }
                    orderFoodData.setFoodlist(foodlist);
                    HttpUtils.MakeAPICall(orderFoodData, getActivity(), new HttpCallBack() {
                        @Override
                        public void onSuccess(Object object) {
                            OrderFoodSuccessData data = (OrderFoodSuccessData) object;
                            Toasts.show(data.getInfo());
                        }

                        @Override
                        public void onFailed() {

                        }

                        @Override
                        public void onError(int statusCode) {
                            Toasts.show("Pay " + statusCode);
                        }
                    });
                }
            }
        });
    }

}
