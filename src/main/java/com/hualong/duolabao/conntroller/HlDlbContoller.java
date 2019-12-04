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

    /**
     * <pre>
     *     上传机器机器序列号  后台配置机器先关配置信息
     * </pre>
     * @param sn
     * @return
     */
    @RequestMapping(value = "/api/insertSnConfig", method = RequestMethod.POST)
    @ResponseBody
    public  String insertSnConfigC(@RequestParam(value = "sn",required = true) String sn){
        log.info("我是获取的参数 insertSnConfigC {} ",sn);
        return this.ownService.InsertSnConfig(sn);
    }

    /**
     * <pre>
     *     查询机器配置信息的
     * </pre>
     * @param sn
     * @return
     */
    @RequestMapping(value = "/api/selectSnConfig", method = RequestMethod.POST)
    @ResponseBody
    public  String selectSnConfig(@RequestParam(value = "sn",required = true) String sn){
        log.info("我是获取的参数 selectSnConfig {} ",sn);
        return this.ownService.SelectSnConfig(sn);
    }

    /**
     * <pre>
     *     检查版本更新的 &
     *     TODO 这里还没写
     * </pre>
     * @param sn
     * @param versionName
     * @return
     */
    @RequestMapping(value = "/api/checkUpdateApk", method = RequestMethod.POST)
    @ResponseBody
    public  String checkUpdateApk(@RequestParam(value = "sn",required = true) String sn,
                                  @RequestParam(value = "versionName",required = true) String versionName){
        log.info("我是获取的参数 checkUpdateApk sn {} , versionName {} ",sn,versionName);

        return this.ownService.checkUpdate(sn,versionName);
    }

}
