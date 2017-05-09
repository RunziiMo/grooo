package net.azstudio.groooseller.model.app;

import java.io.Serializable;

public class CategoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public int id;

    public String name;

    public String images;

    public int resImage;

    public CategoryEntity(int id, String name, int resImage) {
        super();
        this.id = id;
        this.name = name;
        this.resImage = resImage;
    }

}
