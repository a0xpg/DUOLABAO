package com.hualong.duolabao.service;

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
}
