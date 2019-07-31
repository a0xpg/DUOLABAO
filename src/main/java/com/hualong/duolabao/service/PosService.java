package com.hualong.duolabao.service;

import com.alibaba.fastjson.JSONObject;
import com.hualong.duolabao.dao.cluster.CommDaoMapper;
import com.hualong.duolabao.dao.pos.PosMain;
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
    @Deprecated
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
     *<pre>
     *     更新购物车商品数量和单价和金额信息（总部单价更改 这里及时同步）
     *</pre>
     * @param request
     * @param cStoreGoodsList
     * @param frushGood
     * @throws ApiSysException
     */
    void updateGoodsS(Request request,
                      List<cStoreGoods> cStoreGoodsList,
                      FrushGood frushGood) throws ApiSysException;

    /**
     *<pre>
     *     删除商品（根据商品id删除该商品）
     *</pre>
     * @param request
     * @param cStoreGoodsList
     * @param frushGood
     * @throws ApiSysException
     */
    void deleteGood(Request request,
                    List<cStoreGoods> cStoreGoodsList,
                    FrushGood frushGood) throws ApiSysException;

    /**
     * <pre>
     *     会员查询
     * </pre>
     * @param request
     * @throws ApiSysException
     */
    void InsertOrUpdateMemberInfo(Request request) throws ApiSysException;

    /**
     *<pre>
     *     根据提交上来的
     *</pre>
     * @param request
     * @param cStoreGoodsList
     * @param frushGood
     * @throws ApiSysException
     */
    void deleteCartInfo(Request request,
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
     * <pre>
     *     提交购物车之前的操作
     * </pre>
     * @param request
     * @param errorEnum
     * @throws ApiSysException
     */
    void commitCartInfo(Request request,ErrorEnum errorEnum) throws ApiSysException;

    /**
     *<pre>
     *     请求统一处理
     *</pre>
     * @param urlType
     * @param jsonParam
     * @return
     * @throws ApiSysException
     */
    String CommUrlFun(String urlType,JSONObject jsonParam);

    /**
     *<pre>
     *     返回结果统一封装处理后再返回到客户端
     *</pre>
     * @param request
     * @param errorEnum
     * @return
     */
    String ResponseDlb(Request request,ErrorEnum errorEnum);

    /**
     * <pre>
     *     判断是否是生鲜 并对做些初始化工作 在保存商品到购物车事做判断并且再次对其做相应值更改
     * </pre>
     * @param request
     * @param posMain
     * @param commDaoMapper
     * @return
     * @throws ApiSysException
     */
    FrushGood getIsFrushGood(Request request, PosMain posMain, CommDaoMapper commDaoMapper) throws ApiSysException;


    String CommitOrderSysn(Request request,ErrorEnum errorEnum) throws ApiSysException;

    String ResponseDlbOrder(Request request,ErrorEnum errorEnum);

    String ResponseDlbCancelOrder(Request request,ErrorEnum errorEnum);


}
