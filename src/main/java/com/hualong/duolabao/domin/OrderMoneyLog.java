package com.hualong.duolabao.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2019-07-31.
 */
@SuppressWarnings("serial")
@NoArgsConstructor
@Data
@Accessors(chain=true)
public class OrderMoneyLog {

    private String bizType;

    private String orderId;

    private String tradeNo;

    private String tenant;

    private int amount;

    private String currency;

    private String authcode;

    private String orderIp;

    private Boolean paycomplited;

    public OrderMoneyLog(String tradeNo, Boolean paycomplited) {
        this.tradeNo = tradeNo;
        this.paycomplited = paycomplited;
    }

    public OrderMoneyLog(String bizType, String orderId, String tradeNo,
                         String tenant, int amount, String currency,
                         String authcode, String orderIp) {
        this.bizType = bizType;
        this.orderId = orderId;
        this.tradeNo = tradeNo;
        this.tenant = tenant;
        this.amount = amount;
        this.currency = currency;
        this.authcode = authcode;
        this.orderIp = orderIp;
    }
}
