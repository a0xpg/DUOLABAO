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
     *     该版本是V1
     *</pre>
     * @param urlType
     * @param jsonParam
     * @return
     * @throws ApiSysException
     */
    String CommUrlFun(String urlType,JSONObject jsonParam);

    /**
     * <pre>
     *     版本 2.0
     *     描述:版本号V2 这是对以上方法进行的优化
     *     说明:修正了 在并发的时间 dlbPayConnfig 这个单列对象会引起并发串单的问题
     * </pre>
     * @param urlType
     * @param jsonParam
     * @return
     */
    String CommUrlFunV2(String urlType,JSONObject jsonParam);

    String ResponseDlb(Request request, ErrorEnum errorEnum,String status);
}
