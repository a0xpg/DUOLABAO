package com.hualong.duolabao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hualong.duolabao.config.DlbConnfig;
import com.hualong.duolabao.dao.cluster.DlbDao;
import com.hualong.duolabao.dao.cluster.tDLBGoodsInfoMapper;
import com.hualong.duolabao.dao.pos.PosMain;
import com.hualong.duolabao.domin.BLBGoodsInfo;
import com.hualong.duolabao.domin.CartInfo;
import com.hualong.duolabao.domin.MemberInfo;
import com.hualong.duolabao.result.GlobalEumn;
import com.hualong.duolabao.result.ResultMsg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2019-07-17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DLBGoodsInfoMapperTest {

    private static final Logger log= LoggerFactory.getLogger(DLBGoodsInfoMapperTest.class);

    @Autowired
    private DlbDao dlbDao;

    @Autowired
    private DlbConnfig dlbConnfig;

    @Autowired
    private PosMain posMain;

    @Autowired
    private tDLBGoodsInfoMapper dlbGoodsInfoMapper;

    @Test
    public void getDLBGoodsInfo(){
        BLBGoodsInfo blbGoodsInfo=dlbGoodsInfoMapper.getOneBLBGoodsInfo("002","002","00202");
        if(blbGoodsInfo!=null){
            log.info("获取到的结果是  {}", JSON.toJSON(blbGoodsInfo).toString());
        }else{
            log.info("结果集为空");
        }

    }

    @Test
    public void getDLBGoodsInfos(){
        List<BLBGoodsInfo> blbGoodsInfo=dlbGoodsInfoMapper.selectAll("002","002");

        MemberInfo memberInfo=new MemberInfo();
        CartInfo cartInfo=new CartInfo("123", "123", "123", "568",
                589, 966, 985, blbGoodsInfo, memberInfo);

        String s1=JSON.toJSONString(cartInfo, SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.PrettyFormat);

        System.out.println(s1);

        ResultMsg resultMsg=new ResultMsg(true, GlobalEumn.SUCCESS.getCode(),GlobalEumn.SUCCESS.getMesssage(),cartInfo);

        s1=JSON.toJSONString(resultMsg, SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.PrettyFormat);
        System.out.println("我是格式化的: "+s1);


        s1=JSON.toJSONString(resultMsg, SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullBooleanAsFalse);
        System.out.println("我不是格式化 ： "+s1);

        if(blbGoodsInfo!=null){
            log.info("获取到的结果是  {}", s1);
        }else{
            log.info("结果集为空");
        }

    }

    @Test
    public void insertDLBGoodsInfos(){

        BLBGoodsInfo blbGoodsInfo=new BLBGoodsInfo("002", "002", "002", "002",
                "002", null, null,
                "00205", "测试商品5", 6000000, 0,
                null,  6000000, 6000000,
                1, 1.069, true,
                "00205", "个");

        Integer integer=dlbGoodsInfoMapper.insert(blbGoodsInfo);
        if(integer!=null){
            log.info("我是影响行数  {}", integer);
        }else{
            log.info("结果集为空");
        }

    }

    @Test
    public void deleteDLBGoodsInfos(){

        Integer integer=dlbGoodsInfoMapper.deleteBLBGoodsInfo("002","002","00203","3");
        if(integer!=null){
            log.info("我是影响行数  {}", integer);
        }else{
            log.info("结果集为空");
        }
    }

    @Test
    public void updateDLBGoodsInfos(){

        BLBGoodsInfo blbGoodsInfo=new BLBGoodsInfo("002", "002", "002", "002",
                "002", null, null,
                "00205", "测试商品5", 12000000, 0,
                null,  6000000, 6000000,
                2, 1.069, true,
                "00205", "个");
        Integer integer=dlbGoodsInfoMapper.updateByBLBGoodsInfo(blbGoodsInfo);
        if(integer!=null){
            log.info("我是影响行数  {}", integer);
        }else{
            log.info("结果集为空");
        }
    }

}
