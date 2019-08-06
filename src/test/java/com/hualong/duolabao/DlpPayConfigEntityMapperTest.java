package com.hualong.duolabao;

import com.alibaba.fastjson.JSON;
import com.hualong.duolabao.config.DlbConnfig;
import com.hualong.duolabao.dao.cluster.DlbDao;
import com.hualong.duolabao.dao.cluster.DlpPayConfigEntityMapper;
import com.hualong.duolabao.dao.cluster.tDLBGoodsInfoMapper;
import com.hualong.duolabao.dao.pos.PosMain;
import com.hualong.duolabao.domin.BLBGoodsInfo;
import com.hualong.duolabao.domin.payentity.DlpPayConfigEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Administrator on 2019-08-02.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DlpPayConfigEntityMapperTest {
    private static final Logger log= LoggerFactory.getLogger(DlpPayConfigEntityMapperTest.class);

    @Autowired
    private DlpPayConfigEntityMapper dlpPayConfigEntityMapper;


    @Test
    public void getDLBGoodsInfo(){
        DlpPayConfigEntity dlpPayConfigEntity=dlpPayConfigEntityMapper.selectByPrimaryKey("0002","0002",null);
        if(dlpPayConfigEntity!=null){
            log.info("获取到的结果是  {}", JSON.toJSON(dlpPayConfigEntity).toString());
        }else{
            log.info("结果集为空");
        }

    }
}
