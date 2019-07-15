package com.hualong.duolabao.dlbtool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hualong.duolabao.exception.ApiSysException;

/**
 * Created by Administrator on 2019-07-15.
 */
public class testSgnFacotry {

    public static void main(String[] args) {
        try{
            test1("", JSON.parseObject(""),"");
            System.out.println("123");
        }catch (ApiSysException e){
            e.printStackTrace();
            System.out.println(e.getExceptionEnum());
        }

    }

    public static void verifySignAndMerchantNoTest(String md5Key, JSONObject jsonObject, String merchantNo) throws ApiSysException{
        SignFacotry.verifySignAndMerchantNo(md5Key, jsonObject, merchantNo);
    }

    public static void test1(String md5Key, JSONObject jsonObject, String merchantNo) throws ApiSysException{
        verifySignAndMerchantNoTest(md5Key, jsonObject, merchantNo);
    }
}
