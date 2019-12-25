package com.hualong.duolabao.service;

import java.util.Map;

/**
 * Created by Administrator on 2019-12-13.
 */
public interface WxPayService {
    /**
     * 直连商户获取人脸识别的authInfo
     * @param data
     * @return
     */
    String getauthinfo(Map data);

    String getauthinfoByWx(Map<String, String> data);
}
