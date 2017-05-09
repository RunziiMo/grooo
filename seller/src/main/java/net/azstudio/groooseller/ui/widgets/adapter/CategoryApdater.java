package net.azstudio.groooseller.ui.widgets.adapter;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import net.azstudio.groooseller.R;
import net.azstudio.groooseller.model.business.Menu;
import net.azstudio.groooseller.utils.RxJava.RxBus;
import net.azstudio.groooseller.utils.RxEvent.CategoryClickEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by runzii on 16-5-24.
 */
public class CategoryApdater extends RecyclerView.Adapter<CategoryApdater.CategoryHolder> {

    private List<Menu> foods;

    public CategoryApdater() {
        this(new ArrayList<>());
    }

    public CategoryApdater(List<Menu> foods) {
        this.foods = foods;
    }

    public List<Menu> getFoods() {
        return foods;
    }

    public void setFoods(List<Menu> foods) {
        this.foods = foods;
        notifyDataSetChanged();
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, final int position) {
        holder.name.setText(foods.get(position).getCategory());
        holder.count.setText("" + foods.get(position).getFoods().size());
        holder.itemView.setOnClickListener(v -> RxBus.getDefault()
                .post(new CategoryClickEvent(position, foods.get(position).getFoods())));
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public static class CategoryHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.category_dot)
        ImageView detail;
        @BindView(R.id.category_name)
        TextView name;
        @BindView(R.id.category_count)
        TextView count;

        public CategoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                detail.setImageDrawable(new IconicsDrawable(itemView.getContext(), GoogleMaterial.Icon.gmd_lens).sizeDp(8));
            } else {
                detail.setImageDrawable(new IconicsDrawable(itemView.getContext(), GoogleMaterial.Icon.gmd_lens).sizeDp(8));
            }
        }

    }

}