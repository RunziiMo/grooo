package com.wenym.grooo.util.RxEvent;


/**
 * Created by runzii on 16-5-23.
 */
public class ScrollEvent {

    private int state;

    public ScrollEvent(int state) {
        this.state = state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
