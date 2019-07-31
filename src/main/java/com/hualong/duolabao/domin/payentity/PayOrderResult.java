package com.hualong.duolabao.domin.payentity;

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
public class PayOrderResult {

    private String retCode;
    private String retMsg;
    private String bizType;
    private String serialNo;
    private String amount;
    private String currency;
    private String status;


}
