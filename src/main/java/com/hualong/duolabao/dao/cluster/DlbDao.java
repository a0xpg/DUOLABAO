package com.hualong.duolabao.dao.cluster;


import com.hualong.duolabao.domin.GoodsInfo;
import org.apache.catalina.Store;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018-11-13.
 */
@Mapper
public interface DlbDao {

    List<Store> get_cStroe(@Param("cStroreNo") String cStroreNo);

    Integer Exec(@Param("callJsonText")String callJsonText);

    List<GoodsInfo> GetGoodsCartInfo(@Param("cartId")String cartId);


}
