package com.wenym.grooo.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wenym.grooo.R;
import com.wenym.grooo.http.model.OrderFoodData;
import com.wenym.grooo.http.model.OrderFoodSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.model.app.AppUser;
import com.wenym.grooo.model.ecnomy.Food;
import com.wenym.grooo.model.ecnomy.OrderItem;
import com.wenym.grooo.provider.ShoppingBasket;
import com.wenym.grooo.provider.ExtraActivityKeys;
import com.wenym.grooo.utils.GroooAppManager;
import com.wenym.grooo.utils.SmallTools;
import com.wenym.grooo.ui.base.BaseActivity;
import com.wenym.grooo.widgets.Toasts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by runzii on 15-11-7.
 */
public class ConfirmOrderActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener {
    @Override
    protected boolean isHideNavigationBar() {
        return false;
    }

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
        return R.layout.activity_confirm_order;
    }

    private Button confirm;
    private MaterialEditText room, remark, yudingtime, building;
    private SwitchCompat to_door_switch;
    private boolean is_to_doors = true;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppUser appUser = GroooAppManager.getAppUser();
        remark = (MaterialEditText) findViewById(R.id.pay_remark);
        room = (MaterialEditText) findViewById(R.id.pay_room);
        room.setText(appUser.getRoomNum());
        building = (MaterialEditText) findViewById(R.id.pay_building);
        to_door_switch = (SwitchCompat) findViewById(R.id.pay_to_door_switch);

        to_door_switch.setChecked(is_to_doors);

        to_door_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                is_to_doors = isChecked;
                if (isChecked) {
                    yudingtime.setVisibility(View.GONE);
                    room.setEnabled(true);
                    building.setEnabled(true);
                } else {
                    yudingtime.setVisibility(View.VISIBLE);
                    room.setEnabled(false);
                    building.setEnabled(false);
                }
            }
        });


        building.setText(GroooAppManager.getAppUser().getUserBuilding());

        building.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new MaterialDialog.Builder(ConfirmOrderActivity.this)
                            .title("请选择宿舍楼")
                            .items(SmallTools.toArrayBuildings(GroooAppManager.getBuildings()))
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                                    building.setText(charSequence);
                                }
                            })
                            .show();
                }
            }
        });

        yudingtime = (MaterialEditText) findViewById(R.id.pay_time);
        if (is_to_doors) {
            yudingtime.setVisibility(View.GONE);
        } else {
            room.setVisibility(View.GONE);
            building.setVisibility(View.GONE);
        }
        yudingtime.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    Calendar now = Calendar.getInstance();
                    TimePickerDialog tpd = TimePickerDialog.newInstance(
                            ConfirmOrderActivity.this,
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            false
                    );
                    tpd.dismissOnPause(false);
                    tpd.enableSeconds(false);
                    tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            yudingtime.clearFocus();
                        }
                    });
                    tpd.show(getFragmentManager(), "Timepickerdialog");

                }
            }
        });
        confirm = (Button) findViewById(R.id.confirm_pay);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                String building1 = building.getText().toString();
                String roomnum = room.getText().toString();
                String rem = remark.getText().toString();
                String time = yudingtime.getText().toString().trim();
                if (!is_to_doors) {
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
                    orderFoodData.setBuildingNum(SmallTools.buildingtextToId(GroooAppManager.getBuildings(), building1));
                    if (is_to_doors)
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
                    confirm.setEnabled(false);
                    HttpUtils.MakeAPICall(orderFoodData, ConfirmOrderActivity.this, new HttpCallBack() {
                        @Override
                        public void onSuccess(Object object) {
                            OrderFoodSuccessData data = (OrderFoodSuccessData) object;
                            Intent intent = new Intent();
                            intent.putExtra(ExtraActivityKeys.CONFIRMPAY.toString(), true);
                            intent.putExtra(ExtraActivityKeys.PAYINFO.toString(), data.getInfo());
                            setResult(RESULT_OK, intent);
                            finish();
                            confirm.setEnabled(true);
                        }

                        @Override
                        public void onFailed(String reason) {
                            Toasts.show(reason);
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


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        yudingtime.setText(hourOfDay + ":" + minute);
        yudingtime.clearFocus();
    }
}
