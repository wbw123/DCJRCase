package com.chase.dcjrCase.bean;

import java.util.ArrayList;

/**
 * Created by chase on 2017/10/9.
 */

public class CaseData {

    /**
     * data : {"caseData":[{"id":2016,"title":"汽车仪表总成静电整改案例","date":"2017-9-22 11:51","imgUrl":"/dcjr/case/caseImg/case_image16.jpg"},{"id":2015,"title":"隔离电源在高铁门控系统应用中共模浪涌拉弧案例分析","date":"2017-8-30 00:25","imgUrl":"/dcjr/case/caseImg/case_image15.jpg"},{"id":2014,"title":"GPS接口浪涌案例分析\u2014\u2014TVS单向和双向选择","date":"2017-8-23 08:15","imgUrl":"/dcjr/case/caseImg/case_image14.jpg"},{"id":2013,"title":"某车载显示屏辐射发射整改方案","date":"2017-8-8 08:02","imgUrl":"/dcjr/case/caseImg/case_image13.jpg"},{"id":2012,"title":"某军用系统屏蔽网线导致的辐射发射超标案例","date":"2017-8-4 07:47","imgUrl":"/dcjr/case/caseImg/case_image12.jpg"},{"id":2011,"title":"使用接地解决地环路干扰传导骚扰案例","date":"2017-8-3 15:06","imgUrl":"/dcjr/case/caseImg/case_image11.jpg"},{"id":2010,"title":"防护电路设计方案盲点分析不可忽略","date":"2017-7-15 06:59","imgUrl":"/dcjr/case/caseImg/case_image10.jpg"},{"id":2009,"title":"某医疗设备静电、辐射发射整改案例","date":"2017-7-11 08:10","imgUrl":"/dcjr/case/caseImg/case_image9.jpg"},{"id":2008,"title":"铁氧体磁环在广州地铁项目电磁兼容性中的应用","date":"2017-1-23 12:11","imgUrl":"/dcjr/case/caseImg/case_image8.jpg"},{"id":2007,"title":"路由器辐射骚扰分析与整改案例","date":"2017-1-23 10:41","imgUrl":"/dcjr/case/caseImg/case_image7.jpg"},{"id":2006,"title":"高铁和动车EMC设计与风险评估","date":"2017-1-23 09:27","imgUrl":"/dcjr/case/caseImg/case_image6.jpg"},{"id":2005,"title":"交流电机控制电路浪涌故障分析及整改","date":"2017-1-21 19:02","imgUrl":"/dcjr/case/caseImg/case_image5.jpg"},{"id":2004,"title":"电动汽车的系统级EMC设计","date":"2017-1-20 19:35","imgUrl":"/dcjr/case/caseImg/case_image4.jpg"},{"id":2003,"title":"路径分析解决安防摄像机浪涌问题","date":"2017-1-20 19:07","imgUrl":"/dcjr/case/caseImg/case_image3.jpg"},{"id":2002,"title":"摄像头静电故障原因分析及整改","date":"2017-1-20 18:57","imgUrl":"/dcjr/case/caseImg/case_image2.jpg"},{"id":2001,"title":"医疗产品静电抗扰度案例","date":"2017-1-20 18:42","imgUrl":"/dcjr/case/caseImg/case_image1.jpg"}]}
     * retcode : 200
     */

    public DataBean data;
    public int retcode;

    @Override
    public String toString() {
        return "CaseData{" +
                "data=" + data +
                ", retcode=" + retcode +
                '}';
    }

    public class DataBean {
        public ArrayList<CaseDataBean> caseData;

        @Override
        public String toString() {
            return "DataBean{" +
                    "caseData=" + caseData +
                    '}';
        }

        public class CaseDataBean {
            /**
             * id : 2016
             * title : 汽车仪表总成静电整改案例
             * date : 2017-9-22 11:51
             * imgUrl : /dcjr/case/caseImg/case_image16.jpg
             */

            public String id;
            public String title;
            public String date;
            public String imgUrl;
            @Override
            public String toString() {
                return "CaseDataBean{" +
                        "id='" + id + '\'' +
                        ", title='" + title + '\'' +
                        ", date='" + date + '\'' +
                        ", imgUrl='" + imgUrl + '\'' +
                        '}';
            }
        }
    }

}
