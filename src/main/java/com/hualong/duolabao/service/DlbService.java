package com.hualong.duolabao.service;

import org.apache.catalina.Store;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2019-07-02.
 */
public interface DlbService {

    List<Store> get_cStroeS(String cStroreNo);

    Integer ExecS(String callJsonText);

}
