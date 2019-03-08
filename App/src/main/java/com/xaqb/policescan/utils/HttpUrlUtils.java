package com.xaqb.policescan.utils;

import android.content.Context;

/**
 * 接口地址
 */
public class HttpUrlUtils {
    private static HttpUrlUtils httpUrl = new HttpUrlUtils();

    public static HttpUrlUtils getHttpUrl() {
        return httpUrl;
    }



    private String getBaseUrl(Context context) {
        return SPUtils.get(context,"url","").toString();
    }
    /**
     * 登录
     * /v1/governor/login
     * @return
     */
    public String userLogin(Context context) {
        return getBaseUrl(context) + "/v1/governor/login";
    }

    /**
     * 修改密码
     * //http://jdws.qbchoice.cn/v1/governor/governor/update_pwd
     * @return
     */
    public String modifypws(Context context) {
        return getBaseUrl(context) + "/v1/governor/governor/update_pwd";
    }

    public String map(Context context) {
        return getBaseUrl(context) + "/v1/governor/company/position";
    }


    /**
     * 找回密码
     *  http://jdws.qbchoice.cnv1/governor/back_pwd
     * @return
     */
    public String back_pwd(Context context) {
        return getBaseUrl(context) + "/v1/governor/back_pwd";
    }


    /**
     * 获取短信验证码
     *  http://jdws.qbchoice.cn/v1/governor/send_sms
     * @return
     */
    public String getSmsCode(Context context) {
        return getBaseUrl(context) + "/v1/governor/send_sms";
    }

    public String getOrg(Context context) {
        return getBaseUrl(context) + "/v1/governor/securityorganization/search";
    }

    public String query_com(Context context) {
        return getBaseUrl(context) + "/v1/governor/company";
    }

    public String detail_com(Context context) {
        return getBaseUrl(context) + "/v1/governor/company/";
    }

    public String brandcode(Context context) {
        return getBaseUrl(context) + "/v1/governor/brandcode";
    }

    public String companycode(Context context) {
        return getBaseUrl(context) + "/v1/governor/company";
    }

    public String query_per(Context context) {
        return getBaseUrl(context) + "/v1/governor/employee";
    }

    public String detail_per(Context context) {
        return getBaseUrl(context) + "/v1/governor/employee/";
    }
    public String detail_org_down(Context context) {
        return getBaseUrl(context) + "/v1/governor/dashboard/statistical";
    }
    public String detail_org_up(Context context) {
        return getBaseUrl(context) + "/v1/governor/dashboard/index";
    }
    public String clue_list(Context context) {
        return getBaseUrl(context) + "/v1/governor/securityclue";
    }
    public String clue_del(Context context) {
        return getBaseUrl(context) + "/v1/governor/securityclue/";
    }

    public String bill_list(Context context) {
        return getBaseUrl(context) + "/v1/governor/querylog";
    }
    public String joint_list(Context context) {
        return getBaseUrl(context) + "/v1/governor/unioncheck";
    }
    public String bill_del(Context context) {
        return getBaseUrl(context) + "/v1/governor/expresscode/search/";
    }
    public String com_list(Context context) {
        return getBaseUrl(context) + "/v1/governor/dailycheck";
    }

    public String log_del(Context context) {
        return getBaseUrl(context) + "/v1/governor/querylog/";
    }
    public String banner_pic(Context context) {
        return getBaseUrl(context) + "/v1/governor/article/bbimg";
    }


}
