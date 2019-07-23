package com.hualong.duolabao.service;

import com.alibaba.fastjson.JSONObject;
import com.hualong.duolabao.domin.FrushGood;
import com.hualong.duolabao.domin.Request;
import com.hualong.duolabao.domin.cStoreGoods;
import com.hualong.duolabao.exception.ApiSysException;
import com.hualong.duolabao.exception.ErrorEnum;

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

    /**
     * <pre>
     *     上个方法的二次封装
     * </pre>
     * @param request
     * @param cStoreGoodsList
     * @param frushGood
     * @throws ApiSysException
     */
    void SaveGoodsToCartInfo(Request request,
                             List<cStoreGoods> cStoreGoodsList,
                             FrushGood frushGood) throws ApiSysException;

    /**
     * <pre>
     *     查询购物车商品
     * </pre>
     * @param request  请求参数封装类
     * @param errorEnum 可能出错的错误码  自定义返回
     * @return
     * @throws ApiSysException
     */
    String SelectCartInfo(Request request,ErrorEnum errorEnum) throws ApiSysException;

    /**
     *
     * @param urlType
     * @param jsonParam
     * @return
     * @throws ApiSysException
     */
    String CommUrlFun(String urlType,JSONObject jsonParam);

    /**
     *
     * @param request
     * @param errorEnum
     * @return
     */
    String ResponseDlb(Request request,ErrorEnum errorEnum);
}
