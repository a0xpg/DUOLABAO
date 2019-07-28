package com.hualong.duolabao.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Administrator on 2019-07-27.
 */
@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class preferentialGoods {

    private String cUnit;
    private String cSpec;
    private String cSaleSheetno_time;
    private String cGoodsNo;
    private String cGoodsName;
    private String cBarcode;
    private String fQuantity;
    private String fPrice;
    private String fNormalSettle;
    private String fVipPrice;
    private String fLastSettle0;
    private String fAgio;
    private String fPrice_exe;
    private String fRate_Exe;
    private String fVipRate;
    private String fDiscount;
    private String fLastSettle;
    private String iSeed;
    private String bAuditing;
    private String bWeight;
    private String vipScoreAdd;
}
