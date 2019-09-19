package com.hualong.duolabao.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2019-07-11.
 */
@Configuration
@Data
public class DlbConnfig {

    @Value("${dlb.merchantno}")
    private String merchantno;

    @Value("${dlb.deskey}")
    private String deskey;

    @Value("${dlb.mdkey}")
    private String mdkey;

    @Value("${dlb.isdandian}")
    private Boolean isdandian;

    @Value("${dlb.timerenabled}")
    private Boolean timerenabled;

    @Value("${dlb.checkmerchantno}")
    private Boolean checkmerchantno;

    @Value("${dlb.checksystemid}")
    private Boolean checksystemid;

    @Value("${dlb.returnpay}")
    private Boolean returnpay;

    @Value("${dlb.calprice}")
    private Boolean calprice;

    @Value("${dlb.databaserecording}")
    private Boolean databaserecording;

    @Value("${dlb.tenant}")
    private String tenant;

    @Value("${dlb.checktenant}")
    private Boolean checktenant;

    @Value("${dlb.lossprevention}")
    private Boolean lossprevention;










}
