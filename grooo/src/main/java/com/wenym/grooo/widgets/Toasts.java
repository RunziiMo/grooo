package com.wenym.grooo.widgets;

import android.widget.Toast;

import com.wenym.grooo.GroooApplication;

/**
 * Created by Wouldyou on 2015/5/29.
 */
public class Toasts {

    public static void show(String msg) {
        Toast.makeText(GroooApplication.getGroooApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
