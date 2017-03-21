package com.dgkj.tianmi.bean;

/** 实际需要的主题实体类
 * Created by Android004 on 2017/3/14.
 */

public class Theme {
    private String title;
    private int id;
    private boolean isChecked;
    private int plateId;

    public int getPlateId() {
        return plateId;
    }

    public void setPlateId(int plateId) {
        this.plateId = plateId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean check) {
        isChecked = check;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
