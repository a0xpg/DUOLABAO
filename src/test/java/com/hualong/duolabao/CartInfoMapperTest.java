package com.hualong.duolabao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hualong.duolabao.config.DlbConnfig;
import com.hualong.duolabao.dao.cluster.DlbDao;
import com.hualong.duolabao.dao.cluster.tDLBGoodsInfoMapper;
import com.hualong.duolabao.dao.pos.PosMain;
import com.hualong.duolabao.dlbtool.SignFacotry;
import com.hualong.duolabao.domin.FrushGood;
import com.hualong.duolabao.domin.Request;
import com.hualong.duolabao.domin.cStoreGoods;
import com.hualong.duolabao.domin.commSheetNo;
import com.hualong.duolabao.exception.ApiSysException;
import com.hualong.duolabao.exception.ErrorEnum;
import com.hualong.duolabao.service.PosService;
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
 * Created by Administrator on 2019-07-22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CartInfoMapperTest {

    private static final Logger log= LoggerFactory.getLogger(CartInfoMapperTest.class);

    private static final String data ="{\"merchantNo\":\"XILIAN\",\"cipherJson\":\"ae8bb26153c2db4f4cb16f51e4df3852f0fccb4d9475b03309569a7e8aa5729efb7cbae8228e6d178cc2dcbb0e82712e7840108dc5f3c4706c06bee060810d10211c56965a69ad4deda60fcf890cdbdddf0f67dc09d15d26a4fcf1b291713666dbf8b3db77151fc35d966754675e31df\"," +
            "\"sign\":\"e67ca965f200495b0c545d7a679ee4bd\",\"systemId\":\"jdpay-offlinepay-isvaccess\"," +
            "\"uuid\":\"79e6f115-fbe5-4f28-8c0a-6a0facbeee41\",\"tenant\":\"1519833291\",\"storeId\":\"1001\"}";

    @Autowired
    private DlbDao dlbDao;

    @Autowired
    private DlbConnfig dlbConnfig;

    @Autowired
    private PosMain posMain;

    @Autowired
    private tDLBGoodsInfoMapper dlbGoodsInfoMapper;

    @Autowired
    private PosService posService;
    /**
     * <pre>
     *     查询商品并且插入到购物车  然后从购物车再读取出来进行签名
     * </pre>
     */
    @Test
    public void selectGood(){
       try{
           JSONObject json=JSON.parseObject(data);
           SignFacotry.verifySignAndMerchantNo(dlbConnfig.getMdkey(),json,dlbConnfig.getMerchantno());
           JSONObject jsonObject=SignFacotry.decryptCipherJson(dlbConnfig.getDeskey(),json);
           log.info("解析出来的数据 {}",jsonObject);
           List<String> list=new ArrayList<>();
           list.add("6956553400443");
           List<cStoreGoods> storeGoodsList=posService.GetcStoreGoodsS("0002",list);
           SignFacotry.GoodListIsEmpty(storeGoodsList);
           log.info("获取出来的商品是 {}",JSONObject.toJSON(storeGoodsList).toString());

//           SaveGoods(String storeId, String cashierNo,String sn,String cartId,String cartFlowNo,
//                   List<cStoreGoods> cStoreGoodsList,
//                   FrushGood frushGood)
           posService.SaveGoods(
                   jsonObject.getString("storeId"),
                   jsonObject.getString("cashierNo"),
                   jsonObject.getString("sn"),
                   jsonObject.getString("cartId"),
                   jsonObject.getString("cartFlowNo"),
                   storeGoodsList,new FrushGood(false));
       }catch (ApiSysException e){
           e.printStackTrace();
           log.error("出错了 ",e.getExceptionEnum().toString());
           log.error("出错了 ",e.getMessage());

       }
    }


    /**
     * <pre>
     *     调用二次封装实体
     * </pre>
     */
    @Test
    public void selectGoodTwo(){
        try{
            JSONObject json=JSON.parseObject(data);
            SignFacotry.verifySignAndMerchantNo(dlbConnfig.getMdkey(),json,dlbConnfig.getMerchantno());
            JSONObject jsonObject=SignFacotry.decryptCipherJson(dlbConnfig.getDeskey(),json);
            log.info("解析出来的数据 {}",jsonObject);
            Request request=SignFacotry.decryptCipherJsonToRequest(dlbConnfig.getDeskey(),json, ErrorEnum.SSCO010015);
            List<String> list=new ArrayList<>();
            list.add("6956553400443");
            List<cStoreGoods> storeGoodsList=posService.GetcStoreGoodsS("0002",list);
            SignFacotry.GoodListIsEmpty(storeGoodsList);
            log.info("获取出来的商品是 {}",JSONObject.toJSON(storeGoodsList).toString());
            posService.SaveGoodsToCartInfo(
                    request,
                    storeGoodsList,new FrushGood(false));
        }catch (ApiSysException e){
            e.printStackTrace();
            log.error("出错了 ",e.getExceptionEnum().toString());
            log.error("出错了 ",e.getMessage());

        }
    }

}