package com.wenym.grooo.ui.shop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wenym.grooo.R;
import com.wenym.grooo.widgets.CheckableTextView;

import java.util.List;

/**
 * Created by runzii on 16-8-31.
 */
public class HeaderListViewAdapter extends BaseAdapter {

    private List<String> stickersLeft;

    public HeaderListViewAdapter(List<String> stickersLeft) {
        this.stickersLeft = stickersLeft;
    }

    @Override
    public int getCount() {
        return stickersLeft.size();
    }

    @Override
    public String getItem(int position) {
        return stickersLeft.get(position);
    }

    @Override
    public long getItemId(int position) {
        return stickersLeft.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HeaderHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_list_header, parent, false);
            holder = new HeaderHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HeaderHolder) convertView.getTag();
        }
        holder.header.setText(getItem(position));
        return convertView;
    }

    public class HeaderHolder {
        public final CheckableTextView header;

        public HeaderHolder(View view) {
            header = (CheckableTextView) view.findViewById(R.id.food_list_item_header);
        }

    }
}
