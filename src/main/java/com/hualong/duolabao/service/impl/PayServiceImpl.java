package com.hualong.duolabao.service.impl;

import com.hualong.duolabao.config.DlbPayConnfig;
import com.hualong.duolabao.dao.cluster.DlbDao;
import com.hualong.duolabao.dao.cluster.OrderMoneyLogMapper;
import com.hualong.duolabao.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Administrator on 2019-07-31.
 */
@Service
public class PayServiceImpl implements PayService {

    private static final Logger log= LoggerFactory.getLogger(PayServiceImpl.class);

    @Autowired
    private OrderMoneyLogMapper orderMoneyLogMapper;

    @Autowired
    private DlbPayConnfig dlbPayConnfig;

    @Autowired
    private RestTemplate restTemplate;
}
