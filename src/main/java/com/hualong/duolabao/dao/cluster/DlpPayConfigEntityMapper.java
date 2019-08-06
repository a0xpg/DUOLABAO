package com.hualong.duolabao.dao.cluster;

import com.hualong.duolabao.domin.payentity.DlpPayConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2019-08-02.
 */
@Mapper
public interface DlpPayConfigEntityMapper {

    int deleteByPrimaryKey(String tenant);

    int insert(DlpPayConfigEntity record);

    DlpPayConfigEntity selectByPrimaryKey(@Param("tenant") String tenant,
                                          @Param("storeId") String storeId,@Param("sn") String sn);

    List<DlpPayConfigEntity> selectAll();

    int updateByPrimaryKey(DlpPayConfigEntity record);
}
