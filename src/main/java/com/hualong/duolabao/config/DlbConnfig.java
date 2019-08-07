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




}
