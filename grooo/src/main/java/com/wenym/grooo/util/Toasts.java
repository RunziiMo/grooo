package com.wenym.grooo.util;

import android.widget.Toast;

import com.runzii.lib.utils.MyAppManager;


/**
 * Toast类，无需多说
 * Created by Wouldyou on 2015/5/29.
 */
public class Toasts {

    public static void show(String msg) {
        Toast.makeText(GroooAppManager.getAppContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static void show(int msg) {
        Toast.makeText(GroooAppManager.getAppContext(), String.valueOf(msg), Toast.LENGTH_LONG).show();
    }
}
