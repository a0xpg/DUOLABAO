package com.hualong.duolabao;

import com.alibaba.fastjson.JSONObject;
import com.hualong.duolabao.dao.cluster.MemberInfoMapper;
import com.hualong.duolabao.dao.cluster.tDLBGoodsInfoMapper;
import com.hualong.duolabao.domin.VipOffine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Administrator on 2019-07-22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberInfoMapperTest {

    private static final Logger log= LoggerFactory.getLogger(MemberInfoMapperTest.class);

    @Autowired
    private MemberInfoMapper memberInfoMapper;

    @Test
    public void testGetVipOffine(){
        VipOffine vipOffine=memberInfoMapper.Get_VipOffine("13628672210");
        if(vipOffine!=null){
            log.info("获取到的结果是  {}", JSONObject.toJSON(vipOffine));
        }else{
            log.info("结果集为空");
        }
    }
}
