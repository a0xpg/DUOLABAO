package com.hualong.duolabao.conntroller;

import com.hualong.duolabao.service.OwnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2019-11-18.
 * <pre>
 *     自己根据哆啦宝的协议加的
 * </pre>
 */
@RestController
@RequestMapping("/own")
public class HlDlbContoller {

    private static final Logger log= LoggerFactory.getLogger(HlDlbContoller.class);

    @Autowired
    private OwnService  ownService;

    @RequestMapping(value = "/api/insertSnConfig", method = RequestMethod.POST)
    @ResponseBody
    public  String insertSnConfigC(@RequestParam(value = "sn",required = true) String sn){
        log.info("我是获取的参数 insertSnConfigC {} ",sn);
        return this.ownService.InsertSnConfig(sn);
    }

    @RequestMapping(value = "/api/selectSnConfig", method = RequestMethod.POST)
    @ResponseBody
    public  String selectSnConfig(@RequestParam(value = "sn",required = true) String sn){
        log.info("我是获取的参数 selectSnConfig {} ",sn);
        return this.ownService.SelectSnConfig(sn);
    }

}
