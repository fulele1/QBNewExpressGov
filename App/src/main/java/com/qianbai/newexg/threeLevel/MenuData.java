package com.qianbai.newexg.threeLevel;

import java.io.Serializable;

/**
 * Created by LaiYingtang on 2016/5/22.
 * 列数据的bean
 */
public class MenuData implements Serializable {
    public String id;
    public String name;
    public String flag;

    public MenuData(String id, String name, String flag) {
        this.id = id;
        this.name = name;
        this.flag = flag;
    }

    public MenuData() {
    }
}
