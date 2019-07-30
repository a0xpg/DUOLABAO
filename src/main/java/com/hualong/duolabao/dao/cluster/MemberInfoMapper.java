package com.hualong.duolabao.dao.cluster;


import com.hualong.duolabao.domin.MemberInfo;
import com.hualong.duolabao.domin.VipOffine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberInfoMapper {
    int deleteByPrimaryKey(@Param("cartId") String cartId, @Param("storeId") String storeId);

    int updateAddScore(MemberInfo record);

    int insert(MemberInfo record);

    MemberInfo selectByPrimaryKey(@Param("cartId") String cartId, @Param("storeId") String storeId);

    List<MemberInfo> selectAll();

    int updateByPrimaryKey(MemberInfo record);

    /**
     * <pre>
     *     通过手机号 得到线下会员信息
     * </pre>
     * @param mobileNo
     * @return
     */
    VipOffine   Get_VipOffine(@Param("mobileNo") String mobileNo);
}