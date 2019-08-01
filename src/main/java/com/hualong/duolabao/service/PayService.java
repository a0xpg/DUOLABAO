package com.hualong.duolabao.service;

import com.alibaba.fastjson.JSONObject;
import com.hualong.duolabao.domin.Request;
import com.hualong.duolabao.exception.ApiSysException;
import com.hualong.duolabao.exception.ErrorEnum;


/**
 * Created by Administrator on 2019-07-31.
 */
public interface PayService {

    /**
     *<pre>
     *     请求统一处理
     *</pre>
     * @param urlType
     * @param jsonParam
     * @return
     * @throws ApiSysException
     */
    String CommUrlFun(String urlType,JSONObject jsonParam);

    String ResponseDlb(Request request, ErrorEnum errorEnum,String status);
}
