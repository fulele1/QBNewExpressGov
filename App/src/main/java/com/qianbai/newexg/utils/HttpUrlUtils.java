package com.qianbai.newexg.utils;

/**
 * 接口地址
 */
public class HttpUrlUtils {
    private static HttpUrlUtils httpUrl = new HttpUrlUtils();

    public static HttpUrlUtils getHttpUrl() {
        return httpUrl;
    }

//    private String getBaseUrl() {
//        return "http://demo.0-w.cc";
//    }


    private String getBaseUrl() {
        return "http://jdws.qbchoice.cn";
    }

    /**
     * 登录
     * /v1/governor/login
     * @return
     */
    public String userLogin() {
        return getBaseUrl() + "/v1/governor/login";
    }

    /**
     * 品牌
     * www.newexpress.com/v1/common/data/getBranch
     *
     * @return
     */
    public String getBrand() {
        return getBaseUrl() + "common/data/getBranch";
    }


    /**
     * 管辖机构
     * www.newexpress.com/v1/common/data/securityorg
     *
     * @return
     */
    public String getOrg() {
        return getBaseUrl() + "common/data/securityorg";
    }

    /**
     * 企业查询
     * www.newexpress.com/v1/governor/company/search
     *
     * @return
     */
    public String query_com() {
        return getBaseUrl() + "/v1/governor/company";
    }

    /**
     * /v1/governor/company/：id
     *
     * @return
     */
    public String detail_com() {
        return getBaseUrl() + "/v1/governor/company/";
    }

    /**
     * /v1/governor/brandcode
     *
     * @return
     */
    public String brandcode() {
        return getBaseUrl() + "/v1/governor/brandcode";
    }

    /**
     * www.newexpress.com/v1/governor/staff/search
     * 人员查询
     * /v1/governor/employee
     * @return
     */
    public String query_per() {
        return getBaseUrl() + "/v1/governor/employee";
    }

    /**
     * /v1/governor/employee/:id
     *
     * @return
     */
    public String detail_per() {
        return getBaseUrl() + "/v1/governor/employee/";
    }





    /**
     * v1/governor/securityclue
     *
     * @return
     */
    public String clue_list() {
        return getBaseUrl() + "/v1/governor/securityclue";
    }

    /**
     * v1/governor/securityclue/:id
     *
     * @return
     */
    public String clue_del() {
        return getBaseUrl() + "/v1/governor/securityclue/";
    }



    /**
     * v1/governor/querylog
     *
     * @return
     */
    public String log_list() {
        return getBaseUrl() + "/v1/governor/querylog";
    }

    /**
     * v1/governor/securityclue/:id
     *
     * @return
     */
    public String log_del() {
        return getBaseUrl() + "/v1/governor/querylog";
    }






}
