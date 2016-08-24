package com.wenym.grooo.ui.profile;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.view.RxView;
import com.wenym.grooo.R;
import com.wenym.grooo.databinding.ItemSchoolBinding;
import com.wenym.grooo.model.app.School;

import java.util.List;

/**
 * Created by runzii on 16-8-24.
 */
public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.SchoolHolder> {

    private List<School> schools;

    public SchoolAdapter(List<School> schools, OnItemClickListener itemClickListener) {
        this.schools = schools;
        this.itemClickListener = itemClickListener;
    }

    private OnItemClickListener itemClickListener;

    public void update(List<School> newList) {
        if (newList.size() == schools.size())
            return;
        schools = newList;
        notifyDataSetChanged();
    }

    @Override
    public SchoolHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_school, parent, false);
        return new SchoolHolder(view);
    }

    @Override
    public void onBindViewHolder(SchoolHolder holder, int position) {
        holder.bind(schools.get(position));
        RxView.clicks(holder.itemView)
                .subscribe(aVoid -> {
                    if (itemClickListener != null)
                        itemClickListener.onItemClick(schools.get(position));
                });
    }

    @Override
    public int getItemCount() {
        return schools.size();
    }

    public interface OnItemClickListener {

        void onItemClick(School school);
    }

    public static class SchoolHolder extends RecyclerView.ViewHolder {

        private ItemSchoolBinding binding;

        public SchoolHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(School school) {
            binding.setSchool(school);
        }
    }
}
