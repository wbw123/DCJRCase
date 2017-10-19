package com.chase.dcjrCase.bean;

/**
 * Created by ZYK on 2017/10/19.
 */

public class HistoryBean {
    public String id;
    public String title;
    public String date;
    public String imgUrl;
    public String url;

    @Override
    public String toString() {
        return "HistoryBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
