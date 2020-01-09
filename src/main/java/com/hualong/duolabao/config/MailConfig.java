package com.hualong.duolabao.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2019-12-26.
 */
@Configuration
@Data
public class MailConfig {


    /**
     * 发送人
     */
    @Value("${spring.mail.from.mailaddr}")
    private String mailaddr;

    /**
     * 接收人
     */
    @Value("${spring.mail.from.mailaddr}")
    private String toaddr;

    /**
     * 外部商户号
     */
    @Value("${spring.mail.from.tenant}")
    private String tenant;

    /**
     * 外部商户名称
     */
    @Value("${spring.mail.from.tenantname}")
    private String tenantname;


}
