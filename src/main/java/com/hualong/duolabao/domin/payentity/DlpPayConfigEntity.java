package com.hualong.duolabao.domin.payentity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Administrator on 2019-08-02.
 */
@SuppressWarnings("serial")
@NoArgsConstructor
@Data
@Accessors(chain=true)
public class DlpPayConfigEntity {

    private String tenant;

    private String accesskey;

    private String secretkey;

    private String agentnum;

    private String customernum;

    private String machinenum;

    private String shopnum;

    private String storeId;
}
