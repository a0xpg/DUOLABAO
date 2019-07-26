package com.hualong.duolabao.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Created by Administrator on 2019-07-22.
 */
@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class VipOffine {
    private String cVipNo;
    private String cVipName;
    private String cTel;
    private String cSex;
    private String cVipRanck;
    private String cStoreNo;
    private String dBirthday;
    private String dValidStart;
    private String dValidEnd;
    private String fCurValue;
    private String fCurValue_Pos;
    private String bVipPrice;
    private String bDiscount;
    private String fPFrate;
}
