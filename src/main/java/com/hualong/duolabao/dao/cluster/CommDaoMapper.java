package com.hualong.duolabao.dao.cluster;

import com.hualong.duolabao.domin.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * Created by Administrator on 2019-07-19.
 */
@Mapper
public interface CommDaoMapper {

    /**
     * @param tableName
     * @param cID
     * @return
     */
    posConfig getposConfig(@Param("tableName")String tableName, @Param("cID")String cID);


    Integer p_saveSheetNo_Z_call(@Param("cStoreNo")String cStoreNo,@Param("cPosID")String cPosID,
                                 @Param("Zdriqi")String Zdriqi,@Param("SerNo")String SerNo,
                                 @Param("iSeed_Max")String iSeed_Max,@Param("callName")String callName);

    commSheetNo getCommSheetNo(@Param("cStoreNo")String cStoreNo, @Param("cPosID")String cPosID,
                               @Param("Zdriqi")String Zdriqi, @Param("callName")String callName);

    tDlbPosConfiguration gettDlbPosConfiguration(@Param("cartId") String cartId);

    /**
     * <pre>
     *     把数据插入到临时表的过程  提交购物车  计算整单优惠信息以前
     * </pre>
     * @param cartId
     * @param storeId
     * @param cVipNo
     * @param cPosID
     * @param tableName
     * @return
     */
    Integer p_Dataconversion_z(@Param("cartId")String cartId,@Param("storeId")String storeId,
                                 @Param("cVipNo")String cVipNo,@Param("cPosID")String cPosID,
                                 @Param("tableName")String tableName);

    /**
     * <pre>
     *     获取整单积分的方法
     * </pre>
     * @param cSheetNo
     * @param fVipScoreRatio
     * @param callName
     * @return
     */
    VipAddScore getVipScoreAdd(@Param("cSheetNo")String cSheetNo,
                               @Param("fVipScoreRatio")String fVipScoreRatio,
                               @Param("callName")String callName);

    List<preferentialGoods> get_preferentialGoods(@Param("cStoreNo")String cStoreNo, @Param("machineId")String machineId,
                                                  @Param("cSheetNo")String cSheetNo, @Param("vipNo")String vipNo,
                                                  @Param("fVipScoreRatio")String fVipScoreRatio, @Param("bDiscount")String bDiscount,
                                                  @Param("callName")String callName);

    /**
     * <pre>
     *     增减积分的
     * </pre>
     * @param appId
     * @param machineId
     * @param vipNo
     * @param addScore
     * @return
     */
    Integer update_Vip(@Param("appId")String appId,@Param("machineId")String machineId,
                       @Param("vipNo")String vipNo,@Param("addScore")Double addScore);
}
