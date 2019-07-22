package com.hualong.duolabao.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Created by Administrator on 2019-07-22.
 */
@SuppressWarnings("serial")
@NoArgsConstructor
@Data
@Accessors(chain=true)
public class Request {
    private String storeId;
    private String cashierNo;
    private String sn;
    private String cartId;
    private String cartFlowNo;

    private String userId;
    private String barcode;
    private String lineId;
    private Integer quantity;

    private String merchantNo;

    private String systemId;

    private String tenant;


    public Request(String storeId, String cashierNo, String sn, String cartId, String cartFlowNo) {
        this.storeId = storeId;
        this.cashierNo = cashierNo;
        this.sn = sn;
        this.cartId = cartId;
        this.cartFlowNo = cartFlowNo;
    }

}
