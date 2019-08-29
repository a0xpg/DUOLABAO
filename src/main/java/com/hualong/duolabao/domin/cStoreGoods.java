package com.hualong.duolabao.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by Administrator on 2019-07-15.
 */
@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class cStoreGoods implements Serializable {

    private String cGoodsNo;
    private String cGoodsName;
    private String cBarcode;
    private String cStoreNo;
    private String cStoreName;
    private String cUnit;
    private String cSpec;
    private Double fNormalPrice;
    private Double fVipPrice;
    private Double fCKPrice;
    private Integer isWholePack;
    private Double youHuiAmount;
    private Double amount;
    private Integer num;
    private Integer packRate;
    private Double fVipScore_base;
    private Double fVipScore;
    private Boolean bWeight;





}
