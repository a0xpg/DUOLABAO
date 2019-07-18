package com.hualong.duolabao.dao.pos;

import com.hualong.duolabao.domin.cStoreGoods;
import com.hualong.duolabao.domin.tDlbPosConfiguration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2019-04-25.
 */
@Mapper
public interface PosMain {

    //获取商品基本信息
    List<cStoreGoods> GetcStoreGoods(@Param("cStoreNo") String cStoreNo, @Param("barcodeList")List<String> barcodeList);

    //获取商品基本信息 单店
    List<cStoreGoods>  GetcStoreGoodsDanDian(@Param("cStoreNo") String cStoreNo,@Param("barcodeList")List<String> barcodeList);

    tDlbPosConfiguration GettDlbPosConfiguration(@Param("cartId") String cartId);
}
