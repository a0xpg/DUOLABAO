package com.hualong.duolabao.service;

import com.alibaba.fastjson.JSONObject;
import com.hualong.duolabao.domin.Request;
import com.hualong.duolabao.exception.ApiSysException;
import com.hualong.duolabao.exception.ErrorEnum;

/**
 * Created by Administrator on 2019-11-18.
 */
public interface OwnService {

    String InsertSnConfig(String sn);

    String SelectSnConfig(String sn);

    String checkUpdate(String sn, String versionName);
}
