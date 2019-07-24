package com.hualong.duolabao.service.impl;

import com.hualong.duolabao.dao.cluster.DlbDao;
import com.hualong.duolabao.dao.cluster.tDLBGoodsInfoMapper;
import com.hualong.duolabao.domin.BLBGoodsInfo;
import com.hualong.duolabao.exception.ApiSysException;
import com.hualong.duolabao.exception.ErrorEnum;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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


}
