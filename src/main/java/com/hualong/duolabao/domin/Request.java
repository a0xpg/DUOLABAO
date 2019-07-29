package com.hualong.duolabao.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Created by Administrator on 2019-07-22.
 * <pre>
 *     解密上传的参数
 *     统一接收类
 * </pre>
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


    /**
     * 一下试订单回传推过来的信息
     */
    private String merchantOrderId;
    private String payTypeId;
    private String payNo;
    private Long payAmount;
    private String items;
    private String cardNum;


    /**
     * <pre>
     *     这个构造方法有用  不要删除了
     *     SB
     * </pre>
     * @param storeId
     * @param cashierNo
     * @param sn
     * @param cartId
     * @param cartFlowNo
     */
    public Request(String storeId, String cashierNo, String sn, String cartId, String cartFlowNo) {
        this.storeId = storeId;
        this.cashierNo = cashierNo;
        this.sn = sn;
        this.cartId = cartId;
        this.cartFlowNo = cartFlowNo;
    }

}
