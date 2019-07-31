package com.hualong.duolabao.dao.cluster;

import com.hualong.duolabao.domin.OrderMoneyLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Administrator on 2019-07-31.
 */
@Mapper
public interface OrderMoneyLogMapper {

    int deleteByPrimaryKey(Long lineId);

    int insert(OrderMoneyLog record);

    OrderMoneyLog selectByPrimaryKey(Long lineId);

    List<OrderMoneyLog> selectAll();

    int updateByPrimaryKey(OrderMoneyLog record);

}
