package com.hualong.duolabao.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2019-07-09.
 */
@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
@ToString
public class BLBGoodsInfo {



    private String cartld;

    private String cartFlowNo;

    private String storeId;

    private String cashierNo;

    private String sn;



    private String id;

    private String name;

    private BigDecimal amount;

    private BigDecimal discountAmount;

    private String discountName;

    private BigDecimal basePrice;

    private BigDecimal price;

    private Integer qty;

    private BigDecimal weight;

    private Boolean isWeight;

    private String barcode;

    private String unit;
}
