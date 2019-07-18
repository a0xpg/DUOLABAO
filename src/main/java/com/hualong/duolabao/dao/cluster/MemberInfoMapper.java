package com.hualong.duolabao.dao.cluster;


import com.hualong.duolabao.domin.MemberInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberInfoMapper {
    int deleteByPrimaryKey(@Param("cartId") String cartId, @Param("storeId") String storeId);

    int insert(MemberInfo record);

    MemberInfo selectByPrimaryKey(@Param("cartId") String cartId, @Param("storeId") String storeId);

    List<MemberInfo> selectAll();

    int updateByPrimaryKey(MemberInfo record);
}