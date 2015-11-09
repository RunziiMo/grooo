package com.wenym.grooo.widgets;

import android.widget.Toast;

import com.wenym.grooo.utils.GroooAppManager;

/**
 * Toast类，无需多说
 * Created by Wouldyou on 2015/5/29.
 */
public class Toasts {

    public static void show(String msg) {
        Toast.makeText(GroooAppManager.getAppContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
