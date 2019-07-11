package com.hualong.duolabao.conntroller;


import com.alibaba.fastjson.JSONObject;
import com.hualong.duolabao.service.DlbService;
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


    @ResponseBody
    @RequestMapping(value = "/api/getGoods", method = {RequestMethod.POST,RequestMethod.GET}) //, produces = "application/json;charset=UTF-8"
    public String getCartInfo(@RequestBody JSONObject jsonParam) {
        log.info(this.getClass().getName()+" "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"获取的参数: {}",jsonParam.toJSONString());
        return "";
    }


    @ResponseBody
    @RequestMapping(value = "/api/getGoods", method = {RequestMethod.POST,RequestMethod.GET}) //, produces = "application/json;charset=UTF-8"
    public String getGoods(@RequestBody JSONObject jsonParam) {
        log.info(this.getClass().getName()+" "+ Thread.currentThread().getStackTrace()[1].getMethodName()+"获取的参数: {}",jsonParam.toJSONString());
        return "";
    }


    /**
     *
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
