package com.hualong.duolabao.dao.cluster;

import com.hualong.duolabao.domin.hldomin.tDlbSnConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2019-11-18.
 */
@Mapper
public interface tDlbSnConfigMapper {
    int deleteByPrimaryKey(@Param("lineId") Integer lineId, @Param("sn") String sn);

    int insert(tDlbSnConfig record);

    //用到
    int insertBySn(@Param("sn") String sn);

    //用到
    tDlbSnConfig selectByPrimaryKey(@Param("sn") String sn);


    List<tDlbSnConfig> selectAll();

    int updateByPrimaryKey(tDlbSnConfig record);
}
