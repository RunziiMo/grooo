package com.wenym.grooo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.TextView;

import com.wenym.grooo.R;

/**
 * Created by runzii on 15-11-7.
 */
public class CheckableTextView extends TextView implements Checkable {


    private boolean isChecked = false;

    public CheckableTextView(Context context) {
        super(context);
    }

    public CheckableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
        if (checked) {
            setTextColor(getResources().getColor(R.color.red));
            setBackgroundResource(R.color.white);
        } else {
            setTextColor(getResources().getColor(R.color.textColorPrimary));
            setBackgroundResource(R.color.color_d2);
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        if (isChecked) {
            setChecked(false);
        } else {
            setChecked(true);
        }
    }
}
