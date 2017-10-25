package com.chase.dcjrCase.bean;

/**
 * Created by chase on 2017/10/25.
 */

public class CollectionBean {

    public String id;
    public String title;
    public String date;
    public String imgUrl1="";
    public String imgUrl2="";
    public String imgUrl3="";
    public String imgUrl="";
    public String url;
    public String author;
    public String from;
    public String type;

    @Override
    public String toString() {
        return "CollectionBean{" +
                "id='" + id + '\'' +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", imgUrl1='" + imgUrl1 + '\'' +
                ", imgUrl2='" + imgUrl2 + '\'' +
                ", imgUrl3='" + imgUrl3 + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", url='" + url + '\'' +
                ", author='" + author + '\'' +
                ", from='" + from + '\'' +
                ", type=" + type +
                '}';
    }
}
