package net.azstudio.groooseller.utils;

import android.hardware.camera2.params.StreamConfigurationMap;
import android.widget.Toast;

import net.azstudio.groooseller.utils.AppManager;


/**
 * Toast类，无需多说
 * Created by Wouldyou on 2015/5/29.
 */
public class Toasts {

    public static void show(String msg) {
        Toast.makeText(AppManager.getAppContext(), msg, Toast.LENGTH_LONG).show();
    }
    public static void show(int msg) {
        Toast.makeText(AppManager.getAppContext(), String.valueOf(msg), Toast.LENGTH_LONG).show();
    }
}
