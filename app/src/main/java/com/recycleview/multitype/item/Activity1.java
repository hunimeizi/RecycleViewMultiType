package com.recycleview.multitype.item;

import java.util.List;

/**
 * Created by Duo Nuo on 2017/4/12.
 */
public class Activity1 {
    public Activity1(List<Activity_item> activity_items) {
        this.activity_items = activity_items;
    }

    List<Activity_item> activity_items;

    public List<Activity_item> getActivity_items() {
        return activity_items;
    }

    public void setActivity_items(List<Activity_item> activity_items) {
        this.activity_items = activity_items;
    }
}