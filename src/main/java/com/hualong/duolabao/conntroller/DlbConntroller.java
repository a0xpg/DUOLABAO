package com.hualong.duolabao.conntroller;


import com.alibaba.fastjson.JSONObject;
import com.hualong.duolabao.service.DlbService;
import com.hualong.duolabao.service.PayService;
import com.hualong.duolabao.service.PosService;
import org.apache.catalina.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by Administrator on 2019-07-02.
 */
@RestController
@RequestMapping("/hlyf")
public class DlbConntroller {
    private static final Logger log= LoggerFactory.getLogger(DlbConntroller.class);

    @Autowired
    private PosService posService;

    @Autowired
    private PayService payService;

    /**
     * <pre>
     *      请求统一处理配置
     * </pre>
     * @param urlType    路径
     * @param jsonParam  参数json数据
     * @return
     */
    @RequestMapping(value = "/api/{urlType}", method = {RequestMethod.POST}) //, produces = "application/json;charset=UTF-8"
    public String getUrlType(@PathVariable String urlType,
                           @RequestBody JSONObject jsonParam) {
        log.info("我是请求路径 getUrlType urlType {}",urlType);
        log.info("我是请求路径 jsonParam urlType {}",JSONObject.toJSONString(jsonParam));

        return this.posService.CommUrlFun(urlType,jsonParam);
    }


    /**
     * <pre>
     *      pay 扣款接口 查询接口 退款接口  统一封装
     * </pre>
     * @param urlType    路径
     * @param jsonParam  参数json数据
     * @return
     */
    @RequestMapping(value = "/api/pay/{urlType}", method = {RequestMethod.POST})
    public String getPayUrlType(@PathVariable String urlType,
                             @RequestBody JSONObject jsonParam) {
        log.info("我是请求路径 pay getPayUrlType urlType {}",urlType);
        log.info("我是请求路径 pay jsonParam urlType {}",JSONObject.toJSONString(jsonParam));

        //return this.payService.CommUrlFun(urlType,jsonParam);
        //2019-11-20  更改为新的版本的方法
        return this.payService.CommUrlFunV2(urlType,jsonParam);
    }



    @RequestMapping(value = "/api/test/test", method = {RequestMethod.POST})
    //, produces = "application/json;charset=UTF-8"
    public String getUrlType(@RequestBody JSONObject jsonParam,HttpServletRequest request) {

        log.info("我来了 TEST jsonParam  {}",JSONObject.toJSONString(jsonParam));
        String address=getIpAddr(request);
        log.info("我是地址  address  {}",address);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return address+": "+JSONObject.toJSONString(jsonParam);
    }

    //获取访问者的ip地址
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress="";
        }
        // ipAddress = this.getRequest().getRemoteAddr();

        return ipAddress;
    }

}
