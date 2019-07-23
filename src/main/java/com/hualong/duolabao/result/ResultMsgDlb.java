package com.hualong.duolabao.result;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hualong.duolabao.domin.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2019-05-24.
 */

@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
@ToString
public class ResultMsgDlb implements Serializable {

    private String merchantNo;
    private String cipherJson;
    private String sign;
    private boolean success;
    private String errCode;
    private String errCodeDes;
    private String systemId;
    private String uuid;

    public static String ResultMsgDlb(String merchantNo, String cipherJson, String sign,
                                      boolean success, String errCode, String errCodeDes, String systemId, String uuid){
        return JSONObject.toJSONString(new ResultMsgDlb(merchantNo, cipherJson, sign,
                                        success, errCode, errCodeDes, systemId, uuid)).toString();
    }

    public static String ResultMsgDlb(Request request, String cipherJson, String sign, String uuid){
        return JSONObject.toJSONString(new ResultMsgDlb(request.getMerchantNo(), cipherJson, sign,
                true, GlobalEumn.SUCCESS.getCode(), GlobalEumn.SUCCESS.getMesssage(), request.getSystemId(), uuid)).toString();
    }

    public static void main(String[] args) {


    }

}
