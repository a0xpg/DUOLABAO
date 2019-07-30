package com.hualong.duolabao;

import com.alibaba.fastjson.JSONObject;
import com.hualong.duolabao.dao.cluster.CommDaoMapper;
import com.hualong.duolabao.domin.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019-07-19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommDaoMapperTest {
    private static final Logger log= LoggerFactory.getLogger(CommDaoMapperTest.class);

    @Autowired
    private CommDaoMapper commDaoMapper;

    /**
     * <pre>
     *     获取单号
     * </pre>
     */
    @Test
    public void getCommSheetNoTest(){
        commSheetNo commSheetNo=this.commDaoMapper.getCommSheetNo("002","81",
                "2019-07-19","posstation101.dbo.p_getPos_SerialNoSheetNo");
        log.info("我是得到的单号   "+ commSheetNo.getcSheetNo());
    }

    /**
     * <pre>
     *     保存单号
     * </pre>
     */
    @Test
    public void testSaveSheetNo(){
        Integer integer=this.commDaoMapper.p_saveSheetNo_Z_call("002",
                "81","2019-05-06","81201907190001",
                "32","posstation101.dbo.p_saveSheetNo");

        log.info("我是影响行数 "+ integer);
    }

    /**
     * <pre>
     *     得到生鲜规则
     * </pre>
     */
    @Test
    public void testposConfig(){
        posConfig posConfig=this.commDaoMapper.getposConfig("posstation101.dbo.Pos_Config","条码秤");
        log.info("我是获取到的结果集 "+ JSONObject.toJSONString(posConfig));
    }

    /**
     * <pre>
     *     从主库得到哆啦宝的配置信息
     *     条件: 购物车编号
     * </pre>
     */
    @Test
    public void testtDlbPosConfiguration(){
        tDlbPosConfiguration dlbPosConfiguration=this.commDaoMapper.gettDlbPosConfiguration("0002");
        log.info("我是获取到的结果集 "+ dlbPosConfiguration.toString());
        log.info("我是获取到的结果集 "+ dlbPosConfiguration.getCartId());
        log.info("我是获取到的结果集 "+ JSONObject.toJSONString(dlbPosConfiguration));
    }

    @Test
    public void testGetResultProc(){
        ResultProc resultProc=commDaoMapper.GetResultProc("812019072900024","0002",
                "posstation101.dbo.p_commitDataProcToJieSuanAndPOS_SaleSheetDetail_z");
        log.info("我是获取到的结果集 "+ JSONObject.toJSONString(resultProc));
    }

    @Test
    public void testInsertOrderSysnLog(){

        OrderSysnLog orderSysnLog=new OrderSysnLog("0002", "0002", "0002", "0002",
                new Long(123), "0002", "0002", "0002",
                "0002", "0002", "0002", "0002","0002");

        Integer integer=commDaoMapper.insert(orderSysnLog);
        log.info("我是影响行数  {} ", integer);
    }


}
