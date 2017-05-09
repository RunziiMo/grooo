package net.azstudio.groooseller.model.app;

/**
 * Created by Wouldyou on 2015/6/18.
 */
public class BaiduPicture {
    private String id;
    private String image_url;

    @Override
    public String toString() {
        return "BaiduPicture{" +
                "id='" + id + '\'' +
                ", image_url='" + image_url + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
