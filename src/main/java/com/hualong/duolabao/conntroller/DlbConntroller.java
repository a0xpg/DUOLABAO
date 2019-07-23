package com.hualong.duolabao.conntroller;


import com.alibaba.fastjson.JSONObject;
import com.hualong.duolabao.service.DlbService;
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
    private DlbService dlbService;

    @Autowired
    private PosService posService;

    /**
     * <pre>
     *
     * </pre>
     * @param urlType    路径
     * @param jsonParam  参数json数据
     * @return
     */
    @RequestMapping(value = "/api/{urlType}", method = {RequestMethod.POST},produces = "application/json;charset=UTF-8") //, produces = "application/json;charset=UTF-8"
    public String getGoods(@PathVariable String urlType,
                           @RequestBody JSONObject jsonParam) {
        return this.posService.CommUrlFun(urlType,jsonParam);

    }
}
