package com.wenym.grooo.utils;

import com.wenym.grooo.R;

import java.util.Random;

/**
 * Created by Wouldyou on 2015/7/2.
 */
public class ImageProvider {

    private static int[] images = {R.drawable.food1, R.drawable.food2,
            R.drawable.food3, R.drawable.food4, R.drawable.food5,
            R.drawable.food6, R.drawable.food7, R.drawable.food8,
            R.drawable.food9};

    private static Random random = new Random();

    public static int getAnPictureRes() {
        return images[random.nextInt(images.length)];
    }

}
