package com.hualong.duolabao.scheduled;




import com.hualong.duolabao.service.DlbService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.*;



@Component
@Configurable
@Configuration
public class MyStaticTask {

    private static final Logger logger = LoggerFactory.getLogger(MyStaticTask.class);



    @Autowired
    private DlbService dlbService;


    /**
     * 固定cron配置定时任务
     */
    @Scheduled(cron = "0/20 * * * * ?")
    public void doTask(){
        System.out.println("执行了MyStaticTask,时间为:"+new Date(System.currentTimeMillis()));
    }


    //每天凌晨执行
    @Scheduled(cron = "0 0 0 1/1 * ? ")
    public void updateLeftNumerUser(){
        try{
            dlbService.ExecS("delete");
            logger.info("删除临时购物车成功");
        }catch (Exception e){
            logger.error("删除临时购物车失败 : {}",e.getMessage());
        }
    }


    public static void main(String[] args) {

    }
}
