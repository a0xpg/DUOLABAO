package com.hualong.duolabao.dao.cluster;


import com.hualong.duolabao.domin.BLBGoodsInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface tDLBGoodsInfoMapper {
    Integer deleteBLBGoodsInfo(@Param("cartId") String cartId, @Param("storeId") String storeId,
                           @Param("barcode") String barcode, @Param("lineIdDelete") String lineIdDelete);

    int insert(BLBGoodsInfo record);

    BLBGoodsInfo selectByPrimaryKey(@Param("cartId") String cartId, @Param("storeId") String storeId);

    Integer updateByBLBGoodsInfo(BLBGoodsInfo record);

    BLBGoodsInfo getOneBLBGoodsInfo(@Param("cartId") String cartId, @Param("storeId") String storeId,@Param("barcode") String barcode);
    /*
    * 查询整个购物车商品
    * */
    List<BLBGoodsInfo> selectAll(@Param("cartId") String cartId, @Param("storeId") String storeId);

    int updateByPrimaryKey(BLBGoodsInfo record);
}