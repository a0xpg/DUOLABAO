package com.hualong.duolabao.dao.cluster;


import com.hualong.duolabao.domin.GoodsInfoBeifen;
import org.apache.catalina.Store;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2018-11-13.
 */
@Mapper
public interface DlbDao {

    List<Store> get_cStroe(@Param("cStroreNo") String cStroreNo);

    Integer Exec(@Param("callJsonText")String callJsonText);

    List<GoodsInfoBeifen> GetGoodsCartInfo(@Param("cartId")String cartId);


}
