package com.hualong.duolabao.domin;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2019-07-09.
 */
@Data
@ToString(callSuper = true)
@Accessors(chain=true)
@EqualsAndHashCode(callSuper = true)
public class BLBGoodsInfo  extends DlbCommon {


    private String cartFlowNo;
    private String cashierNo;
    private String lineId;
    /**
     * 这个字段用作删除用的
     * 这个字段不被序列化
     */
    @JSONField(serialize=false)
    private Long lineIdDelete;
    private String id;
    private String name;
    private long amount;
    private long discountAmount;
    private String discountName;
    private long basePrice;
    private long price;
    private Integer qty;
    private double weight;
    private Boolean isWeight;
    private String barcode;
    private String unit;

    /**
     *商户订单号
     */
    @JSONField(serialize=false)
    private String merchantOrderId;

    public BLBGoodsInfo() {
    }


    public BLBGoodsInfo(String storeId, String sn, String cartId, String merchantOrderId) {
        super(storeId, sn, cartId);
        this.merchantOrderId = merchantOrderId;
    }

    public BLBGoodsInfo(String cartFlowNo, String cashierNo, String lineId, Long lineIdDelete,
                        String id, String name, long amount, long discountAmount,
                        String discountName, long basePrice, long price,
                        Integer qty, double weight, Boolean isWeight,
                        String barcode, String unit) {
        this.cartFlowNo = cartFlowNo;
        this.cashierNo = cashierNo;
        this.lineId = lineId;
        this.lineIdDelete = lineIdDelete;
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.discountAmount = discountAmount;
        this.discountName = discountName;
        this.basePrice = basePrice;
        this.price = price;
        this.qty = qty;
        this.weight = weight;
        this.isWeight = isWeight;
        this.barcode = barcode;
        this.unit = unit;
    }

    public BLBGoodsInfo(String storeId, String sn, String cartId, String cartFlowNo,
                        String cashierNo, String lineId, Long lineIdDelete,
                        String id, String name, long amount, long discountAmount,
                        String discountName, long basePrice, long price,
                        Integer qty, double weight, Boolean isWeight,
                        String barcode, String unit) {
        super(storeId, sn, cartId);
        this.cartFlowNo = cartFlowNo;
        this.cashierNo = cashierNo;
        this.lineId = lineId;
        this.lineIdDelete = lineIdDelete;
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.discountAmount = discountAmount;
        this.discountName = discountName;
        this.basePrice = basePrice;
        this.price = price;
        this.qty = qty;
        this.weight = weight;
        this.isWeight = isWeight;
        this.barcode = barcode;
        this.unit = unit;
    }
}
