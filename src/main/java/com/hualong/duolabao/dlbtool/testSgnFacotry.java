package com.hualong.duolabao.dlbtool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hualong.duolabao.exception.ApiSysException;

import java.util.UUID;

/**
 * Created by Administrator on 2019-07-15.
 */
public class testSgnFacotry {

    public static void main(String[] args) {

        int a=1;
        System.out.println(String.valueOf((double) a/100));
//        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
//        System.out.println(uuid);
//        System.out.println(uuid.length());
//        test2();


//        test3();

//        for(int i=0;i<10;i++){
//            test2();
//        }
//        System.out.println("我到了最后");

    }

    private static void test3() {
        try{
            SignFacotry.decryptCipherJsonToRequest("", JSONObject.parseObject(""),null);
        }catch (ApiSysException e){
            e.printStackTrace();
            System.out.println("获取异常类型1 :"+e.getMessage());
            System.out.println("获取异常类型 :"+e.getExceptionEnum());


        }
    }

    private static void test2() {
        try{
            String jsonString="{\"merchantNo\":\"XILIAN\",\"cipherJson\":\"ae8bb26153c2db4f4cb16f51e4df3852f0fccb4d9475b03309569a7e8aa5729efb7cbae8228e6d178cc2dcbb0e82712e7840108dc5f3c4706c06bee060810d10211c56965a69ad4deda60fcf890cdbdddf0f67dc09d15d26a4fcf1b291713666dbf8b3db77151fc35d966754675e31df\"," +
                    "\"sign\":\"e67ca965f200495b0c545d7a679ee4bd\",\"systemId\":\"jdpay-offlinepay-isvaccess\"," +
                    "\"uuid\":\"79e6f115-fbe5-4f28-8c0a-6a0facbeee41\",\"tenant\":\"1519833291\",\"storeId\":\"1001\"}";
            //test1("D8C2313325E1E049FE19AFAC122B987F", JSON.parseObject(jsonString),"XILIAN");//正确的MD5key

            test1("D8C2313325E1E049FE19AFAC122B987F 1", JSON.parseObject(jsonString),"XILIAN");
            System.out.println("123");
        }catch (ApiSysException e){
            e.printStackTrace();
            System.out.println("获取异常类型1 :"+e.getMessage());
            System.out.println("获取异常类型 :"+e.getExceptionEnum());

        }
    }

    public static void verifySignAndMerchantNoTest(String md5Key, JSONObject jsonObject, String merchantNo) throws ApiSysException{
        SignFacotry.verifySignAndMerchantNo(md5Key, jsonObject, merchantNo);
    }

    public static void test1(String md5Key, JSONObject jsonObject, String merchantNo) throws ApiSysException{
        verifySignAndMerchantNoTest(md5Key, jsonObject, merchantNo);
    }
}
