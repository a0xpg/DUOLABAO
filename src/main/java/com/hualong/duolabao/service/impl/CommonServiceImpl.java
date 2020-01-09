package com.hualong.duolabao.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hualong.duolabao.config.DlbConnfig;
import com.hualong.duolabao.config.MailConfig;
import com.hualong.duolabao.dao.cluster.*;
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
import com.hualong.duolabao.tool.SHA1;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.hualong.duolabao.tool.String_Tool.getTimeUnix;

/**
 * Created by Administrator on 2019-07-19.
 */
public class CommonServiceImpl {
    private static final Logger log= LoggerFactory.getLogger(CommonServiceImpl.class);

    /**
     *    <pre>
     *        保存数据到购物车 我来了
     *    </pre>
     * @param dlbGoodsInfoMapper
     * @param blbGoodsInfo1
     * @throws ApiSysException
     */
    public static void insertBlbGoodsInfo(tDLBGoodsInfoMapper dlbGoodsInfoMapper, BLBGoodsInfo blbGoodsInfo1) throws ApiSysException {
        try{
            Integer integer=dlbGoodsInfoMapper.insert(blbGoodsInfo1);
            if(integer==0){
                throw  new ApiSysException(ErrorEnum.SSCO001002);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("保存数据到购物车失败 ：{}",e.getMessage());
            throw  new ApiSysException(ErrorEnum.SSCO001002);
        }

    }

    /**
     *  <pre>
     *      更新 非称重商品 数量  单价
     *  </pre>
     * @param dlbGoodsInfoMapper
     * @param blbGoodsInfo1
     * @throws ApiSysException
     */
    public static void updateBlbGoodsInfo(tDLBGoodsInfoMapper dlbGoodsInfoMapper, BLBGoodsInfo blbGoodsInfo1) throws ApiSysException {
        try{
            Integer integer=dlbGoodsInfoMapper.updateByBLBGoodsInfo(blbGoodsInfo1);
            if(integer==0){
                throw  new ApiSysException(ErrorEnum.SSCO001002);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("更新数据到购物车失败 ：{}",e.getMessage());
            throw  new ApiSysException(ErrorEnum.SSCO001002);
        }

    }

    /**
     * <pre>
     *     1.dlbGoodsInfoMapper lineIdDelete 其他值为空  根据行号删除该单个商品
     *     2.dlbGoodsInfoMapper cartId       其他值为空  根据购物车id删除整个购物车（该cartId购物车下的商品）
     *     3.dlbGoodsInfoMapper              其他值为空   清除整个购物车表数据
     * </pre>
     * @param dlbGoodsInfoMapper   daoMapper
     * @param cartId                购物车id
     * @param storeId               门店id
     * @param barcode               商品编码
     * @param lineIdDelete         商品所在行号
     * @throws ApiSysException
     */
    public static Integer deleteBlbGoodsInfo(tDLBGoodsInfoMapper dlbGoodsInfoMapper,String cartId, String storeId,
                                          String barcode, String lineIdDelete) throws ApiSysException {
        try{
            Integer integer=dlbGoodsInfoMapper.deleteBLBGoodsInfo(cartId, storeId, barcode, lineIdDelete);
            return integer;
        }catch (Exception e){
            e.printStackTrace();
            log.error("删除数据到购物车失败 ：{}",e.getMessage());
            throw  new ApiSysException(ErrorEnum.SSCO001002);
        }

    }
    /**
     * <pre>
     *     得到表 tDlbPosConfiguration 的配置信息
     * </pre>
     * @param request
     * @param commDaoMapper
     * @return
     * @throws ApiSysException
     */
    public static tDlbPosConfiguration gettDlbPosConfiguration(Request request, CommDaoMapper commDaoMapper) throws ApiSysException {
        tDlbPosConfiguration tDlbPosConfiguration=commDaoMapper.gettDlbPosConfiguration(request.getCartId());
        if(tDlbPosConfiguration==null){
            log.info("读取配置信息失败,请先配置");
            log.error("读取配置信息失败,请先配置");
            throw  new ApiSysException(ErrorEnum.SSCO001002);
        }else {
            log.info("我是获取到的 tDlbPosConfiguration 结果集 {}", JSONObject.toJSONString(tDlbPosConfiguration));
        }
        return tDlbPosConfiguration;
    }

    /**
     * <pre>
     *     获取单号
     * </pre>
     * @param request
     * @param tDlbPosConfiguration
     * @param commDaoMapper
     * @return
     * @throws ApiSysException
     */
    public static String getSheetNo(Request request, tDlbPosConfiguration tDlbPosConfiguration,CommDaoMapper commDaoMapper) throws ApiSysException {
        try{
            commSheetNo commSheetNo=commDaoMapper.getCommSheetNo(request.getStoreId(),tDlbPosConfiguration.getPosid(),
                    new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                    tDlbPosConfiguration.getPosName()+".dbo.p_getPos_SerialNoSheetNo");
            Integer integer=commDaoMapper.p_saveSheetNo_Z_call(
                    request.getStoreId(),tDlbPosConfiguration.getPosid(),
                    new SimpleDateFormat("yyyy-MM-dd").format(new Date()),commSheetNo.getcSheetNo(),
                    "1",tDlbPosConfiguration.getPosName()+".dbo.p_saveSheetNo");
            log.info("提交订单获取单号是 {}",commSheetNo.getcSheetNo());
            return commSheetNo.getcSheetNo();
        }catch (Exception e){
            log.error("提交订单获取单号出错了 {}",e.getMessage());
            throw new ApiSysException(ErrorEnum.SSCO001001);
        }
    }

    /**
     *<pre>
     *     此处是修改购物车
     *</pre>
     * @param request
     * @param sheetNo
     * @param dlbGoodsInfoMapper
     * @throws ApiSysException
     */
    public static void updateCartInfoMerchantOrderId(Request request, String sheetNo, tDLBGoodsInfoMapper dlbGoodsInfoMapper) throws ApiSysException {
        BLBGoodsInfo blbGoodsInfo=new BLBGoodsInfo(request.getStoreId(), request.getSn(), request.getCartId(), sheetNo);
        int integer=dlbGoodsInfoMapper.updateBLBGoodsInfoOderId(blbGoodsInfo);
        if(integer==0){
            log.info("我是提交订单 但是此时车是空的  {}", integer);
            throw  new ApiSysException(ErrorEnum.SSCO010008);
        }
    }

    /**
     *<pre>
     *     这里很关键  这个过程不是用来查询的  查询只是打印出日志而已
     *     真正的目的在于更改购物车商品表 tDlbGoodsInfo
     *</pre>
     * @param request
     * @param tDlbPosConfiguration
     * @param sheetNo
     * @param vipNo
     * @param bDiscount
     * @param fPFrate
     * @param commDaoMapper
     * @throws ApiSysException
     */
    public static void SubmitShoppingCartCalculation(Request request, tDlbPosConfiguration tDlbPosConfiguration, String sheetNo, String vipNo, String bDiscount, String fPFrate,CommDaoMapper commDaoMapper) throws ApiSysException {
        try{
            //TODO 把数据插入到临时表计算整单优惠信息
            commDaoMapper.p_Dataconversion_z(request.getCartId(),request.getStoreId(),
                    vipNo,tDlbPosConfiguration.getPosid(),tDlbPosConfiguration.getPosName()+".dbo.pos_SaleSheetDetailTemp");
            //TODO 计算并且赋值
            List<preferentialGoods> list =commDaoMapper.get_preferentialGoods(
                    request.getStoreId(),tDlbPosConfiguration.getPosid(),sheetNo,vipNo,fPFrate,bDiscount,
                    (tDlbPosConfiguration.getPosName()+".dbo.p_ProcessPosSheetDLB").trim());
            if(list!=null){
                log.info("得到的优惠信息 p_ProcessPosSheetDLB ：{} ",
                        JSONObject.toJSONString(list, SerializerFeature.WriteMapNullValue,SerializerFeature.PrettyFormat));
            }else {
                log.error("提交购物车失败了  p_ProcessPosSheetDLB ：");
                throw  new ApiSysException(ErrorEnum.SSCO010008);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("提交购物车失败了 异常 p_ProcessPosSheetDLB ：");
            throw  new ApiSysException(ErrorEnum.SSCO001001);
        }
    }

    /**
     * <pre>
     *     如果有会员卡  这里是增加积分用的
     * </pre>
     * @param request
     * @param tDlbPosConfiguration
     * @param sheetNo
     * @param vipNo
     * @param commDaoMapper
     * @param memberInfoMapper
     * @throws ApiSysException
     */
    public static void CalVipAddScore(Request request, tDlbPosConfiguration tDlbPosConfiguration, String sheetNo, String vipNo, CommDaoMapper commDaoMapper, MemberInfoMapper memberInfoMapper) throws ApiSysException {
        try{
            if(!vipNo.equals("")){
                VipAddScore vipAddScore=commDaoMapper.getVipScoreAdd(sheetNo,
                        "100",tDlbPosConfiguration.getPosName()+".dbo.p_CountVipScore_Online");
                if(!vipAddScore.getVipAddScore().equals("0")){
                    MemberInfo memberInfo1=new MemberInfo(request.getStoreId(),request.getSn(),request.getCartId(),new Double(vipAddScore.getVipAddScore()));
                    memberInfoMapper.updateAddScore(memberInfo1);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("计算会员积分的时间出错了");
            throw new ApiSysException(ErrorEnum.SSCO001001);
        }

    }

    /**
     *<pre>
     *     下单的方法需要同步  避免并发 但是里面的动态对象仍需要克隆  否则一样会出问题
     *</pre>
     * @param request
     * @param dlpPayConfigEntityMapper
     * @param dlbConnfig
     * @param restTemplate
     * @param orderMoneyLogMapper
     * @return
     * @throws ApiSysException
     */
    public static synchronized String  payOrder(Request request, DlpPayConfigEntityMapper dlpPayConfigEntityMapper,
                                                DlbConnfig dlbConnfig, RestTemplate restTemplate,OrderMoneyLogMapper orderMoneyLogMapper) {

        String response="";
        try{
            DlpPayConfigEntity dlpPayConfigEntity=null;
            //查询商户dlb支付的配置
            dlpPayConfigEntity=dlpPayConfigEntityMapper.selectByPrimaryKey(request.getTenant(),request.getStoreId(),null);
            if(dlpPayConfigEntity==null){
                log.info("dlpPayConfigEntity {}","查询出来的dlb支付配置为空");
                log.error("dlpPayConfigEntity {}","查询出来的dlb支付配置为空");
                return ResponseDlb(request,ErrorEnum.SSCO003000,null,dlbConnfig);
            }
            String timeUnix=getTimeUnix();
            String authCode=request.getAuthcode();
            //这里就用DLB 的 京东交易号
            String requestNum=request.getTradeNo();
            SweepOrder sweepOrder=null;
            String amount=dlbConnfig.getIftestpay() ? "0.01":String.valueOf((double)request.getAmount()/100);
            sweepOrder=new SweepOrder(dlpPayConfigEntity.getAgentnum(),dlpPayConfigEntity.getCustomernum(),
                    authCode,
                    null,dlpPayConfigEntity.getShopnum(),
                    requestNum,amount,
                    "API",null);
            String body=JSONObject.toJSONString(sweepOrder);
            log.info("payOrder 我是请求体携带的数据 {}",body);
            String url = "https://openapi.duolabao.com/v1/agent/passive/create";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("accessKey",dlpPayConfigEntity.getAccesskey());
            headers.set("timestamp",timeUnix);
            String sign="secretKey="+dlpPayConfigEntity.getSecretkey()+"&timestamp="+timeUnix +
                    "&path=/v1/agent/passive/create&body="+body;
            sign= SHA1.encode(sign);
            headers.set("token",sign.toUpperCase());
            HttpEntity<String> entity = new HttpEntity<String>(body, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
            String result = responseEntity.getBody();
            log.info("payOrder 单号 {} 消费金额 {}元  拿到的结果 {}",requestNum,amount,result);
            JSONObject jsonObject= JSON.parseObject(result);
            if(jsonObject.containsKey("data") && jsonObject.containsKey("result")
                    && jsonObject.getString("result").equals("success")){
                //TODO 下单成功
                response=ResponseDlb(request,ErrorEnum.SUCCESS,"DOING",dlbConnfig);
                if(dlbConnfig.getDatabaserecording()){
                    //TODO 如果下单成功  记录到数据库
                    //TODO 放到这里  提升性能  (这里还可以优化  使用消息队列 交给队列去处理 无需线程同步)
                    OrderMoneyLog orderMoneyLog=null;
                    orderMoneyLog=new OrderMoneyLog(request.getBizType(),request.getOrderId(),
                            request.getTradeNo(),request.getTenant(),request.getAmount(),
                            request.getCurrency(),request.getAuthcode(),request.getOrderIp());
                    orderMoneyLog.setStoreId(request.getStoreId());
                    orderMoneyLog.setSn(request.getSn());
                    orderMoneyLogMapper.insert(orderMoneyLog);
                }

            }else {
                //TODO 下单失败
                response=ResponseDlb(request,ErrorEnum.SSCO003000,null,dlbConnfig);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("pay 下单失败");
            response=ResponseDlb(request,ErrorEnum.SSCO003000,null,dlbConnfig);
        }

        return response;
    }

    /**
     *
     * @param request
     * @param errorEnum
     * @param status
     * @param dlbConnfig
     * @return
     */
    public static String ResponseDlb(Request request, ErrorEnum errorEnum,String status,DlbConnfig dlbConnfig) {
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
            String uuid= SignFacotry.getUUID();
            String sign=ThreeDESUtilDLB.md5(cipherJson+uuid,dlbConnfig.getMdkey());
            return ResultMsgDlb.ResultMsgDlb(request,cipherJson,sign,uuid);
        }catch (Exception e){
            e.printStackTrace();
            log.error("订单错误的封装 {}",e.getMessage());
            //TODO 如果这里都出错了  基本就KO  不用往下写了
            return JSONObject.toJSONString(new ResultMsg(false, GlobalEumn.SSCO001001.getCode(),GlobalEumn.SSCO001001.getMesssage(),(String)null));
        }
    }

    /**
     * <pre>
     *     发送邮件的统一封装
     *     <div>该方法和业务有冲突</div>
     * </pre>
     * @param mailSender
     * @param mailConfig
     * @param content
     */
    public static void sendErrorMail(JavaMailSender mailSender, MailConfig mailConfig, String content) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(mailConfig.getMailaddr());
                message.setTo(mailConfig.getToaddr());
                message.setSubject(mailConfig.getTenant() +" "+mailConfig.getTenantname() +" 程序异常");
                message.setText("程序异常信息如下: "+"  \n"
                        + "商户编号: "+mailConfig.getTenant()+"  \n"
                        + "商户名称: "+mailConfig.getTenantname()+"  \n"
                        + "异常content:"+content );
                mailSender.send(message);
            }
        }).start();

    }








}
