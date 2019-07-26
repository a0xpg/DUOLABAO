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
 * Created by Administrator on 2019-07-23.
 */
@RestController
@RequestMapping("/hlyfTest")
public class TestConntroller {
    private static final Logger log= LoggerFactory.getLogger(TestConntroller.class);

    @Autowired
    private DlbService dlbService;

    @Autowired
    private PosService posService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public  String test(){
       return "你好啊";
    }

    /**
     *<pre>
     *     搭建项目测试用的
     *</pre>
     * @param cStoreNo       门店编号
     * @param cStoreName     门店名称
     * @return                门店集合
     */
    @RequestMapping(value = "/api/getStore", method = RequestMethod.POST)
    @ResponseBody
    public  String findStore(@RequestParam(value = "cStoreNo",required = false) String cStoreNo,
                             @RequestParam(value = "cStoreName",required = false) String cStoreName){
        try{
            List<Store> list =dlbService.get_cStroeS(cStoreNo);
            String result="";
            if(list!=null && !list.isEmpty()){
                try{
//                    result= listBean.getBeanJson(list);
                    result= JSONObject.toJSONString(list);
                    return result;
                }catch (Exception e){
                    e.printStackTrace();
                    log.info(e.getMessage());
                    return e.getMessage();
                }

            }else {
                return "";
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return e.getMessage();
        }
    }
}
