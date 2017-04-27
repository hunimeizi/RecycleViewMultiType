package com.recycleview.multitype.item;

import java.util.List;

/**
 * Created by Duo Nuo on 2017/4/27.
 */

public class Activity_item {
    private List<String> url_string;
    private List<String> title_string;

    public Activity_item(List<String> url_string, List<String> title_string) {
        this.url_string = url_string;
        this.title_string = title_string;
    }

    public List<String> getUrl_string() {
        return url_string;
    }

    public void setUrl_string(List<String> url_string) {
        this.url_string = url_string;
    }

    public List<String> getTitle_string() {
        return title_string;
    }

    public void setTitle_string(List<String> title_string) {
        this.title_string = title_string;
    }
}
