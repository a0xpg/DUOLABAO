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
     *  <pre>
     *      检验商品是否存在的
     *  </pre>
     * @param list
     * @throws ApiSysException
     */
    public static void GoodListIsEmpty(List list)  throws ApiSysException {
        if(list==null || list.size()==0){
            throw  new ApiSysException(ErrorEnum.SSCO010004);
        }
    }

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
     *     根据商品 行号 删除该条记录
     * </pre>
     * @param dlbGoodsInfoMapper
     * @param cartId
     * @param storeId
     * @param barcode
     * @param lineIdDelete        只用这一个值就可以了
     * @throws ApiSysException
     */
    public static void deleteBlbGoodsInfo(tDLBGoodsInfoMapper dlbGoodsInfoMapper,String cartId, String storeId,
                                          String barcode, String lineIdDelete) throws ApiSysException {
        try{
            Integer integer=dlbGoodsInfoMapper.deleteBLBGoodsInfo(cartId, storeId, barcode, lineIdDelete);
            if(integer==0){
                throw  new ApiSysException(ErrorEnum.SSCO001002);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("更新数据到购物车失败 ：{}",e.getMessage());
            throw  new ApiSysException(ErrorEnum.SSCO001002);
        }

    }
}
