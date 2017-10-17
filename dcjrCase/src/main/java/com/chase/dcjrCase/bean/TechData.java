package com.chase.dcjrCase.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZYK on 2017/10/16.
 */

public class TechData {

    /**
     * data : {"TechData":[{"id":4024,"title":"简述个人整理的emc整改思路","date":"2017-10-10 11:52","imgUrl":"/dcjr/tech/techImg/tech_image24.jpg"},{"id":4023,"title":"20年资深工程师，从7个方面分析开关电源的电路和EMC设计细节(下)","date":"2017-8-24 17:35","imgUrl":"/dcjr/tech/techImg/tech_image23.jpg"},{"id":4022,"title":"20年资深工程师，从7个方面分析开关电源的电路和EMC设计细节(上)","date":"2017-8-24 17:24","imgUrl":"/dcjr/tech/techImg/tech_image22.jpg"},{"id":4021,"title":"正确排查EMI问题的四大实用性技巧","date":"2017-7-12 07:42","imgUrl":"/dcjr/tech/techImg/tech_image21.jpg"},{"id":4020,"title":"汽车抗电磁干扰措施有哪些？","date":"2017-7-8 08:55","imgUrl":"/dcjr/tech/techImg/tech_image20.jpg"},{"id":4019,"title":"经典剖析电源PCB布板与EMC的关系（下）","date":"2017-1-21 23:56","imgUrl":"/dcjr/tech/techImg/tech_image19.jpg"},{"id":4018,"title":"经典剖析电源PCB布板与EMC的关系（上）","date":"2017-1-21 23:34","imgUrl":"/dcjr/tech/techImg/tech_image18.jpg"},{"id":4017,"title":"照明产品的电磁兼容（EMC）问题及检测技术","date":"2017-1-21 22:40","imgUrl":"/dcjr/tech/techImg/tech_image17.jpg"},{"id":4016,"title":"国外专家总结的医疗设各10个常见电磁干扰问题","date":"2017-1-21 22:10","imgUrl":"/dcjr/tech/techImg/tech_image16.jpg"},{"id":4015,"title":"数字电路PCB的EMI控制技术","date":"2017-1-21 21:56","imgUrl":"/dcjr/tech/techImg/tech_image15.jpg"},{"id":4014,"title":"详解常见差分信号PCB布局的三大误区","date":"2017-1-21 18:30","imgUrl":"/dcjr/tech/techImg/tech_image14.jpg"},{"id":4013,"title":"拿走不谢！268条PCB Layout设计规范（下）","date":"2017-1-21 17:34","imgUrl":"/dcjr/tech/techImg/tech_image13.jpg"},{"id":4012,"title":"拿走不谢！268条PCB Layout设计规范（中）","date":"2017-1-21 17:25","imgUrl":"/dcjr/tech/techImg/tech_image12.jpg"},{"id":4011,"title":"拿走不谢！268条PCB Layout设计规范（上）","date":"2017-1-21 17:09","imgUrl":"/dcjr/tech/techImg/tech_image11.jpg"},{"id":4010,"title":"滤波电容、去耦电容、旁路电容作用","date":"2017-1-21 16:33","imgUrl":"/dcjr/tech/techImg/tech_image10.jpg"},{"id":4009,"title":"让你从此远离EMC困扰的必备\u201c干粮\u201d！（下）","date":"2017-1-21 16:10","imgUrl":"/dcjr/tech/techImg/tech_image9.jpg"},{"id":4008,"title":"让你从此远离EMC困扰的必备\u201c干粮\u201d！（上）","date":"2017-1-21 15:58","imgUrl":"/dcjr/tech/techImg/tech_image8.jpg"},{"id":4007,"title":"高速电路回流路径分析（下）","date":"2017-1-21 12:07","imgUrl":"/dcjr/tech/techImg/tech_image7.jpg"},{"id":4006,"title":"高速电路回流路径分析（下）","date":"2017-1-21 10:49","imgUrl":"/dcjr/tech/techImg/tech_image6.jpg"},{"id":4005,"title":"开关电源经典问答","date":"2017-1-21 10:17","imgUrl":"/dcjr/tech/techImg/tech_image5.jpg"},{"id":4004,"title":"非常好的静电保护(ESD)原理和设计（下）","date":"2017-1-21 01:20","imgUrl":"/dcjr/tech/techImg/tech_image4.jpg"},{"id":4003,"title":"非常好的静电保护(ESD)原理和设计（上）","date":"2017-1-21 00:59","imgUrl":"/dcjr/tech/techImg/tech_image3.jpg"},{"id":4002,"title":"汽车电子产品EMC标准与常见问题解决方法","date":"2017-1-21 00:11","imgUrl":"/dcjr/tech/techImg/tech_image2.jpg"},{"id":4001,"title":"\u201c李尔平：发现电磁兼容的\u201c奥秘\u201d","date":"2016-10-12 21:29","imgUrl":"/dcjr/tech/techImg/tech_image1.jpg"}],"topTech":[{"id":401,"title":"正确排查EMI问题的四大实用性技巧","date":"2017-10-9 21:29","imgUrl":"/dcjr/tech/topTechImg/topTech_image1.jpg","url":"/dcjr/tech/tech21.html"},{"id":402,"title":"10个常见电磁干扰问题","date":"2017-1-21 22:10","imgUrl":"/dcjr/tech/topTechImg/topTech_image2.jpg","url":"/dcjr/tech/tech16.html"},{"id":403,"title":"268条PCB Layout设计规范（中）","date":"2017-1-21 17:25","imgUrl":"/dcjr/tech/topTechImg/topTech_image3.jpg","url":"/dcjr/tech/tech12.html"},{"id":404,"title":"汽车电子产品EMC标准与常见问题解决方法","date":"2017-1-21 00:11","imgUrl":"/dcjr/tech/topTechImg/topTech_image4.jpg","url":"/dcjr/tech/tech2.html"}]}
     * retcode : 200
     */

    public DataBean data;
    public int retcode;

    @Override
    public String toString() {
        return "TechData{" +
                "data=" + data +
                ", retcode=" + retcode +
                '}';
    }

    public class DataBean {
        public ArrayList<TechDataBean> techData;
        public ArrayList<TopTechBean> topTech;

        @Override
        public String toString() {
            return "DataBean{" +
                    "TechData=" + techData +
                    ", topTech=" + topTech +
                    '}';
        }

        public  class TechDataBean {
            /**
             * id : 4024
             * title : 简述个人整理的emc整改思路
             * date : 2017-10-10 11:52
             * imgUrl : /dcjr/tech/techImg/tech_image24.jpg
             */

            public String id;
            public String title;
            public String date;
            public String imgUrl;

            @Override
            public String toString() {
                return "TechDataBean{" +
                        "id='" + id + '\'' +
                        ", title='" + title + '\'' +
                        ", date='" + date + '\'' +
                        ", imgUrl='" + imgUrl + '\'' +
                        '}';
            }
        }

        public  class TopTechBean {
            /**
             * id : 401
             * title : 正确排查EMI问题的四大实用性技巧
             * date : 2017-10-9 21:29
             * imgUrl : /dcjr/tech/topTechImg/topTech_image1.jpg
             * url : /dcjr/tech/tech21.html
             */

            public String id;
            public String title;
            public String date;
            public String imgUrl;
            public String url;

            @Override
            public String toString() {
                return "TopTechBean{" +
                        "id='" + id + '\'' +
                        ", title='" + title + '\'' +
                        ", date='" + date + '\'' +
                        ", imgUrl='" + imgUrl + '\'' +
                        ", url='" + url + '\'' +
                        '}';
            }
        }
    }
}
