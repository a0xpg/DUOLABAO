package com.hualong.duolabao.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Created by Administrator on 2019-07-29.
 * <pre>
 *     接收订单回传操作数据控是否成功
 *
 *
 *     返回说明 resultCode
         1 成功
         2 异常 并携带异常错误码输出
         3 此单子在该门店不存在
      errorCode  如果调用过出错了  这里返回错误码

 * </pre>
 */
@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class ResultProc {

    private String resultCode;
    private String errorCode;
}
