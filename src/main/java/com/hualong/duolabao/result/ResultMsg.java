package com.hualong.duolabao.result;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class ResultMsg implements Serializable {

    private boolean success;
    private String retCode;
    private String retMessage;
    private Object data;


    public static void GetTypeClass(String var0){
        if(var0.getClass().toString().equals("class java.lang.String")){

            if (var0!=null) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(var0);
                var0 = m.replaceAll("");
            }

            System.out.println(var0);
        } else {
            System.out.println("我不是字符串类型");
        }
    }

    public static <T> String ResultMsgSuccess(T data){

        return "";
    }

    public static void main(String[] args) {
        ResultMsg resultMsg = new ResultMsg(true,GlobalEumn.SSCO001001.getCode(),GlobalEumn.SSCO001001.getMesssage(),(String)null);

        //resultMsg = new ResultMsg(true,"1001","正确",(ResultMsg)resultMsg);

        String jsonString = JSON.toJSONString(resultMsg);
        System.out.println(jsonString);
        resultMsg = new ResultMsg(true,"1001","正确",(ResultMsg)null);

        jsonString = JSONObject.toJSONString(resultMsg);
        System.out.println(jsonString);



    }

}
