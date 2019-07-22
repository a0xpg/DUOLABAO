package com.hualong.duolabao.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hualong.duolabao.config.DlbConnfig;
import com.hualong.duolabao.conntroller.DlbConntroller;
import com.hualong.duolabao.dao.cluster.DlbDao;
import com.hualong.duolabao.dao.cluster.MemberInfoMapper;
import com.hualong.duolabao.dao.cluster.tDLBGoodsInfoMapper;
import com.hualong.duolabao.dao.pos.PosMain;
import com.hualong.duolabao.domin.*;
import com.hualong.duolabao.exception.ApiSysException;
import com.hualong.duolabao.exception.ErrorEnum;
import com.hualong.duolabao.result.GlobalEumn;
import com.hualong.duolabao.result.ResultMsg;
import com.hualong.duolabao.service.PosService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2019-07-15.
 */
@Service
public class PosServiceImpl implements PosService {

    private static final Logger log= LoggerFactory.getLogger(PosServiceImpl.class);

    @Autowired
    private DlbDao dlbDao;

    @Autowired
    private DlbConnfig dlbConnfig;

    @Autowired
    private PosMain posMain;

    @Autowired
    private tDLBGoodsInfoMapper dlbGoodsInfoMapper;

    @Autowired
    private MemberInfoMapper memberInfoMapper;


    @Override
    public List<cStoreGoods> GetcStoreGoodsS(String cStoreNo, List<String> barcodeList) throws ApiSysException {

        List<cStoreGoods> list=null;
        try {
            if(!this.dlbConnfig.getIsdandian()){
                //单店的走这里
                list=this.posMain.GetcStoreGoods(cStoreNo, barcodeList);
            }else {
                //连锁的走这里
                list=this.posMain.GetcStoreGoodsDanDian(cStoreNo, barcodeList);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName()+"获取本地商品数据出错了 {}",e.getMessage());
            throw  new ApiSysException(ErrorEnum.SSCO001001);
        }

        return list;
    }

    @Override
    public void SaveGoods(String storeId, String cashierNo,String sn,String cartId,String cartFlowNo,
                          List<cStoreGoods> cStoreGoodsList,
                          FrushGood frushGood) throws ApiSysException {
        try{
            //得到商品信息
            cStoreGoods storeGoods=cStoreGoodsList.get(0);
            log.info("查询到的商品信息  {}", JSON.toJSON(storeGoods).toString());
            //如果是生鲜 直接保存到购物车
            if(frushGood.isWeight()){
                CommonServiceImpl.insertBlbGoodsInfo(dlbGoodsInfoMapper,
                                                    new BLBGoodsInfo(storeId, sn, cartId, cartFlowNo,
                                                    cashierNo, null, null,
                                                    storeGoods.getCGoodsNo(), storeGoods.getCGoodsName(),
                                                    frushGood.getAllMoney(), 0, null,
                                                    frushGood.getPrice(),frushGood.getPrice(),
                                                    0, frushGood.getWeight(), true,
                                                    storeGoods.getCBarcode(), "kg"));
            }else {
                //检测购物车是否存在改商品
                BLBGoodsInfo blbGoodsInfo=this.dlbGoodsInfoMapper.getOneBLBGoodsInfo(cartId,storeId,storeGoods.getCBarcode());

                Long NomalPrice=new Double(storeGoods.getFNormalPrice()).longValue()*100;

                //如果没有改商品保存的购物车   如果存在就更新该购物车
                if(blbGoodsInfo!=null){
                    int num=blbGoodsInfo.getQty()+1;
                    Long amount=NomalPrice*num;
                    CommonServiceImpl.updateBlbGoodsInfo(dlbGoodsInfoMapper,
                                                    new BLBGoodsInfo(storeId, sn, cartId, cartFlowNo,
                                                    cashierNo, null, null,
                                                    storeGoods.getCGoodsNo(), storeGoods.getCGoodsName(),
                                                            amount,
                                                    0, null,
                                                            NomalPrice,NomalPrice,
                                                    blbGoodsInfo.getQty()+1, 0, false,
                                                    storeGoods.getCBarcode(), "个"));
                }else{

                    CommonServiceImpl.insertBlbGoodsInfo(dlbGoodsInfoMapper,
                            new BLBGoodsInfo(storeId, sn, cartId, cartFlowNo,
                                    cashierNo, null, null,
                                    storeGoods.getCGoodsNo(), storeGoods.getCGoodsName(),
                                    NomalPrice, 0,
                                    null,  NomalPrice,NomalPrice,
                                    1, 0, false,
                                    storeGoods.getCBarcode(), "个"));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询商品的时间出错了:  {}",e.getMessage());
            throw  new ApiSysException(ErrorEnum.SSCO001001);
        }
    }


    @Override
    public void SaveGoodsToCartInfo(Request request,
                          List<cStoreGoods> cStoreGoodsList,
                          FrushGood frushGood) throws ApiSysException {
        try{
            //得到商品信息
            cStoreGoods storeGoods=cStoreGoodsList.get(0);
            log.info("查询到的商品信息  {}", JSON.toJSON(storeGoods).toString());
            //如果是生鲜 直接保存到购物车
            if(frushGood.isWeight()){
                CommonServiceImpl.insertBlbGoodsInfo(dlbGoodsInfoMapper,
                        new BLBGoodsInfo(request.getStoreId(), request.getSn(), request.getCartId(), request.getCartFlowNo(),
                                request.getCashierNo(), null, null,
                                storeGoods.getCGoodsNo(), storeGoods.getCGoodsName(),
                                frushGood.getAllMoney(), 0, null,
                                frushGood.getPrice(),frushGood.getPrice(),
                                0, frushGood.getWeight(), true,
                                storeGoods.getCBarcode(), "kg"));
            }else {
                //检测购物车是否存在改商品
                BLBGoodsInfo blbGoodsInfo=this.dlbGoodsInfoMapper.getOneBLBGoodsInfo(request.getCartId(),request.getStoreId(),storeGoods.getCBarcode());

                Long NomalPrice=new Double(storeGoods.getFNormalPrice()).longValue()*100;

                //如果没有改商品保存的购物车   如果存在就更新该购物车
                if(blbGoodsInfo!=null){
                    int num=blbGoodsInfo.getQty()+1;
                    Long amount=NomalPrice*num;
                    CommonServiceImpl.updateBlbGoodsInfo(dlbGoodsInfoMapper,
                            new BLBGoodsInfo(request.getStoreId(), request.getSn(), request.getCartId(), request.getCartFlowNo(),
                                    request.getCashierNo(), null, null,
                                    storeGoods.getCGoodsNo(), storeGoods.getCGoodsName(),
                                    amount,
                                    0, null,
                                    NomalPrice,NomalPrice,
                                    blbGoodsInfo.getQty()+1, 0, false,
                                    storeGoods.getCBarcode(), "个"));
                }else{

                    CommonServiceImpl.insertBlbGoodsInfo(dlbGoodsInfoMapper,
                            new BLBGoodsInfo(request.getStoreId(), request.getSn(), request.getCartId(), request.getCartFlowNo(),
                                    request.getCashierNo(), null, null,
                                    storeGoods.getCGoodsNo(), storeGoods.getCGoodsName(),
                                    NomalPrice, 0,
                                    null,  NomalPrice,NomalPrice,
                                    1, 0, false,
                                    storeGoods.getCBarcode(), "个"));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询商品的时间出错了:  {}",e.getMessage());
            throw  new ApiSysException(ErrorEnum.SSCO001001);
        }
    }



    public String selectCartInfo(Request request,ErrorEnum errorEnum) throws ApiSysException {
        try{
            List<BLBGoodsInfo> blbGoodsInfo=dlbGoodsInfoMapper.selectAll(request.getCartId(),request.getStoreId());
            long totalFee=0;
            long discountFee=0;
            long actualFee=0;
            if(blbGoodsInfo!=null && blbGoodsInfo.size()>0){
                for(BLBGoodsInfo t:blbGoodsInfo){
                    totalFee=totalFee+t.getAmount();
                    discountFee=discountFee+t.getDiscountAmount();
                }
                actualFee=totalFee-discountFee;
            }
            /**
             * 会员这里先预留  下面应该是一个方法
             */
            MemberInfo memberInfo=new MemberInfo();
            CartInfo cartInfo=new CartInfo(request.getStoreId(), request.getSn(), request.getCartId(), null,
                    totalFee, discountFee, actualFee, blbGoodsInfo, memberInfo);
            ResultMsg resultMsg=new ResultMsg(true, GlobalEumn.SUCCESS.getCode(),GlobalEumn.SUCCESS.getMesssage(),cartInfo);
            String s1=JSON.toJSONString(resultMsg, SerializerFeature.WriteNullListAsEmpty,
                    SerializerFeature.WriteNullNumberAsZero,
                    SerializerFeature.WriteNullBooleanAsFalse);
            return s1;
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询购物车:  {}",e.getMessage());
            throw  new ApiSysException(errorEnum);
        }
    }




}
