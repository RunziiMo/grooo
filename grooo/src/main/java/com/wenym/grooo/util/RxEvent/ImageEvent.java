package com.wenym.grooo.util.RxEvent;


/**
 * Created by runzii on 16-5-23.
 */
public class ImageEvent {

    private String imageUrl;
    private int color;

    public String getImageUrl() {
        return imageUrl;
    }

    public int getColor() {
        return color;
    }

    public ImageEvent(String imageUrl, int color) {
        this.imageUrl = imageUrl;
        this.color = color;
    }
}
