package com.hualong.duolabao.service.impl;

import com.hualong.duolabao.dao.cluster.tDLBGoodsInfoMapper;
import com.hualong.duolabao.domin.BLBGoodsInfo;
import com.hualong.duolabao.domin.FrushGood;
import com.hualong.duolabao.domin.Request;
import com.hualong.duolabao.domin.cStoreGoods;
import com.hualong.duolabao.exception.ApiSysException;
import com.hualong.duolabao.exception.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2019-11-25.
 * <pre>
 *     这个工具类 注解相对丰富一些
 *     这里为什么需要单据在做一个类 （高手请忽略注释）（因为方法名有这个 synchronized 修饰  （如果你是小白或者技术不是精炼  需要思考  务必搞清楚 synchronized 的含义和为什么一个类里面的多个静态方法最好只有一个synchronized 修饰的方法 ））
 * </pre>
 */
public class ComGoodServiceImp {

    private static final Logger log= LoggerFactory.getLogger(ComGoodServiceImp.class);

    /**
     *<pre>
     *     这里是操作商品增加或更改的
     *     这里需要使用同步的方法 要不会出问题
     *</pre>
     * @param dlbGoodsInfoMapper  操作数据库的Dao
     * @param request              统一请求参数的封装  在不同的业务处理模块中  取出来和自己业务模块相关的数据即可  不相关的或者不是本模块的数据  取出来可能为null
     * @param frushGood            是否是称重商品的一个封装对象  记录解析的条码是否是称重商品 如果是  记录称重标志及相关解析出来的价格 金额 重量等信息
     * @param storeGoods           从数据库中查询到的商品
     * @param nomalPrice           称重 非称重  单价
     * @throws ApiSysException
     */
    public synchronized static void  InsertOrUpdateGoods(tDLBGoodsInfoMapper dlbGoodsInfoMapper, Request request, FrushGood frushGood, cStoreGoods storeGoods, Long nomalPrice) throws ApiSysException {

        try{
            //检测购物车是否存在改商品  这个地方可能存在并发 所以在方法上面面会
            BLBGoodsInfo blbGoodsInfo=dlbGoodsInfoMapper
                    .getOneBLBGoodsInfo(request.getCartId(),request.getStoreId(),storeGoods.getCBarcode());
            //如果没有改商品保存的购物车   如果存在就更新该购物车
            if(blbGoodsInfo!=null){
                int num=blbGoodsInfo.getQty()+1;
                Long amount= nomalPrice *num;
                CommonServiceImpl.updateBlbGoodsInfo(dlbGoodsInfoMapper,
                        new BLBGoodsInfo(request.getStoreId(), request.getSn(), request.getCartId(), request.getCartFlowNo(),
                                request.getCashierNo(), null, null,
                                storeGoods.getCGoodsNo(), storeGoods.getCGoodsName(),
                                amount,
                                0, null,
                                nomalPrice, nomalPrice,
                                blbGoodsInfo.getQty()+1, 0, false,
                                storeGoods.getCBarcode(), "个"));
            }else{

                CommonServiceImpl.insertBlbGoodsInfo(dlbGoodsInfoMapper,
                        new BLBGoodsInfo(request.getStoreId(), request.getSn(), request.getCartId(), request.getCartFlowNo(),
                                request.getCashierNo(), null, null,
                                storeGoods.getCGoodsNo(), storeGoods.getCGoodsName(),
                                nomalPrice, 0,
                                null, nomalPrice, nomalPrice,
                                1, 0, false,
                                storeGoods.getCBarcode(), "个",frushGood.getReceivingCode()));
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询商品的时间出错了:  {}",e.getMessage());
            throw  new ApiSysException(ErrorEnum.SSCO001001);
        }
    }
}
