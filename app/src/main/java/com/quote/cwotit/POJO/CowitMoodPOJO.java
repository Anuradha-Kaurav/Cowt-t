package com.quote.cwotit.POJO;

import java.io.Serializable;

/**
 * Created by guruji on 5/12/2018.
 */

public class CowitMoodPOJO implements Serializable {
    String id;
    String category;
    String active;
    String imagePath;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
