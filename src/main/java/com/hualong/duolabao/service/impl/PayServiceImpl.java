package com.hualong.duolabao.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hualong.duolabao.config.DlbConnfig;
import com.hualong.duolabao.config.DlbPayConnfig;
import com.hualong.duolabao.config.DlbUrlConfig;
import com.hualong.duolabao.dao.cluster.*;
import com.hualong.duolabao.dao.pos.PosMain;
import com.hualong.duolabao.dlbtool.SignFacotry;
import com.hualong.duolabao.dlbtool.ThreeDESUtilDLB;
import com.hualong.duolabao.domin.*;
import com.hualong.duolabao.domin.payentity.DlpPayConfigEntity;
import com.hualong.duolabao.domin.payentity.PayOrderResult;
import com.hualong.duolabao.domin.payentity.SweepOrder;
import com.hualong.duolabao.exception.ApiSysException;
import com.hualong.duolabao.exception.ErrorEnum;
import com.hualong.duolabao.result.GlobalEumn;
import com.hualong.duolabao.result.ResultMsg;
import com.hualong.duolabao.result.ResultMsgDlb;
import com.hualong.duolabao.service.PayService;
import com.hualong.duolabao.tool.SHA1;
import com.hualong.duolabao.tool.String_Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.hualong.duolabao.tool.String_Tool.getTimeUnix;

/**
 * Created by Administrator on 2019-07-31.
 */
@Service
public class PayServiceImpl implements PayService,DlbUrlConfig {

    private static final Logger log= LoggerFactory.getLogger(PayServiceImpl.class);

    @Autowired
    private OrderMoneyLogMapper orderMoneyLogMapper;

    @Autowired
    private DlbPayConnfig dlbPayConnfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DlbConnfig dlbConnfig;

    @Autowired
    private DlpPayConfigEntityMapper dlpPayConfigEntityMapper;


    @Override
    public  String CommUrlFun(String urlType,JSONObject jsonParam){
        String response="";
        log.info(urlType+" "+" 获取的参数: {}",jsonParam.toJSONString());
        Request request=null;
        DlpPayConfigEntity dlpPayConfigEntity=null;
        try{
            SignFacotry.verifySignAndMerchantNo(dlbConnfig.getMdkey(),jsonParam,dlbConnfig.getMerchantno(),dlbConnfig);
            request=SignFacotry.decryptCipherJsonToRequest(dlbConnfig.getDeskey(),jsonParam, ErrorEnum.SSCO010015);
            //解密数据
            if(request==null){
                log.info("pay 解密出来的 数据 {}","机密好像失败了,没有解密出来任何数据");
                return ResponseDlb(request,ErrorEnum.SSCO001006,null);
            }else {
                log.info("pay 解密出来的 数据 {}",JSONObject.toJSONString(request,SerializerFeature.WriteMapNullValue));
            }
            //查询商户dlb支付的配置
            dlpPayConfigEntity=this.dlpPayConfigEntityMapper.selectByPrimaryKey(request.getTenant(),request.getStoreId(),null);
            if(dlpPayConfigEntity==null){
                log.info("dlpPayConfigEntity {}","查询出来的dlb支付配置为空");
                log.error("dlpPayConfigEntity {}","查询出来的dlb支付配置为空");
                return ResponseDlb(request,ErrorEnum.SSCO001006,null);
            }
            //重新赋值
            dlbPayConnfig.setAccesskey(dlpPayConfigEntity.getAccesskey());
            dlbPayConnfig.setSecretkey(dlpPayConfigEntity.getSecretkey());
            dlbPayConnfig.setAgentnum(dlpPayConfigEntity.getAgentnum());
            dlbPayConnfig.setCustomernum(dlpPayConfigEntity.getCustomernum());
            dlbPayConnfig.setMachinenum(dlpPayConfigEntity.getMachinenum());
            dlbPayConnfig.setShopnum(dlpPayConfigEntity.getShopnum());

        }catch (ApiSysException e){
            e.printStackTrace();
            log.error("pay 出错了 ",e.getExceptionEnum().toString());
            return ResponseDlb(request,ErrorEnum.SSCO001006,null);
        }

        String timeUnix=getTimeUnix();
        String authCode=request.getAuthcode();
        //这里就用DLB 的 京东交易号
        String requestNum=request.getTradeNo();
        OrderMoneyLog orderMoneyLog=null;
        SweepOrder sweepOrder=null;
        switch (urlType){
            case payOrder:
                //TODO 支付订单
                try{
                    orderMoneyLog=new OrderMoneyLog(request.getBizType(),request.getOrderId(),
                            request.getTradeNo(),request.getTenant(),request.getAmount(),
                            request.getCurrency(),request.getAuthcode(),request.getOrderIp());
                    orderMoneyLogMapper.insert(orderMoneyLog);
                    String amount=String.valueOf((double)request.getAmount()/100);
                    sweepOrder=new SweepOrder(dlbPayConnfig.getAgentnum(),dlbPayConnfig.getCustomernum(),
                            authCode,
                            null,dlbPayConnfig.getShopnum(),
                            requestNum,amount,
                            "API",null);
                    String body=JSONObject.toJSONString(sweepOrder);
                    log.info("payOrder 我是请求体携带的数据 {}",body);
                    String url = "https://openapi.duolabao.com/v1/agent/passive/create";
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.set("accessKey",dlbPayConnfig.getAccesskey());
                    headers.set("timestamp",timeUnix);
                    String sign="secretKey="+dlbPayConnfig.getSecretkey()+"&timestamp="+timeUnix +
                            "&path=/v1/agent/passive/create&body="+body;
                    sign= SHA1.encode(sign);
                    headers.set("token",sign.toUpperCase());
                    HttpEntity<String> entity = new HttpEntity<String>(body, headers);
                    ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
                    String result = responseEntity.getBody();
                    log.info("payOrder 单号 {} 消费金额 {}元  拿到的结果 {}",requestNum,amount,result);
                    JSONObject jsonObject=JSON.parseObject(result);
                    if(jsonObject.containsKey("data") && jsonObject.containsKey("result")
                            && jsonObject.getString("result").equals("success")){
                        //TODO 下单成功
                        response=ResponseDlb(request,ErrorEnum.SUCCESS,"DOING");
                    }else {
                        //TODO 下单失败
                        response=ResponseDlb(request,ErrorEnum.SSCO003000,null);
                    }


                }catch (Exception e){
                    e.printStackTrace();
                    log.error("pay 下单失败");
                    response=ResponseDlb(request,ErrorEnum.SSCO003000,null);
                }

                break;
            case queryPayOrder:
                //TODO 查询订单
                try{

                    String urlAfter="/v1/agent/order/payresult/"
                            +dlbPayConnfig.getAgentnum()+"/"
                            +dlbPayConnfig.getCustomernum()+"/"
                            +dlbPayConnfig.getShopnum()+"/"+requestNum;
                    String url = "https://openapi.duolabao.com"+urlAfter;
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("accessKey",dlbPayConnfig.getAccesskey());
                    headers.set("timestamp",timeUnix);
                    String sign="secretKey="+dlbPayConnfig.getSecretkey()+"&timestamp="+timeUnix +
                            "&path="+urlAfter;
                    sign= SHA1.encode(sign).toUpperCase();
                    headers.set("token",sign);
                    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
                    requestBody.add("agentNum", dlbPayConnfig.getAgentnum());
                    requestBody.add("customerNum", dlbPayConnfig.getCustomernum());
                    requestBody.add("shopNum", dlbPayConnfig.getShopnum());
                    requestBody.add("requestNum", requestNum);
                    HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(requestBody, headers);
                    ResponseEntity<String> responseEntity =responseEntity = restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            new HttpEntity<String>(headers),
                            String.class,
                            requestEntity);
                    String result = responseEntity.getBody();
                    log.info("queryPayOrder 拿到的结果 {}",result);
                    JSONObject jsonObject=JSON.parseObject(result);
                    if(jsonObject.containsKey("data") && jsonObject.containsKey("result")
                            && jsonObject.getString("result").equals("success")){
                        //TODO 查询支付状态
                        jsonObject=JSON.parseObject(jsonObject.getString("data"));
                        if(jsonObject.getString("status").equals("INIT")){
                            response=ResponseDlb(request,ErrorEnum.SUCCESS,"DOING");
                        }else if(jsonObject.getString("status").equals("SUCCESS")){
                            Double orderAmount = new Double(jsonObject.getString("orderAmount"));
                            orderAmount=orderAmount*100;

                             orderMoneyLog=new OrderMoneyLog(request.getTradeNo(),
                                     orderAmount.intValue(),true,request.getTenant());
                            orderMoneyLogMapper.updateByPrimaryKey(orderMoneyLog);
                            response=ResponseDlb(request,ErrorEnum.SUCCESS,"SUCCESS");
                        }else if(jsonObject.getString("status").equals("CANCEL")){
                            response=ResponseDlb(request,ErrorEnum.SUCCESS,"CLOSE");
                        }else{
                            response=ResponseDlb(request,ErrorEnum.SUCCESS,"CLOSE");
                        }
                    }else {
                        //TODO
                        response=ResponseDlb(request,ErrorEnum.SSCO005009,null);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    log.error("pay 查询失败");
                    response=ResponseDlb(request,ErrorEnum.SSCO005009,null);
                }
                break;
            case canclePayOrder:
                //TODO 发起退款
                try{
                    sweepOrder=new SweepOrder(dlbPayConnfig.getAgentnum(),dlbPayConnfig.getCustomernum(),null,
                            null,dlbPayConnfig.getShopnum(),requestNum,null,null,null);
                    String body=JSONObject.toJSONString(sweepOrder);
                    log.info("我是请求体携带的数据 {}",body);
                    String url = "https://openapi.duolabao.com/v1/agent/order/refund";
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.set("accessKey",dlbPayConnfig.getAccesskey());
                    headers.set("timestamp",timeUnix);
                    String sign="secretKey="+dlbPayConnfig.getSecretkey()+"&timestamp="+timeUnix +
                            "&path=/v1/agent/order/refund&body="+body;
                    sign= SHA1.encode(sign).toUpperCase();
                    headers.set("token",sign);
                    HttpEntity<String> entity = new HttpEntity<String>(body, headers);
                    ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
                    String result = responseEntity.getBody();
                    log.info("canclePayOrder 拿到的结果 {}",result);
                    log.info("queryPayOrder 拿到的结果 {}",result);
                    JSONObject jsonObject=JSON.parseObject(result);
                    if(jsonObject.containsKey("data") && jsonObject.containsKey("result")
                            && jsonObject.getString("result").equals("success")) {
                        //TODO 退款成功记录一下
                        jsonObject = JSON.parseObject(jsonObject.getString("data"));
                        Double orderAmount = new Double(jsonObject.getString("refundAmount"));
                        orderAmount = orderAmount * 100;
                        orderMoneyLog = new OrderMoneyLog(request.getTradeNo(),
                                orderAmount.intValue());
                        orderMoneyLog.setTenant(request.getTenant());
                        orderMoneyLogMapper.updateByPrimaryKey(orderMoneyLog);
                    }
                    response=ResponseDlb(request,ErrorEnum.SUCCESS,"SUCCESS");
                }catch (Exception e){
                    e.printStackTrace();
                    log.error("pay 退款");
                    response=ResponseDlb(request,ErrorEnum.SUCCESS,"SUCCESS");
                }

                break;
            default:
                response="地址错误";
                log.info("请求地址出错了 pay");
                break;
        }
        return response;
    }

    @Override
    public String ResponseDlb(Request request, ErrorEnum errorEnum,String status) {
        try{
            status=status==null ? "FAIL":status;
            PayOrderResult payOrderResult=null;
            switch(request.getBizType()){
                case "AppPaySplitBill":
//                    PayOrderResult(String retCode, String retMsg, String bizType,
//                            String serialNo, String amount, String currency, String status)
                    payOrderResult=new PayOrderResult(errorEnum.getCode(),errorEnum.getMesssage(),
                                        request.getBizType(),request.getTradeNo(),
                             request.getAmount()+"",request.getCurrency(),status);
                    break;
                case "AppPayQuery":
                    payOrderResult=new PayOrderResult(errorEnum.getCode(),errorEnum.getMesssage(),
                            request.getBizType(),request.getTradeNo(),
                            request.getAmount()+"",request.getCurrency(),status);
                    break;
                case "AppPayCancle":
                    payOrderResult=new PayOrderResult(errorEnum.getCode(),errorEnum.getMesssage(),
                            request.getBizType(),null,
                            null,null,"SUCCESS");
                    break;
                default:
                    log.info("对方上传的参数有误");
                    break;

            }
            String content=JSON.toJSONString(payOrderResult);
            String cipherJson= ThreeDESUtilDLB.encrypt(content,dlbConnfig.getDeskey(),"UTF-8");
            String uuid=SignFacotry.getUUID();
            String sign=ThreeDESUtilDLB.md5(cipherJson+uuid,dlbConnfig.getMdkey());
            return ResultMsgDlb.ResultMsgDlb(request,cipherJson,sign,uuid);
        }catch (Exception e){
            e.printStackTrace();
            log.error("订单错误的封装 {}",e.getMessage());
            //TODO 如果这里都出错了  基本就KO  不用往下写了
            return JSONObject.toJSONString(new ResultMsg(false,GlobalEumn.SSCO001001.getCode(),GlobalEumn.SSCO001001.getMesssage(),(String)null));
        }
    }


}
