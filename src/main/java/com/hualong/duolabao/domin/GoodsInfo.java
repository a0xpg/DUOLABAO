package com.hualong.duolabao.domin;

import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2019-07-02.
 */

@Data
@ToString(callSuper = true)
@Accessors(chain=true)
@EqualsAndHashCode(callSuper = true)
public class GoodsInfo extends DlbCommon{

    private String id;
    private String name;
    private BigDecimal amount;
    private BigDecimal discountAmount;
    private BigDecimal basePrice;
    private BigDecimal price;
    private Integer qty;
    private BigDecimal weight;
    private boolean isWeight;
    private String lineId;
    private String barcode;
    private String unit;

    public  GoodsInfo(){

    }

    public GoodsInfo(String id, String name, BigDecimal amount, BigDecimal discountAmount,
                     BigDecimal basePrice, BigDecimal price, Integer qty, BigDecimal weight,
                     boolean isWeight, String lineId, String barcode, String unit) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.discountAmount = discountAmount;
        this.basePrice = basePrice;
        this.price = price;
        this.qty = qty;
        this.weight = weight;
        this.isWeight = isWeight;
        this.lineId = lineId;
        this.barcode = barcode;
        this.unit = unit;
    }
}
