package com.hualong.duolabao.domin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Created by Administrator on 2019-07-15.
 */
@Data
@ToString(callSuper = true)
@Accessors(chain=true)
@EqualsAndHashCode(callSuper = true)
public class tDlbPosConfiguration extends DlbCommon{
    private String  posName;
    private String  posid;
}
