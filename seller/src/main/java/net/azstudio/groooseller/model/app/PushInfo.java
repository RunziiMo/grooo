package net.azstudio.groooseller.model.app;

/**
 * Created by runzii on 16-5-30.
 */
public class PushInfo {

    private String push_id;

    public PushInfo(String push_id) {
        this.push_id = push_id;
    }

    public String getPush_id() {
        return push_id;
    }

    public void setPush_id(String push_id) {
        this.push_id = push_id;
    }
}
