package com.hualong.duolabao.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hualong.duolabao.config.DlbConnfig;
import com.hualong.duolabao.config.DlbUrlConfig;
import com.hualong.duolabao.conntroller.DlbConntroller;
import com.hualong.duolabao.dao.cluster.CommDaoMapper;
import com.hualong.duolabao.dao.cluster.DlbDao;
import com.hualong.duolabao.dao.cluster.MemberInfoMapper;
import com.hualong.duolabao.dao.cluster.tDLBGoodsInfoMapper;
import com.hualong.duolabao.dao.pos.PosMain;
import com.hualong.duolabao.dlbtool.SignFacotry;
import com.hualong.duolabao.dlbtool.ThreeDESUtilDLB;
import com.hualong.duolabao.domin.*;
import com.hualong.duolabao.exception.ApiSysException;
import com.hualong.duolabao.exception.ErrorEnum;
import com.hualong.duolabao.result.GlobalEumn;
import com.hualong.duolabao.result.ResultMsg;
import com.hualong.duolabao.result.ResultMsgDlb;
import com.hualong.duolabao.service.PosService;
import com.hualong.duolabao.tool.String_Tool;
import com.sun.xml.internal.bind.v2.TODO;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2019-07-15.
 */
@Service
public class PosServiceImpl implements PosService,DlbUrlConfig {

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

    @Autowired
    private CommDaoMapper commDaoMapper;


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
                                                    0, frushGood.getWeightwight(), true,
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
            Long NomalPrice=new Double(storeGoods.getFNormalPrice()*100).longValue();
            log.info("查询到的商品信息  {}", JSON.toJSON(storeGoods).toString());
            //如果是生鲜 直接保存到购物车
            if(frushGood.isWeight()){
                //如果是13码  计算出来重量
                if(request.getBarcode().length()==13){
                    if(NomalPrice==0){
                        log.error("查询商品的时间出错了: 查询出来的单价是 0");
                        throw  new ApiSysException(ErrorEnum.SSCO001001);
                    }
                   double wight=(double)frushGood.getAllMoney()/(double)NomalPrice;
                    wight=new Double(String_Tool.String_IS_Two(wight)) ;
                    //重新赋值
                    frushGood.setWeightwight(wight);
                }
                CommonServiceImpl.insertBlbGoodsInfo(dlbGoodsInfoMapper,
                        new BLBGoodsInfo(request.getStoreId(), request.getSn(), request.getCartId(), request.getCartFlowNo(),
                                request.getCashierNo(), null, null,
                                storeGoods.getCGoodsNo(), storeGoods.getCGoodsName(),
                                frushGood.getAllMoney(), 0, null,
                                NomalPrice,NomalPrice,
                                0, frushGood.getWeightwight(), true,
                                storeGoods.getCBarcode(), "kg",frushGood.getReceivingCode()));
            }else {
                //检测购物车是否存在改商品
                BLBGoodsInfo blbGoodsInfo=this.dlbGoodsInfoMapper.getOneBLBGoodsInfo(request.getCartId(),request.getStoreId(),storeGoods.getCBarcode());



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
                                    storeGoods.getCBarcode(), "个",frushGood.getReceivingCode()));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询商品的时间出错了:  {}",e.getMessage());
            throw  new ApiSysException(ErrorEnum.SSCO001001);
        }
    }
    @Override
    public void updateGoodsS(Request request,
                             List<cStoreGoods> cStoreGoodsList,
                             FrushGood frushGood) throws ApiSysException {
        try{
            //得到商品信息
            cStoreGoods storeGoods=cStoreGoodsList.get(0);
            log.info("查询到的商品信息  {}", JSON.toJSON(storeGoods).toString());
            //检测购物车是否存在改商品
            BLBGoodsInfo blbGoodsInfo=this.dlbGoodsInfoMapper.getOneBLBGoodsInfo(request.getCartId(),request.getStoreId(),storeGoods.getCBarcode());
            Long NomalPrice=new Double(storeGoods.getFNormalPrice()*100).longValue();
            //如果没有改商品保存的购物车   如果存在就更新该购物车
            if(blbGoodsInfo!=null){
                int num=request.getQuantity();
                if(num==0){
                    //TODO 删除购物车的商品
                    CommonServiceImpl.deleteBlbGoodsInfo(dlbGoodsInfoMapper,null,null,null,request.getLineId());
                }else{
                    Long amount=NomalPrice*num;
                    CommonServiceImpl.updateBlbGoodsInfo(dlbGoodsInfoMapper,
                            new BLBGoodsInfo(request.getStoreId(), request.getSn(), request.getCartId(), request.getCartFlowNo(),
                                    request.getCashierNo(), null, null,
                                    storeGoods.getCGoodsNo(), storeGoods.getCGoodsName(),
                                    amount,
                                    0, null,
                                    NomalPrice,NomalPrice,
                                    num, 0, false,
                                    storeGoods.getCBarcode(), "个"));
                }
            }else{
                throw  new ApiSysException(ErrorEnum.SSCO010004);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("修改商品数量的时间出错了:  {}",e.getMessage());
            throw  new ApiSysException(ErrorEnum.SSCO001001);
        }
    }
    @Override
    public void deleteGood(Request request,
                               List<cStoreGoods> cStoreGoodsList,
                               FrushGood frushGood) throws ApiSysException {
        try{
            //TODO 删除购物车的商品
            int info=CommonServiceImpl.deleteBlbGoodsInfo(dlbGoodsInfoMapper,null,null,null,request.getLineId());
            if(info==0){
                throw  new ApiSysException(ErrorEnum.SSCO010004);
            }
        }catch (ApiSysException e){
            e.printStackTrace();
            log.error("删除商品的时间出错了:  {}",e.getMessage());
            throw  new ApiSysException(ErrorEnum.SSCO010004);
        }

    }
    @Override
    public void deleteCartInfo(Request request,
                             List<cStoreGoods> cStoreGoodsList,
                             FrushGood frushGood) throws ApiSysException {
        try{
            //删除会员
            memberInfoMapper.deleteByPrimaryKey(request.getCartId(),request.getStoreId());
            //TODO 删除购物车的商品
            int info=CommonServiceImpl.deleteBlbGoodsInfo(dlbGoodsInfoMapper,request.getCartId(),request.getStoreId(),null,null);
//            if(info==0){
//                throw  new ApiSysException(ErrorEnum.SSCO010008);
//            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("删除整个购物车的时间出错了:  {}",e.getMessage());
            throw  new ApiSysException(ErrorEnum.SSCO010008);
        }
    }
    @Override
    public void InsertOrUpdateMemberInfo(Request request) throws ApiSysException {
        try{
            VipOffine vipOffine=memberInfoMapper.Get_VipOffine(request.getUserId());
            if(vipOffine==null){
                log.info("该会员卡号不存在 {}",request.getUserId());
                throw  new ApiSysException(ErrorEnum.SSCO008001);
            }
            MemberInfo memberInfo2=new MemberInfo(request.getStoreId(), request.getSn(), request.getCartId(), request.getUserId(),
                    "ISV", null, vipOffine.getCVipNo(),
                    null, vipOffine.getFCurValue(), vipOffine.getBDiscount(), vipOffine.getFPFrate());
            MemberInfo memberInfo=memberInfoMapper.selectByPrimaryKey(request.getCartId(),request.getStoreId());
            if(memberInfo!=null){
                memberInfoMapper.deleteByPrimaryKey(request.getCartId(),request.getStoreId());
                memberInfoMapper.insert(memberInfo2);
            }else{
                memberInfoMapper.insert(memberInfo2);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("添加会员的时间出错了:  {}",e.getMessage());
            throw  new ApiSysException(ErrorEnum.SSCO008001);
        }
    }

    @Override
    public String CommitOrderSysn(Request request,ErrorEnum errorEnum) throws ApiSysException {
        ResultMsg resultMsg=null;
        try{
            //TODO 第一步 记录单据到数据库
            String orderLog=JSON.toJSONString(request);
            log.info("记录单据到数据库 CommitOrderSysn Log {}",orderLog);
            commDaoMapper.insert(new OrderSysnLog(request.getCartId(),request.getMerchantOrderId(),request.getPayTypeId(),request.getPayNo(),
                    request.getPayAmount(),request.getCartFlowNo(),request.getItems(),request.getStoreId(),
                    null,null,request.getSn(),request.getCardNum(),request.getCashierNo()));
            //TODO 第二步 查询分库库名
            tDlbPosConfiguration tDlbPosConfiguration = CommonServiceImpl.gettDlbPosConfiguration(request, commDaoMapper);
            //TODO 第三步 同步数据到分库
            ResultProc resultProc=commDaoMapper.GetResultProc(request.getMerchantOrderId(),request.getStoreId(),
                    tDlbPosConfiguration.getPosName()+".dbo.p_commitDataProcToJieSuanAndPOS_SaleSheetDetail_z");
            if(!resultProc.getResultCode().equals("1")){
                log.info("同步数据到分库失败 {} ",JSON.toJSONString(resultProc));
                errorEnum=ErrorEnum.SSCO001001;
                resultMsg= new ResultMsg(true, errorEnum.getCode(),errorEnum.getMesssage(),null);
                return JSON.toJSONString(resultMsg);
            }
            //TODO 第四步 如果存在会员卡 增加积分
            if(request.getCardNum()!=null && !request.getCardNum().equals("")){
                MemberInfo memberInfo=memberInfoMapper.selectByPrimaryKey(request.getCartId(),request.getStoreId());
                if(memberInfo!=null){
                    log.info("获取到的会员信息是  {}", JSONObject.toJSONString(memberInfo));
                    try{
                        commDaoMapper.update_Vip(request.getCartId(),request.getSn(),memberInfo.getCardNum(),memberInfo.getAddScore());
                        log.info("会员卡{} 增加了 {} 积分 成功", memberInfo.getCardNum(),memberInfo.getAddScore());
                    }catch (Exception e){
                        e.printStackTrace();
                        log.error("会员卡{} 增加了 {} 积分 失败", memberInfo.getCardNum(),memberInfo.getAddScore());
                    }
                }
            }
            resultMsg= new ResultMsg(true, errorEnum.getCode(),errorEnum.getMesssage(),null);
        }catch (ApiSysException e){
            e.printStackTrace();
            log.error("订单完成同步失败:  {}",e.getMessage());
            new ResultMsg(true, ErrorEnum.SSCO001001.getCode(),ErrorEnum.SSCO001001.getMesssage(),null);
        }
        String s1=JSON.toJSONString(resultMsg);
        return s1;
    }

    @Override
    public String ResponseDlbOrder(Request request,ErrorEnum errorEnum){
        try{
            String content=this.CommitOrderSysn(request,errorEnum);
            log.info("查询出来的订单同步信息 : {}",content);
            String cipherJson= ThreeDESUtilDLB.encrypt(content,dlbConnfig.getDeskey(),"UTF-8");
            log.info("查询出来的订单同步加密信息 : {}",cipherJson);
            String uuid=SignFacotry.getUUID();
            String sign=ThreeDESUtilDLB.md5(cipherJson+uuid,dlbConnfig.getMdkey());
            return ResultMsgDlb.ResultMsgDlb(request,cipherJson,sign,uuid);
        }catch (Exception e){
            e.printStackTrace();
            log.error("最终返回值封装这里出错了 {}",e.getMessage());
            //TODO 如果这里都出错了  基本就KO  不用往下写了
            return JSONObject.toJSONString(new ResultMsg(true,GlobalEumn.SSCO001001.getCode(),GlobalEumn.SSCO001001.getMesssage(),(String)null));
        }
    }

    @Override
    public String SelectCartInfo(Request request,ErrorEnum errorEnum) throws ApiSysException {
        try{
            List<BLBGoodsInfo> blbGoodsInfo=dlbGoodsInfoMapper.selectAll(request.getCartId(),request.getStoreId());
            long totalFee=0;
            long discountFee=0;
            long actualFee=0;
            String merchantOrderId=null;
            if(blbGoodsInfo!=null && blbGoodsInfo.size()>0){
                for(BLBGoodsInfo t:blbGoodsInfo){
                    totalFee=totalFee+t.getAmount();
                    discountFee=discountFee+t.getDiscountAmount();
                    merchantOrderId=t.getMerchantOrderId()==null ? merchantOrderId:t.getMerchantOrderId();
                }
                actualFee=totalFee-discountFee;
            }
            MemberInfo memberInfo=memberInfoMapper.selectByPrimaryKey(request.getCartId(),request.getStoreId());

            Double AddScore=(double) 0;
            if(memberInfo!=null){
                log.info("获取到的会员信息是  {}", JSONObject.toJSONString(memberInfo));
                memberInfo.setBDiscount(null);
                memberInfo.setFPFrate(null);
                AddScore=memberInfo.getAddScore();
                memberInfo.setAddScore(null);
            }
            CartInfo cartInfo=new CartInfo(request.getStoreId(), request.getSn(), request.getCartId(), merchantOrderId,
                    totalFee, discountFee, actualFee, blbGoodsInfo, memberInfo);
            //TODO 计算积分  可以在这里进行
            if(memberInfo!=null){
                cartInfo.setOrderScore(AddScore);
                cartInfo.setScoreInfo(new ScoreInfo(new Double(memberInfo.getScore())));
            }
            boolean returnStatus=errorEnum==ErrorEnum.SUCCESS ? true:false;
            ResultMsg resultMsg= new ResultMsg(returnStatus, errorEnum.getCode(),errorEnum.getMesssage(),cartInfo);
            String s1=JSON.toJSONString(resultMsg, SerializerFeature.WriteNullListAsEmpty,
                    SerializerFeature.WriteNullNumberAsZero,
                    SerializerFeature.WriteNullBooleanAsFalse);
            return s1;
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询购物车:  {}",e.getMessage());
            throw  new ApiSysException(ErrorEnum.SSCO001001);
        }
    }
    @Override
    public String ResponseDlb(Request request,ErrorEnum errorEnum){
        try{
            String content=this.SelectCartInfo(request,errorEnum);
            log.info("查询出来的购物车信息 : {}",content);
            String cipherJson= ThreeDESUtilDLB.encrypt(content,dlbConnfig.getDeskey(),"UTF-8");
            log.info("查询出来的购物车加密信息 : {}",cipherJson);
            String uuid=SignFacotry.getUUID();
            String sign=ThreeDESUtilDLB.md5(cipherJson+uuid,dlbConnfig.getMdkey());
            return ResultMsgDlb.ResultMsgDlb(request,cipherJson,sign,uuid);
        }catch (Exception e){
            e.printStackTrace();
            log.error("最终返回值封装这里出错了 {}",e.getMessage());
            //TODO 如果这里都出错了  基本就KO  不用往下写了
            return JSONObject.toJSONString(new ResultMsg(false,GlobalEumn.SSCO001001.getCode(),GlobalEumn.SSCO001001.getMesssage(),(String)null));
        }
    }
    @Override
    public String ResponseDlbCancelOrder(Request request,ErrorEnum errorEnum){
        try{
            ResultMsg resultMsg= new ResultMsg(true, errorEnum.getCode(),errorEnum.getMesssage(),null);
            String content=JSON.toJSONString(resultMsg);
            String cipherJson= ThreeDESUtilDLB.encrypt(content,dlbConnfig.getDeskey(),"UTF-8");
            String uuid=SignFacotry.getUUID();
            String sign=ThreeDESUtilDLB.md5(cipherJson+uuid,dlbConnfig.getMdkey());
            return ResultMsgDlb.ResultMsgDlb(request,cipherJson,sign,uuid);
        }catch (Exception e){
            e.printStackTrace();
            log.error("最终返回值封装这里出错了 {}",e.getMessage());
            //TODO 如果这里都出错了  基本就KO  不用往下写了
            return JSONObject.toJSONString(new ResultMsg(true,GlobalEumn.SSCO001001.getCode(),GlobalEumn.SSCO001001.getMesssage(),(String)null));
        }
    }

    @Override
    public  String CommUrlFun(String urlType,JSONObject jsonParam){
        String response="";
        log.info(urlType+" "+" 获取的参数: {}",jsonParam.toJSONString());
        Request request=null;
        try{
            SignFacotry.verifySignAndMerchantNo(dlbConnfig.getMdkey(),jsonParam,dlbConnfig.getMerchantno(),dlbConnfig);
            request=SignFacotry.decryptCipherJsonToRequest(dlbConnfig.getDeskey(),jsonParam, ErrorEnum.SSCO010015);
            //解密数据
            if(request==null){
                log.info("解密出来的 数据 {}","机密好像失败了,没有解密出来任何数据");
                return ResponseDlb(request,ErrorEnum.SSCO001006);
            }else {
                log.info("解密出来的 数据 {}",JSONObject.toJSONString(request,SerializerFeature.WriteMapNullValue));
            }
        }catch (ApiSysException e){
            e.printStackTrace();
            log.error("出错了 ",e.getExceptionEnum().toString());
            return ResponseDlb(request,ErrorEnum.SSCO001006);
        }

        switch (urlType){
            case selectMemberInfo://会员查询
                try{
                    this.InsertOrUpdateMemberInfo(request);
                    response=this.ResponseDlb(request,ErrorEnum.SUCCESS);
                }catch (ApiSysException e){
                    e.printStackTrace();
                    log.error("查询会员出错了 ",e.getExceptionEnum().toString());
                    log.error("查询会员出错了 ",e.getMessage());
                    return this.ResponseDlb(request,SignFacotry.getErrorEnumByCode(e.getExceptionEnum().getCode()));
                }
                break;
            case selectGoods:
                try{
                    FrushGood frushGood= getIsFrushGood(request, this.posMain, this.commDaoMapper);
                    List<String> list=new ArrayList<>();
                    list.add(frushGood.getBarcode());
                    List<cStoreGoods> storeGoodsList=this.GetcStoreGoodsS(request.getStoreId(),list);
                    SignFacotry.GoodListIsEmpty(storeGoodsList);
                    log.info("获取出来的商品是 {}",JSONObject.toJSON(storeGoodsList).toString());
                    this.SaveGoodsToCartInfo(
                            request,
                            storeGoodsList,frushGood);
                    response=this.ResponseDlb(request,ErrorEnum.SUCCESS);
                }catch (ApiSysException e){
                    e.printStackTrace();
                    log.error("获取商品出错了 ",e.getExceptionEnum().toString());
                    log.error("获取商品出错了 ",e.getMessage());
                    return this.ResponseDlb(request,SignFacotry.getErrorEnumByCode(e.getExceptionEnum().getCode()));
                }
                break;
            case updateGoods:
                try{
                    List<String> list=new ArrayList<>();
                    list.add(request.getBarcode());
                    List<cStoreGoods> storeGoodsList=this.GetcStoreGoodsS(request.getStoreId(),list);
                    SignFacotry.GoodListIsEmpty(storeGoodsList);
                    log.info("获取出来的商品是 {}",JSONObject.toJSON(storeGoodsList).toString());
                    this.updateGoodsS(
                            request,
                            storeGoodsList,null);
                    response=this.ResponseDlb(request,ErrorEnum.SUCCESS);
                }catch (ApiSysException e){
                    e.printStackTrace();
                    log.error("更改商品数量出错了 ",e.getExceptionEnum().toString());
                    log.error("更改商品数量出错了 ",e.getMessage());
                    return this.ResponseDlb(request,SignFacotry.getErrorEnumByCode(e.getExceptionEnum().getCode()));
                }
                break;
            case deleteGoods:
                try{
                    this.deleteGood(
                            request,
                            null,null);
                    response=this.ResponseDlb(request,ErrorEnum.SUCCESS);
                }catch (ApiSysException e){
                    e.printStackTrace();
                    log.error("删除商品出错了 ",e.getExceptionEnum().toString());
                    log.error("删除商品出错了 ",e.getMessage());
                    return this.ResponseDlb(request,SignFacotry.getErrorEnumByCode(e.getExceptionEnum().getCode()));
                }
                break;
            case clearCartInfo:
                try{
                    this.deleteCartInfo(
                            request,
                            null,null);
                    response=this.ResponseDlb(request,ErrorEnum.SUCCESS);
                }catch (ApiSysException e){
                    e.printStackTrace();
                    log.error("删除购物车出错了 ",e.getExceptionEnum().toString());
                    log.error("删除购物车出错了 ",e.getMessage());
                    return this.ResponseDlb(request,SignFacotry.getErrorEnumByCode(e.getExceptionEnum().getCode()));
                }
                break;
            case commitCartInfo:
                try{
                    this.commitCartInfo(
                            request,
                            ErrorEnum.SUCCESS);
                    response=this.ResponseDlb(request,ErrorEnum.SUCCESS);
                }catch (ApiSysException e){
                    e.printStackTrace();
                    log.error("提交购物车出错了 ",e.getExceptionEnum().toString());
                    log.error("提交购物车出错了 ",e.getMessage());
                    return this.ResponseDlb(request,SignFacotry.getErrorEnumByCode(e.getExceptionEnum().getCode()));
                }
                break;
            case cancleOrder:
                response=this.ResponseDlbCancelOrder(request,ErrorEnum.SUCCESS);
                break;
            case orderSysn:
                try{
                    response=this.ResponseDlbOrder(request,ErrorEnum.SUCCESS);
                    this.deleteCartInfo(
                            request,
                            null,null);
                }catch (Exception e){
                    e.printStackTrace();
                    log.error("同步订单完成出错了 {}",e.getMessage());
                }

                break;
            default:
                response="地址错误";
                log.info("请求地址出错了");
                break;
        }
        return response;
    }

    @Override
    public FrushGood getIsFrushGood(Request request,PosMain posMain,CommDaoMapper commDaoMapper) throws ApiSysException {
        FrushGood frushGood=new FrushGood(false);
        frushGood.setBarcode(request.getBarcode());
        frushGood.setReceivingCode(request.getBarcode());
        tDlbPosConfiguration tDlbPosConfiguration = CommonServiceImpl.gettDlbPosConfiguration(request, commDaoMapper);
        posConfig posConfig=this.commDaoMapper.getposConfig(tDlbPosConfiguration.getPosName()+".dbo.Pos_Config","条码秤");
        if(posConfig==null){
            log.info("读取配置信息失败,请先配置");
            log.error("读取配置信息失败,请先配置");
            throw  new ApiSysException(ErrorEnum.SSCO001002);
        }else {
            log.info("我是获取到的posConfig结果集 {}", JSONObject.toJSONString(posConfig));
        }

        if(!request.getBarcode().startsWith(posConfig.getCCharID()) || request.getBarcode().length()<13){
            //不是生鲜直接返回
            return frushGood;
        }else {
            if(request.getBarcode().startsWith(posConfig.getCCharID()) && request.getBarcode().length()==13){
                String barcode=request.getBarcode().substring(posConfig.getIGoodsNoStart()-1,posConfig.getIGoodsNoEnd());
                String money=request.getBarcode().substring(posConfig.getIMoneyStart()-1,posConfig.getIMoneyEnd());
                //单位  分
                double money2 = (Double.valueOf(money) * 100) / posConfig.getIRatio();
                //重新赋值
                frushGood.setWeight(true);
                frushGood.setAllMoney((new Double(money2)).longValue());
                frushGood.setBarcode(barcode);
            }else
            if(request.getBarcode().startsWith(posConfig.getCCharID()) && request.getBarcode().length()==18){
                String barcode=request.getBarcode().substring(posConfig.getIGoodsNoStart()-1,posConfig.getIGoodsNoEnd());
                String money=request.getBarcode().substring(posConfig.getIMoneyStart18()-1,posConfig.getIMoneyEnd18());
                String wight=request.getBarcode().substring(posConfig.getIWeightStart18()-1,posConfig.getIWeightEnd18());
                //重量 kg
                double wight2 = new Double(wight) / 1000;
                //单位  分
                double money2 = ((new Double(money) ) * 100) / posConfig.getIRatio();
                //重新赋值
                frushGood.setBarcode(barcode);
                frushGood.setWeight(true);
                frushGood.setWeightwight(wight2);
                frushGood.setAllMoney((new Double(money2)).longValue());

            }else {
                frushGood.setWeight(false);
                return frushGood;
            }
        }
        return frushGood;
    }
    @Override
    public void commitCartInfo(Request request,ErrorEnum errorEnum) throws ApiSysException{
        //TODO 这个是对整个购物车的操作
        tDlbPosConfiguration tDlbPosConfiguration = CommonServiceImpl.gettDlbPosConfiguration(request, commDaoMapper);
        String sheetNo=CommonServiceImpl.getSheetNo(request, tDlbPosConfiguration,commDaoMapper);
        CommonServiceImpl.updateCartInfoMerchantOrderId(request, sheetNo, dlbGoodsInfoMapper);
        //TODO 得到会员信息
        MemberInfo memberInfo=memberInfoMapper.selectByPrimaryKey(request.getCartId(),request.getStoreId());
        String vipNo="";
        String bDiscount="0";
        String fPFrate="100";
        if(memberInfo!=null){
            vipNo=memberInfo.getCardNum();
            bDiscount=memberInfo.getBDiscount();
            fPFrate=memberInfo.getFPFrate();
        }
        //TODO 计算获取购物车的信息
        CommonServiceImpl.SubmitShoppingCartCalculation(request, tDlbPosConfiguration, sheetNo, vipNo, bDiscount, fPFrate,commDaoMapper);
        //TODO  计算应该增加的积分并且更改积分值
        CommonServiceImpl.CalVipAddScore(request, tDlbPosConfiguration, sheetNo, vipNo, commDaoMapper, memberInfoMapper);
    }


}
