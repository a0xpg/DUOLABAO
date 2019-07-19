package com.hualong.duolabao.service;

import com.hualong.duolabao.domin.FrushGood;
import com.hualong.duolabao.domin.cStoreGoods;
import com.hualong.duolabao.exception.ApiSysException;

import java.util.List;

/**
 * Created by Administrator on 2019-07-15.
 */
public interface PosService {

    /**
     * <pre>
     *      根据条码获取商品
     * </pre>
     * @param cStoreNo  门店编号
     * @param barcodeList 商品条码
     * @return
     * @throws ApiSysException
     */
    List<cStoreGoods> GetcStoreGoodsS(String cStoreNo, List<String> barcodeList) throws ApiSysException;

    /**
     * <pre>
     *     将查询到的商品数据 保存到购物车
     *
     * </pre>
     * @param storeId       商户側门店号
     * @param cashierNo     收银员号
     * @param sn             设备SN
     * @param cartId        购物ID
     * @param cartFlowNo   购物车流水号
     * @param cStoreGoodsList  查询出来的商品对象一个
     * @param frushGood         生鲜商品重量 单价 金额 对象类
     * @throws ApiSysException
     */
    void SaveGoods(String storeId, String cashierNo,String sn,String cartId,String cartFlowNo,
              List<cStoreGoods> cStoreGoodsList,
              FrushGood frushGood) throws ApiSysException;
}
