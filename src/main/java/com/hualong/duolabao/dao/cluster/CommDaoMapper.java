package com.hualong.duolabao.dao.cluster;

import com.hualong.duolabao.domin.commSheetNo;
import com.hualong.duolabao.domin.posConfig;
import com.hualong.duolabao.domin.tDlbPosConfiguration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


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
}
