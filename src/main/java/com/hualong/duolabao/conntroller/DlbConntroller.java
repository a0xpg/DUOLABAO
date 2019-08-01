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
        log.info("我是请求路径 getUrlType urlType{}",urlType);
        log.info("我是请求路径 jsonParam urlType{}",JSONObject.toJSONString(jsonParam));

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
        log.info("我是请求路径 pay getPayUrlType urlType{}",urlType);
        log.info("我是请求路径 pay jsonParam urlType{}",JSONObject.toJSONString(jsonParam));

        return this.payService.CommUrlFun(urlType,jsonParam);
    }

}
