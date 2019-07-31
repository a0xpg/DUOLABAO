package com.hualong.duolabao.domin.payentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Created by Administrator on 2019-07-31.
 */
@SuppressWarnings("serial")
@NoArgsConstructor
@Data
@Accessors(chain=true)
public class SweepOrder {

    private String agentNum;
    private String customerNum;
    private String authCode;
    private String machineNum;
    private String shopNum;
    private String requestNum;
    private String amount;
    private String source;
    private String tableNum;

    public SweepOrder(String agentNum, String customerNum, String authCode, String machineNum, String shopNum,
                      String requestNum, String amount, String source, String tableNum) {
        this.agentNum = agentNum;
        this.customerNum = customerNum;
        this.authCode = authCode;
        this.machineNum = machineNum;
        this.shopNum = shopNum;
        this.requestNum = requestNum;
        this.amount = amount;
        this.source = source;
        this.tableNum = tableNum;
    }
}
