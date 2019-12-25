package com.hualong.duolabao.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hualong.duolabao.result.GlobalEumn;
import com.hualong.duolabao.result.ResultMsg;
import com.hualong.duolabao.sdkWX.HttpClientUtils;
import com.hualong.duolabao.sdkWX.WXPayUtil;
import com.hualong.duolabao.service.WxPayService;
import com.hualong.duolabao.tool.SHA1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.hualong.duolabao.tool.String_Tool.getTimeUnix;

/**
 * Created by Administrator on 2019-12-13.
 */
@Service
public class WxPayServiceImpl implements WxPayService {
    private static final Logger log= LoggerFactory.getLogger(WxPayService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getauthinfo(Map data) {

        String result= JSON.toJSONString(new ResultMsg(
                false, GlobalEumn.SSCO010003.getCode(),GlobalEumn.SSCO010003.getMesssage(),null));
        try{
            String timeUnix=getTimeUnix();
            Map<String,String> bodydata=new HashMap<>();
            bodydata.put("agentNum",(String)data.get("agentNum"));
            bodydata.put("customerNum",(String)data.get("customerNum"));
            bodydata.put("deviceNo",(String)data.get("deviceNo"));
            bodydata.put("rawData",((String)data.get("rawData")).replace(" ","+"));
//            bodydata.put("storeId",(String)data.get("storeId"));
//
//            bodydata.put("storeName","测试门店0005");
            String body= JSONObject.toJSONString(bodydata);
            log.info("payOrder 我是请求体携带的数据 {}",body);
            String url = "https://openapi.duolabao.com/v1/agent/payfield/facepay/getauthinfo";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("accessKey",(String) data.get("accessKey"));
            headers.set("timestamp",timeUnix);
            String sign="secretKey="+(String) data.get("secretKey")+"&timestamp="+timeUnix +
                    "&path=/v1/agent/payfield/facepay/getauthinfo&body="+body;
            sign= SHA1.encode(sign);
            headers.set("token",sign.toUpperCase());
            HttpEntity<String> entity = new HttpEntity<String>(body, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
            String resultV0 = responseEntity.getBody();
            log.info(Thread.currentThread().getStackTrace()[0].getClassName()+
                    Thread.currentThread().getStackTrace()[0].getMethodName()+"  :"+resultV0);
            result=JSON.toJSONString(new ResultMsg(
                    true, GlobalEumn.SUCCESS.getCode(),GlobalEumn.SUCCESS.getMesssage(),resultV0));
        }catch (Exception e){
            e.printStackTrace();
            log.info(Thread.currentThread().getStackTrace()[0].getClassName()+
                    Thread.currentThread().getStackTrace()[0].getMethodName()+" 出错了 :"+e.getMessage());
        }

        return result;
    }

    @Override
    public String getauthinfoByWx(Map<String, String> data) {
        String result= JSON.toJSONString(new ResultMsg(
                false, GlobalEumn.SSCO010003.getCode(),GlobalEumn.SSCO010003.getMesssage(),null));
        try{
            String timeUnix=getTimeUnix();
            Map<String,String> bodydata=new HashMap<>();
            bodydata.put("store_id",(String)data.get("store_id"));
            bodydata.put("store_name",(String)data.get("store_name"));
            bodydata.put("device_id",(String)data.get("device_id"));
            bodydata.put("rawdata",((String)data.get("rawdata")).replace(" ","+"));
            bodydata.put("mch_id",(String)data.get("mch_id"));
            bodydata.put("appid",(String)data.get("appid"));
            bodydata= HttpClientUtils.fillRequestData(bodydata,"ABCDEFG123456789987654321UTRON88");

            log.info(Thread.currentThread().getStackTrace()[0].getClassName()+
                    Thread.currentThread().getStackTrace()[0].getMethodName()
                    +" 请求微信的参数 :"+JSONObject.toJSONString(bodydata));

            String resultV0=HttpClientUtils.requestOnce("123",
                    "https://payapp.weixin.qq.com/face/get_wxpayface_authinfo",
                   WXPayUtil.mapToXml(bodydata),2000,2000);

            Map<String, String>  map=WXPayUtil.xmlToMap(resultV0);
            resultV0=JSONObject.toJSONString(map);
            log.info(Thread.currentThread().getStackTrace()[0].getClassName()+
                    Thread.currentThread().getStackTrace()[0].getMethodName()+"  :"+resultV0);
            result=JSON.toJSONString(new ResultMsg(
                    true, GlobalEumn.SUCCESS.getCode(),GlobalEumn.SUCCESS.getMesssage(),resultV0));
        }catch (Exception e){
            e.printStackTrace();
            log.info(Thread.currentThread().getStackTrace()[0].getClassName()+
                    Thread.currentThread().getStackTrace()[0].getMethodName()+" 出错了 :"+e.getMessage());
        }

        return result;
    }
}
