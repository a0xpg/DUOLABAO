package com.hualong.duolabao.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Created by Administrator on 2019-07-29.
 * <pre>
 *     接收订单回传的记录表
 * </pre>
 */
@SuppressWarnings("serial")
@NoArgsConstructor
@Data
@Accessors(chain=true)
public class OrderSysnLog {

    private String cartId;
    private String merchantOrderId;
    private String payTypeId;
    private String payNo;
    private Long payAmount;
    private String cartFlowNo;
    private String items;
    private String storeId;
    private String userType;
    private String userNick;
    private String sn;
    private String cardNum;
    private String cashierNo;

    public OrderSysnLog(String cartId, String merchantOrderId, String payTypeId, String payNo,
                        Long payAmount, String cartFlowNo, String items, String storeId,
                        String userType, String userNick, String sn, String cardNum, String cashierNo) {
        this.cartId = cartId;
        this.merchantOrderId = merchantOrderId;
        this.payTypeId = payTypeId;
        this.payNo = payNo;
        this.payAmount = payAmount;
        this.cartFlowNo = cartFlowNo;
        this.items = items;
        this.storeId = storeId;
        this.userType = userType;
        this.userNick = userNick;
        this.sn = sn;
        this.cardNum = cardNum;
        this.cashierNo = cashierNo;
    }


}
