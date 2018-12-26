package com.vtyc.entity;


import java.io.Serializable;

public class Workcenter implements Serializable {

    private String wc_wkctr;
    private String wc_desc;

    public String getWc_wkctr() {
        return wc_wkctr;
    }

    public void setWc_wkctr(String wc_wkctr) {
        this.wc_wkctr = wc_wkctr;
    }

    public String getWc_desc() {
        return wc_desc;
    }

    public void setWc_desc(String wc_desc) {
        this.wc_desc = wc_desc;
    }
}
