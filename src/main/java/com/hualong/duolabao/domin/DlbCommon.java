package com.hualong.duolabao.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * <pre>
 *    这里是所有类共有的参数
 * </pre>
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DlbCommon {

    /**
     *门店号
     */
    private String storeId;

    /**
     *设备SN
     */
    private String sn;
    /**
     *购物车ID
     * 商户维度，每台设备会对应一个购物车ID
     */
    private String cartId;


}
