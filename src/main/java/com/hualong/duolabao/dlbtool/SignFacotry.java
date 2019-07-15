package com.hualong.duolabao.dlbtool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hualong.duolabao.exception.ApiSysException;
import com.hualong.duolabao.exception.ErrorEnum;
import com.hualong.duolabao.result.GlobalEumn;
import com.hualong.duolabao.service.impl.PosServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.hualong.duolabao.tool.ThreeDESUtilDLB.verify;

/**
 * Created by Administrator on 2019-07-15.
 */
public class SignFacotry {

    private static final Logger log= LoggerFactory.getLogger(SignFacotry.class);

    /**
     * <pre>
     *     这个方法意义： 校验拿到的json数据是否是哆啦宝上传来的数据,商户号是否和我们的是保持一致的
     *                     全部交验通过方能放行 继续前行
     * </pre>
     * @param md5Key         MD5秘钥
     * @param jsonObject    原始json数据
     * @param merchantNo    商户号
     * @return
     * @throws ApiSysException  错误码
     */
    GlobalEumn verifySignAndMerchantNo(String md5Key,JSONObject jsonObject,String merchantNo) throws ApiSysException {
        try{
            if(!jsonObject.containsKey("merchantNo") || !jsonObject.containsKey("cipherJson") ||
                    !jsonObject.containsKey("sign") || !jsonObject.containsKey("systemId") ||
                    !jsonObject.containsKey("uuid") || !jsonObject.containsKey("tenant") ||
                    !jsonObject.containsKey("storeId") || !jsonObject.getString("merchantNo").equals(merchantNo)){
                log.error(" 上传的参数没有包含所需要的值 ");
                throw new ApiSysException(ErrorEnum.SSCO001006);
            }
            try{
                if(verify(jsonObject.getString("cipherJson") + jsonObject.getString("uuid"), md5Key, jsonObject.getString("sign"))){
                    return GlobalEumn.SUCCESS;
                } else {
                    log.error(" 校验签名失败  ");
                    throw new ApiSysException(ErrorEnum.SSCO001006);
                }
            }catch (Exception E){
                E.printStackTrace();
                log.error(Thread.currentThread().getStackTrace()[0].getMethodName()+" 签名校验失败或者不是本商户号的访问 ："+E.getMessage());
                throw new ApiSysException(ErrorEnum.SSCO001006);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(Thread.currentThread().getStackTrace()[0].getMethodName()+" 签名校验失败或者不是本商户号的访问 ："+e.getMessage());
            throw new ApiSysException(ErrorEnum.SSCO001006);
        }
    }

}
