package com.hualong.duolabao.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2019-07-31.
 */
@Configuration
@Data
public class DlbPayConnfig {

    @Value("${dlbpay.accesskey}")
    private String accesskey;

    @Value("${dlbpay.secretkey}")
    private String secretkey;

    @Value("${dlbpay.agentnum}")
    private String agentnum;

    @Value("${dlbpay.customernum}")
    private String customernum;

    @Value("${dlbpay.machinenum}")
    private String machinenum;

    @Value("${dlbpay.shopnum}")
    private String shopnum;


}
