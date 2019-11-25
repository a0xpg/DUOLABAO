package com.hualong.duolabao.domin.hldomin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Created by Administrator on 2019-11-18.
 * 华隆自己的无人自助机的配置表信息都在这里
 */
@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class tDlbSnConfig {

    //外部商户号
    private String tenant;
    //购物车id
    private String cartId;
    private String ip;
    private String storeId;
    private String sotreName;
    //设备号区分 唯一
    private String sn;
    private Integer lineId;

    private String tel;
    private String address;

}
