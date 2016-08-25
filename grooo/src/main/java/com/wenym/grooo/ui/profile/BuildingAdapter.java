package com.wenym.grooo.ui.profile;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.view.RxView;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ItemBuildingBinding;
import com.wenym.grooo.databinding.ItemSchoolBinding;
import com.wenym.grooo.model.app.School;

import java.util.List;

/**
 * Created by runzii on 16-8-24.
 */
public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.BuildingHolder> {

    private List<String> buildings;

    public BuildingAdapter(List<String> buildings, OnItemClickListener itemClickListener) {
        this.buildings = buildings;
        this.itemClickListener = itemClickListener;
    }

    private OnItemClickListener itemClickListener;

    public void update(List<String> newList) {
        if (newList.size() == buildings.size())
            return;
        buildings = newList;
        notifyDataSetChanged();
    }

    @Override
    public BuildingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_building, parent, false);
        return new BuildingHolder(view);
    }

    @Override
    public void onBindViewHolder(BuildingHolder holder, int position) {
        holder.bind(buildings.get(position));
        RxView.clicks(holder.itemView)
                .subscribe(aVoid -> {
                    if (itemClickListener != null)
                        itemClickListener.onItemClick(buildings.get(position));
                });
    }

    @Override
    public int getItemCount() {
        return buildings.size();
    }

    public interface OnItemClickListener {

        void onItemClick(String building);
    }

    public static class BuildingHolder extends RecyclerView.ViewHolder {

        private ItemBuildingBinding binding;

        public BuildingHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(String building) {
            binding.setBuilding(building);
        }
    }
}
