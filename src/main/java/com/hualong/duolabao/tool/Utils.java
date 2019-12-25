package com.hualong.duolabao.tool;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019-11-19.
 */

public class Utils {

    private static final Logger log= LoggerFactory.getLogger(Utils.class);
    /**
     * <pre>
     *     得到当前时间的时间戳
     * </pre>
     * @return
     */
    public static String getTimeUnix() {
        return String.valueOf((new Date()).getTime()).substring(0,10);
    }

    /**
     * <pre>
     *     此方法更牛逼
     * </pre>
     * @param mothedName
     * @return
     */
    public static Map<String,String> LogRequest(String mothedName) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        //请求参数
        Enumeration<String> enumeration = request.getParameterNames();
        Map parmsMap= new HashMap<String,String>();
        String parmsString = "";
        while (enumeration.hasMoreElements()) {
            String paramName = (String) enumeration.nextElement();
            String paramValue = null;
            try {
                paramValue = URLDecoder.decode(request.getParameterValues(paramName)[0], "UTF-8")+"";
            } catch (UnsupportedEncodingException e) {
                paramValue="";
                System.out.println("解码出错了");
                log.error(Thread.currentThread().getStackTrace()[1].getMethodName()+"解码出错了：\n {} ",mothedName);
                e.printStackTrace();
            }
            parmsMap.put(paramName,paramValue );
            parmsString = parmsString + paramName + "=" + paramValue + "&";
        }
        log.info("接收到的请求参数 {} : {} ",mothedName,parmsString);
        return parmsMap;
    }
}
