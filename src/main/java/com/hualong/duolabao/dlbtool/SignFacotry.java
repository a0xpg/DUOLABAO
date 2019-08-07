package com.hualong.duolabao.dlbtool;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.hualong.duolabao.config.DlbConnfig;
import com.hualong.duolabao.domin.Request;
import com.hualong.duolabao.exception.ApiSysException;
import com.hualong.duolabao.exception.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static com.hualong.duolabao.dlbtool.ThreeDESUtilDLB.verify;

/**
 * Created by Administrator on 2019-07-15.
 * <pre>
 *     封装的方法类
 *     不要随意改动  OK?
 * </pre>
 */
public class SignFacotry {

    private static final Logger log= LoggerFactory.getLogger(SignFacotry.class);

    /**
     *
     * @param code
     * @return
     */
    public static ErrorEnum getErrorEnumByCode(String code){
        for(ErrorEnum sexEnum : ErrorEnum.values()){
            if(StringUtils.equals(code, sexEnum.getCode())){
                return sexEnum;
            }
        }
        return ErrorEnum.SSCO001002;
    }


    /**
     *  <pre>
     *      获取UUID
     *  </pre>
     * @throws ApiSysException
     */
    public static String getUUID()  throws ApiSysException {
        try{
            return UUID.randomUUID().toString().replaceAll("-", "");
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取UUID失败 {}",e.getMessage());
            throw  new ApiSysException(ErrorEnum.SSCO001001);
        }
    }
    /**
     *  <pre>
     *      检验商品是否为空
     *  </pre>
     * @param list
     * @throws ApiSysException
     */
    public static void GoodListIsEmpty(List list)  throws ApiSysException {
        if(list==null || list.size()==0){
            throw  new ApiSysException(ErrorEnum.SSCO010004);
        }
    }
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
    public static void verifySignAndMerchantNo(String md5Key,JSONObject jsonObject,String merchantNo) throws ApiSysException {
        try{
            /**
             * 这个校验没有打开  是因为哆啦宝支付的没有上传  所以这里屏蔽了这个校验
             */
            // || !jsonObject.getString("merchantNo").equals(merchantNo)
            if(!jsonObject.containsKey("merchantNo") || !jsonObject.containsKey("cipherJson") ||
                    !jsonObject.containsKey("sign") || !jsonObject.containsKey("systemId") ||
                    !jsonObject.containsKey("uuid") || !jsonObject.containsKey("tenant") ||
                    !jsonObject.containsKey("storeId")){
                log.error(" 上传的参数没有包含所需要的值 ");
                throw new ApiSysException(ErrorEnum.SSCO001004);
            }

            try{
                if(verify(jsonObject.getString("cipherJson") + jsonObject.getString("uuid"), md5Key, jsonObject.getString("sign"))){
                } else {
                    log.error(" 校验签名失败  ");
                    throw new ApiSysException(ErrorEnum.SSCO001004);
                }
            }catch (Exception E){
                E.printStackTrace();
                log.error(Thread.currentThread().getStackTrace()[0].getMethodName()+" 签名校验失败或者不是本商户号的访问 ："+E.getMessage());
                throw new ApiSysException(ErrorEnum.SSCO001004);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(Thread.currentThread().getStackTrace()[0].getMethodName()+" 签名校验失败或者不是本商户号的访问 ："+e.getMessage());
            throw new ApiSysException(ErrorEnum.SSCO001004);
        }
    }

    /**
     * <pre>
     *     重新的方法 是否开启校验merchantNo
     *     如果是获取商品基本信息 需要校验
     *     如果是请求的支付接口   不需要校验
     * </pre>
     * @param md5Key
     * @param jsonObject
     * @param merchantNo
     * @param dlbConnfig
     * @throws ApiSysException
     */
    public static void verifySignAndMerchantNo(String md5Key,JSONObject jsonObject,String merchantNo,DlbConnfig dlbConnfig) throws ApiSysException {
        try{
            if(!jsonObject.containsKey("merchantNo") || !jsonObject.containsKey("cipherJson") ||
                    !jsonObject.containsKey("sign") || !jsonObject.containsKey("systemId") ||
                    !jsonObject.containsKey("uuid") || !jsonObject.containsKey("tenant") ||
                    !jsonObject.containsKey("storeId")){
                log.error(" 上传的参数没有包含所需要的值 ");
                throw new ApiSysException(ErrorEnum.SSCO001004);
            }
            if(dlbConnfig.getCheckmerchantno()){
                if(!jsonObject.getString("merchantNo").equals(merchantNo)){
                    log.error(" merchantNo 校验失败 ");
                    throw new ApiSysException(ErrorEnum.SSCO001004);
                }
            }
            try{
                if(verify(jsonObject.getString("cipherJson") + jsonObject.getString("uuid"), md5Key, jsonObject.getString("sign"))){
                } else {
                    log.error(" 校验签名失败  ");
                    throw new ApiSysException(ErrorEnum.SSCO001004);
                }
            }catch (Exception E){
                E.printStackTrace();
                log.error(Thread.currentThread().getStackTrace()[0].getMethodName()+" 签名校验失败或者不是本商户号的访问 ："+E.getMessage());
                throw new ApiSysException(ErrorEnum.SSCO001004);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(Thread.currentThread().getStackTrace()[0].getMethodName()+" 签名校验失败或者不是本商户号的访问 ："+e.getMessage());
            throw new ApiSysException(ErrorEnum.SSCO001004);
        }
    }
    /**
     * <pre>
     *     解密
     * </pre>
     * @param desKey         秘钥
     * @param jsonObject     需要解密的json 从中拿到需要解密的字符串cipherJson
     * @return
     * @throws ApiSysException
     */
    public static JSONObject decryptCipherJson(String desKey,JSONObject jsonObject) throws ApiSysException {
        try{
            return JSONObject.parseObject(ThreeDESUtilDLB.decrypt(jsonObject.getString("cipherJson"),desKey,"UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
            log.error(Thread.currentThread().getStackTrace()[0].getMethodName()+" 签名校验失败或者不是本商户号的访问 ："+e.getMessage());
            throw new ApiSysException(ErrorEnum.SSCO001006);
        }
    }

    /**
     * <pre>
     *     解密并返回实体类（通用封装）
     * </pre>
     * @param desKey
     * @param jsonObject
     * @param errorEnum    不可以写null
     * @return
     * @throws ApiSysException
     */
    public static Request decryptCipherJsonToRequest(String desKey, JSONObject jsonObject,ErrorEnum errorEnum) throws ApiSysException {
        try{
            if(errorEnum==null){
                //throw new ApiSysException("方法的错误类型不可以写null,请填写错误类型");
                throw new ApiSysException("方法的错误类型不可以写null,请填写错误类型",new Throwable("方法的错误类型不可以写null,请填写错误类型"));
            }
            Request request=new Request();
            JSONObject jsonObject1= JSONObject.parseObject(
                    ThreeDESUtilDLB.decrypt(jsonObject.getString("cipherJson"),desKey,"UTF-8"));
            //
            request.setMerchantNo(jsonObject.getString("merchantNo"));
            request.setSystemId(jsonObject.getString("systemId"));
            request.setTenant(jsonObject.getString("tenant"));
            request.setStoreId(jsonObject.getString("storeId"));
            //下面是解密出来的
            request.setCartFlowNo(jsonObject1.getString("cartFlowNo"));
            request.setCartId(jsonObject1.getString("cartId"));
            request.setSn(jsonObject1.getString("sn"));
            request.setCashierNo(jsonObject1.getString("cashierNo"));
            request.setUserId(jsonObject1.getString("userId"));
            request.setBarcode(jsonObject1.getString("barcode"));
            request.setLineId(jsonObject1.getString("lineId"));
            request.setQuantity(jsonObject1.getInteger("quantity"));
            //订单回传的接收
            request.setMerchantOrderId(jsonObject1.getString("merchantOrderId"));
            request.setPayTypeId(jsonObject1.getString("payTypeId"));
            request.setPayNo(jsonObject1.getString("payNo"));
            if(jsonObject1.containsKey("payAmount")){
                request.setPayAmount(jsonObject1.getLong("payAmount"));
            }
            request.setItems(jsonObject1.getString("items"));
            request.setCardNum(jsonObject1.getString("cardNum"));
            /**
             * 接收必须返回的字段
             * 支付下单接口(收银机扫用户)
             */
            request.setBizType(jsonObject1.getString("bizType"));
            request.setOrderId(jsonObject1.getString("orderId"));
            request.setTradeNo(jsonObject1.getString("tradeNo"));

            if(jsonObject1.containsKey("amount")){
                request.setAmount(jsonObject1.getInteger("amount"));
            }
            request.setCurrency(jsonObject1.getString("currency"));
            request.setAuthcode(jsonObject1.getString("authcode"));
            request.setAppType(jsonObject1.getString("appType"));
            request.setOrderIp(jsonObject1.getString("orderIp"));

            request.setSerialNo(jsonObject1.getString("serialNo"));

            if(request!=null){
                return request;
            }else {
                throw new ApiSysException(errorEnum);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(Thread.currentThread().getStackTrace()[0].getMethodName()+" 签名校验失败或者不是本商户号的访问 ："+e.getMessage());
            throw new ApiSysException(errorEnum);
        }

    }


}
