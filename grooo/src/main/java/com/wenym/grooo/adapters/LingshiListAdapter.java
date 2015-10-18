package com.wenym.grooo.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wenym.grooo.R;
import com.wenym.grooo.model.LingshiClass;
import com.wenym.grooo.utils.SmallTools;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class LingshiListAdapter extends BaseAdapter {

    private static int[] images = {R.drawable.food1, R.drawable.food2,
            R.drawable.food3, R.drawable.food4, R.drawable.food5,
            R.drawable.food6, R.drawable.food7, R.drawable.food8,
            R.drawable.food9};

    private final LayoutInflater layoutInflater;
    private LingshiClass lingshiClass;

    public LingshiListAdapter(Context context, LingshiClass lingshiClass) {
        layoutInflater = LayoutInflater.from(context);
        this.lingshiClass = lingshiClass;
    }

    @Override
    public int getCount() {
        return lingshiClass.getClasses().size();
    }

    @Override
    public Object getItem(int position) {
        return lingshiClass.getClasses().values().toArray()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        CardView v;

        if (convertView == null) {
            v = (CardView) layoutInflater.inflate(
                    R.layout.lingshi_class_item, parent, false);
        } else {
            v = (CardView) convertView;
        }
        TextView tv = (TextView) v.findViewById(R.id.text);
        ImageView iv = (ImageView) v.findViewById(R.id.lingshi_class_image);
        tv.setText(lingshiClass.getClasses().keySet().toArray()[position].toString());
        iv.setBackgroundColor(0xff000000 | new Random().nextInt(0x00ffffff));
        ImageLoader.getInstance().displayImage(SmallTools.resourceIdToUri(images[position]), iv);
        return v;
    }

}