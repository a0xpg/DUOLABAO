package com.hualong.duolabao.service.impl;

import com.hualong.duolabao.dao.cluster.DlbDao;
import com.hualong.duolabao.service.DlbService;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2019-07-02.
 */
@Service
public class DlbServiceImpl implements DlbService {

    @Autowired
    private DlbDao dlbDao;
    @Override
    public List<Store> get_cStroeS(String cStroreNo) {
        return dlbDao.get_cStroe(cStroreNo);
    }

    @Override
    public Integer ExecS(String callJsonText) {
        return dlbDao.Exec(callJsonText);
    }
}
