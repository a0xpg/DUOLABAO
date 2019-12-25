package com.hualong.duolabao.conntroller;

import com.hualong.duolabao.service.WxPayService;
import com.hualong.duolabao.tool.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2019-12-13.
 */
@RestController
@RequestMapping("/wxpay")
public class WxFacePayConntroller {
    private static final Logger log= LoggerFactory.getLogger(WxFacePayConntroller.class);

    @Autowired
    private WxPayService wxPayService;

    /**
     * 直连商户获取人脸识别的authInfo
     * @param sn
     * @return
     */
    @RequestMapping("/api/getauthinfo")
    @ResponseBody
    public  String getauthinfoCon(
            @RequestParam(value = "sn",required = false) String sn){
        return this.wxPayService.getauthinfo(Utils.LogRequest(Thread.currentThread().getStackTrace()[0].getClassName()+
                Thread.currentThread().getStackTrace()[0].getMethodName()+" 直连商户获取人脸识别的authInfo "));
    }

    /**
     * 直连商户获取人脸识别的authInfo  官方通道
     * @param sn
     * @return
     */
    @RequestMapping("/api/getauthinfoByWx")
    @ResponseBody
    public  String getauthinfoConByWx(
            @RequestParam(value = "sn",required = false) String sn){
        return this.wxPayService.getauthinfoByWx(Utils.LogRequest(Thread.currentThread().getStackTrace()[0].getClassName()+
                Thread.currentThread().getStackTrace()[0].getMethodName()+" 直连商户获取人脸识别的authInfo "));
    }

}
