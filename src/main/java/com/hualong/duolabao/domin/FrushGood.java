package com.hualong.duolabao.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Created by Administrator on 2019-07-19.
 */
@SuppressWarnings("serial")
@NoArgsConstructor
@Data
@Accessors(chain=true)
public class FrushGood {

    /**
     * 是否生鲜
     */
    private boolean isWeight;
    /**
     *金额 单位分
     */
    private Long allMoney;
    /**
     *单价
     */
    private Long price;
    /**
     *重量
     */
    private double weight;

    public FrushGood(boolean isWeight) {
        this.isWeight = isWeight;
    }

    public FrushGood(boolean isWeight, Long allMoney, Long price, Long weight) {
        this.isWeight = isWeight;
        this.allMoney = allMoney;
        this.price = price;
        this.weight = weight;
    }


}
