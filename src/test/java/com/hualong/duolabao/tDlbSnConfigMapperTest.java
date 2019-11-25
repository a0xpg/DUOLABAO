package com.hualong.duolabao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hualong.duolabao.dao.cluster.tDlbSnConfigMapper;
import com.hualong.duolabao.domin.hldomin.tDlbSnConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * Created by Administrator on 2019-11-18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class tDlbSnConfigMapperTest {
    private static final Logger log= LoggerFactory.getLogger(tDlbSnConfigMapperTest.class);

    @Autowired
    private tDlbSnConfigMapper tDlbSnConfigMapper;

    @Test
    public void testInsertBySn(){
        int i=0;
        try{
           i= tDlbSnConfigMapper.insertBySn("123456");
            log.info("我是 影响行数  {}",i);
        }catch (Exception e){
            e.printStackTrace();
            log.info("我是 出错了   {}",e.getMessage());
        }
    }

    @Test
    public void testSelctBySn(){
        tDlbSnConfig tDlbSnConfig=tDlbSnConfigMapper.selectByPrimaryKey("123456");
        if(tDlbSnConfig!=null){
            log.info("获取到的结果是  {}", JSON.toJSONString(tDlbSnConfig, SerializerFeature.WriteNullListAsEmpty,
                    SerializerFeature.WriteNullNumberAsZero,
                    SerializerFeature.WriteNullBooleanAsFalse,SerializerFeature.WriteMapNullValue));
        }else{
            log.info("结果集为空");
        }

    }
}
