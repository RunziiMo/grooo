package com.wenym.grooo.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.materialviewpager.Utils;
import com.jpardogo.listbuddies.lib.adapters.CircularLoopAdapter;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.squareup.picasso.Picasso;
import com.wenym.grooo.R;
import com.wenym.grooo.model.app.HomeItemEntity;
import com.wenym.grooo.widgets.CircleTransformation;
import com.wenym.grooo.widgets.ScaleToFitWidhtHeigthTransform;

import java.util.ArrayList;
import java.util.List;

public class CircularAdapter extends CircularLoopAdapter {
    private static final String TAG = CircularAdapter.class.getSimpleName();

    private List<HomeItemEntity> mItems = new ArrayList<HomeItemEntity>();
    private Context mContext;

    public CircularAdapter(Context context, List<HomeItemEntity> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public HomeItemEntity getItem(int position) {
        return mItems.get(getCircularPosition(position));
    }

    @Override
    protected int getCircularCount() {
        return mItems.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        HomeItemEntity entity = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_home, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (entity.getHomeiten_height() != 0) {
            holder.back.setMinimumHeight(entity.getHomeiten_height());
        }
        if (!TextUtils.isEmpty(entity.getHomeitem_content())) {
            holder.content.setText(entity.getHomeitem_content());
        }else {
            holder.content.setText("");
        }
        if (!TextUtils.isEmpty(entity.getHomeitem_title())) {
            holder.title.setText(entity.getHomeitem_title());
        }else {
            holder.title.setText("");
        }
        if (!TextUtils.isEmpty(entity.getHomeitem_back())) {
//            Picasso.with(mContext)
//                    .load(entity.getHomeitem_back())
//                    .transform(new ScaleToFitWidhtHeigthTransform(entity.getHomeiten_height(), true))
//                    .into(holder.back);
        }
        if (!TextUtils.isEmpty(entity.getHomeitem_avator())) {
            Picasso.with(mContext)
                    .load(entity.getHomeitem_avator())
                    .transform(new CircleTransformation())
                    .into(holder.avator);
        }
        return convertView;
    }

    public static class ViewHolder {
        ImageView back;
        ImageView avator;
        TextView title;
        TextView content;

        public ViewHolder(View convertView) {
            back = (ImageView) convertView.findViewById(R.id.homeitem_back);
            avator = (ImageView) convertView.findViewById(R.id.homeitem_avator);
            title = (TextView) convertView.findViewById(R.id.homeitem_title);
            content = (TextView) convertView.findViewById(R.id.homeitem_content);
        }
    }

    private void generateColor(Bitmap bitmap, final TextView... textView) {
        new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch();

                if (null != swatch) {

                    for (final TextView t : textView) {
                        int before = t.getCurrentTextColor();
                        int after = swatch.getTitleTextColor();
                        ObjectAnimator colorAnimText = ObjectAnimator.ofInt(textView, "textColor", before, after);
                        colorAnimText.setEvaluator(new ArgbEvaluator());
                        colorAnimText.setDuration((long) 200);
                        colorAnimText.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int colorAlpha = Utils.colorWithAlpha(((Integer) animation.getAnimatedValue()).intValue(), 0f);
                                t.setTextColor(colorAlpha);
                            }
                        });
                        colorAnimText.start();
                    }


                }
            }

        });
    }

}
