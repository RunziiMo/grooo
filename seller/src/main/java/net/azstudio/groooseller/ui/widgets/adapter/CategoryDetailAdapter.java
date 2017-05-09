package net.azstudio.groooseller.ui.widgets.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.azstudio.groooseller.R;
import net.azstudio.groooseller.model.http.HttpFood;
import net.azstudio.groooseller.ui.activities.MenuActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by runzii on 16-5-25.
 */
public class CategoryDetailAdapter extends RecyclerView.Adapter<CategoryDetailAdapter.FoodHolder> {

    private boolean selectMode = false;
    // Start with first item selected

    private List<HttpFood> foods;

    private List<HttpFood> selectItems = new ArrayList<>();

    private MenuActivity.ItemClickCallback mCallback; // 用户点击项的回调
    private MenuActivity.ItemLongClickCallback itemLongClickCallback;

    public CategoryDetailAdapter(List<HttpFood> foods, MenuActivity.ItemLongClickCallback itemLongClickCallback, MenuActivity.ItemClickCallback mCallback) {
        this.foods = foods;
        this.itemLongClickCallback = itemLongClickCallback;
        this.mCallback = mCallback;
    }

    public void setFoods(List<HttpFood> foods) {
        this.foods = foods;
        notifyDataSetChanged();
    }

    public List<HttpFood> getSelectItems() {
        return selectItems;
    }

    @Override
    public FoodHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodHolder holder, final int position) {
        final HttpFood food = foods.get(position);
        Glide.with(holder.itemView.getContext()).load(food.getLogo()).placeholder(R.drawable.login_logo).into(holder.logo);
        holder.name.setText(food.getName());
        holder.price.setText(food.getPrice());
        holder.sold.setText(food.getMonthSold());
        holder.description.setText(food.getDescription());
        holder.itemView.setOnClickListener(v -> {
            if (!selectMode)
                mCallback.onItemClicked(food, false, food.getCategory());
        });
        holder.itemView.setOnLongClickListener(v -> {
            itemLongClickCallback.onItemLongClicked();
            return false;
        });
        holder.select.setVisibility(selectMode ? View.VISIBLE : View.GONE);
        holder.select.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                selectItems.add(food);
            else
                selectItems.remove(food);
        });
        holder.select.setChecked(selectItems.contains(food) ? true : false);
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public boolean isSelectMode() {
        return selectMode;
    }

    public void setSelectMode(boolean mode) {
        if (selectMode == mode)
            return;
        selectMode = mode;
        if (!selectMode)
            selectItems.clear();
        notifyDataSetChanged();
    }

    public static class FoodHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.food_list_item_image)
        ImageView logo;
        @BindView(R.id.food_list_item_name)
        TextView name;
        @BindView(R.id.food_list_item_price)
        TextView price;
        @BindView(R.id.food_list_item_monthsold)
        TextView sold;
        @BindView(R.id.food_list_item_description)
        TextView description;
        @BindView(R.id.food_list_item_select)
        CheckBox select;

        public FoodHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
