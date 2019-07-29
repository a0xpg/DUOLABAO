package com.hualong.duolabao.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hualong.duolabao.dao.cluster.CommDaoMapper;
import com.hualong.duolabao.dao.cluster.DlbDao;
import com.hualong.duolabao.dao.cluster.MemberInfoMapper;
import com.hualong.duolabao.dao.cluster.tDLBGoodsInfoMapper;
import com.hualong.duolabao.domin.*;
import com.hualong.duolabao.exception.ApiSysException;
import com.hualong.duolabao.exception.ErrorEnum;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2019-07-19.
 */
public class CommonServiceImpl {
    private static final Logger log= LoggerFactory.getLogger(CommonServiceImpl.class);



    /**
     *    <pre>
     *        保存数据到购物车
     *    </pre>
     * @param dlbGoodsInfoMapper
     * @param blbGoodsInfo1
     * @throws ApiSysException
     */
    public static void insertBlbGoodsInfo(tDLBGoodsInfoMapper dlbGoodsInfoMapper, BLBGoodsInfo blbGoodsInfo1) throws ApiSysException {
        try{
            Integer integer=dlbGoodsInfoMapper.insert(blbGoodsInfo1);
            if(integer==0){
                throw  new ApiSysException(ErrorEnum.SSCO001002);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("保存数据到购物车失败 ：{}",e.getMessage());
            throw  new ApiSysException(ErrorEnum.SSCO001002);
        }

    }

    /**
     *  <pre>
     *      更新 非称重商品 数量  单价
     *  </pre>
     * @param dlbGoodsInfoMapper
     * @param blbGoodsInfo1
     * @throws ApiSysException
     */
    public static void updateBlbGoodsInfo(tDLBGoodsInfoMapper dlbGoodsInfoMapper, BLBGoodsInfo blbGoodsInfo1) throws ApiSysException {
        try{
            Integer integer=dlbGoodsInfoMapper.updateByBLBGoodsInfo(blbGoodsInfo1);
            if(integer==0){
                throw  new ApiSysException(ErrorEnum.SSCO001002);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("更新数据到购物车失败 ：{}",e.getMessage());
            throw  new ApiSysException(ErrorEnum.SSCO001002);
        }

    }

    /**
     * <pre>
     *     1.dlbGoodsInfoMapper lineIdDelete 其他值为空  根据行号删除该单个商品
     *     2.dlbGoodsInfoMapper cartId       其他值为空  根据购物车id删除整个购物车（该cartId购物车下的商品）
     *     3.dlbGoodsInfoMapper              其他值为空   清除整个购物车表数据
     * </pre>
     * @param dlbGoodsInfoMapper   daoMapper
     * @param cartId                购物车id
     * @param storeId               门店id
     * @param barcode               商品编码
     * @param lineIdDelete         商品所在行号
     * @throws ApiSysException
     */
    public static Integer deleteBlbGoodsInfo(tDLBGoodsInfoMapper dlbGoodsInfoMapper,String cartId, String storeId,
                                          String barcode, String lineIdDelete) throws ApiSysException {
        try{
            Integer integer=dlbGoodsInfoMapper.deleteBLBGoodsInfo(cartId, storeId, barcode, lineIdDelete);

            return integer;
        }catch (Exception e){
            e.printStackTrace();
            log.error("删除数据到购物车失败 ：{}",e.getMessage());
            throw  new ApiSysException(ErrorEnum.SSCO001002);
        }

    }
    /**
     * <pre>
     *     得到表 tDlbPosConfiguration 的配置信息
     * </pre>
     * @param request
     * @param commDaoMapper
     * @return
     * @throws ApiSysException
     */
    public static tDlbPosConfiguration gettDlbPosConfiguration(Request request, CommDaoMapper commDaoMapper) throws ApiSysException {
        tDlbPosConfiguration tDlbPosConfiguration=commDaoMapper.gettDlbPosConfiguration(request.getCartId());
        if(tDlbPosConfiguration==null){
            log.info("读取配置信息失败,请先配置");
            log.error("读取配置信息失败,请先配置");
            throw  new ApiSysException(ErrorEnum.SSCO001002);
        }else {
            log.info("我是获取到的 tDlbPosConfiguration 结果集 {}", JSONObject.toJSONString(tDlbPosConfiguration));
        }
        return tDlbPosConfiguration;
    }

    /**
     * <pre>
     *     获取单号
     * </pre>
     * @param request
     * @param tDlbPosConfiguration
     * @param commDaoMapper
     * @return
     * @throws ApiSysException
     */
    public static String getSheetNo(Request request, tDlbPosConfiguration tDlbPosConfiguration,CommDaoMapper commDaoMapper) throws ApiSysException {
        try{
            commSheetNo commSheetNo=commDaoMapper.getCommSheetNo(request.getStoreId(),tDlbPosConfiguration.getPosid(),
                    new SimpleDateFormat("yyyy-MM-dd").format(new Date()),tDlbPosConfiguration.getPosName()+".dbo.p_getPos_SerialNoSheetNo");
            Integer integer=commDaoMapper.p_saveSheetNo_Z_call(
                    request.getStoreId(),tDlbPosConfiguration.getPosid(),
                    new SimpleDateFormat("yyyy-MM-dd").format(new Date()),commSheetNo.getcSheetNo(),
                    "1",tDlbPosConfiguration.getPosName()+".dbo.p_saveSheetNo");
            log.info("提交订单获取单号是 {}",commSheetNo.getcSheetNo());
            return commSheetNo.getcSheetNo();
        }catch (Exception e){
            log.error("提交订单获取单号出错了 {}",e.getMessage());
            throw new ApiSysException(ErrorEnum.SSCO001001);
        }
    }

    /**
     *<pre>
     *     此处是修改购物车
     *</pre>
     * @param request
     * @param sheetNo
     * @param dlbGoodsInfoMapper
     * @throws ApiSysException
     */
    public static void updateCartInfoMerchantOrderId(Request request, String sheetNo, tDLBGoodsInfoMapper dlbGoodsInfoMapper) throws ApiSysException {
        BLBGoodsInfo blbGoodsInfo=new BLBGoodsInfo(request.getStoreId(), request.getSn(), request.getCartId(), sheetNo);
        int integer=dlbGoodsInfoMapper.updateBLBGoodsInfoOderId(blbGoodsInfo);
        if(integer==0){
            log.info("我是提交订单 但是此时车是空的  {}", integer);
            throw  new ApiSysException(ErrorEnum.SSCO010008);
        }
    }

    /**
     *<pre>
     *     这里很关键  这个过程不是用来查询的  查询只是打印出日志而已
     *     真正的目的在于更改购物车商品表 tDlbGoodsInfo
     *</pre>
     * @param request
     * @param tDlbPosConfiguration
     * @param sheetNo
     * @param vipNo
     * @param bDiscount
     * @param fPFrate
     * @param commDaoMapper
     * @throws ApiSysException
     */
    public static void SubmitShoppingCartCalculation(Request request, tDlbPosConfiguration tDlbPosConfiguration, String sheetNo, String vipNo, String bDiscount, String fPFrate,CommDaoMapper commDaoMapper) throws ApiSysException {
        try{
            //TODO 把数据插入到临时表计算整单优惠信息
            commDaoMapper.p_Dataconversion_z(request.getCartId(),request.getStoreId(),
                    vipNo,tDlbPosConfiguration.getPosid(),tDlbPosConfiguration.getPosName());
            //TODO 计算并且赋值
            List<preferentialGoods> list =commDaoMapper.get_preferentialGoods(
                    request.getStoreId(),tDlbPosConfiguration.getPosid(),sheetNo,vipNo,fPFrate,bDiscount,
                    (tDlbPosConfiguration.getPosName()+".dbo.p_ProcessPosSheetDLB").trim());
            if(list!=null){
                log.info("得到的优惠信息 p_ProcessPosSheetDLB ：",
                        JSONObject.toJSONString(list, SerializerFeature.WriteMapNullValue,SerializerFeature.PrettyFormat));
            }else {
                log.error("提交购物车失败了  p_ProcessPosSheetDLB ：");
                throw  new ApiSysException(ErrorEnum.SSCO010008);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("提交购物车失败了 异常 p_ProcessPosSheetDLB ：");
            throw  new ApiSysException(ErrorEnum.SSCO001001);
        }
    }

    /**
     * <pre>
     *     如果有会员卡  这里是增加积分的
     * </pre>
     * @param request
     * @param tDlbPosConfiguration
     * @param sheetNo
     * @param vipNo
     * @param commDaoMapper
     * @param memberInfoMapper
     * @throws ApiSysException
     */
    public static void CalVipAddScore(Request request, tDlbPosConfiguration tDlbPosConfiguration, String sheetNo, String vipNo, CommDaoMapper commDaoMapper, MemberInfoMapper memberInfoMapper) throws ApiSysException {
        try{
            if(!vipNo.equals("")){
                VipAddScore vipAddScore=commDaoMapper.getVipScoreAdd(sheetNo,
                        "100",tDlbPosConfiguration.getPosName()+".dbo.p_CountVipScore_Online");
                if(!vipAddScore.getVipAddScore().equals("0")){
                    MemberInfo memberInfo1=new MemberInfo(request.getStoreId(),request.getSn(),request.getCartId(),new Double(vipAddScore.getVipAddScore()));
                    memberInfoMapper.updateAddScore(memberInfo1);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("计算会员积分的时间出错了");
            throw new ApiSysException(ErrorEnum.SSCO001001);
        }

    }


}
