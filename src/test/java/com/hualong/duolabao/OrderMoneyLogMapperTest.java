package com.hualong.duolabao;

import com.alibaba.fastjson.JSONObject;
import com.hualong.duolabao.config.DlbPayConnfig;
import com.hualong.duolabao.dao.cluster.MemberInfoMapper;
import com.hualong.duolabao.dao.cluster.OrderMoneyLogMapper;
import com.hualong.duolabao.domin.OrderMoneyLog;
import com.hualong.duolabao.domin.VipOffine;
import com.hualong.duolabao.domin.payentity.SweepOrder;
import com.hualong.duolabao.tool.SHA1;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import static com.hualong.duolabao.tool.String_Tool.getTimeUnix;

/**
 * Created by Administrator on 2019-07-31.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMoneyLogMapperTest {

    private static final Logger log= LoggerFactory.getLogger(OrderMoneyLogMapperTest.class);

    @Autowired
    private OrderMoneyLogMapper orderMoneyLogMapper;

    @Autowired
    private DlbPayConnfig dlbPayConnfig;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void insertOrderMoneyLogMapper(){
//        (String bizType, String orderId, String tradeNo,
//                String tenant, int amount, String currency,
//                String authcode, String orderIp)
        OrderMoneyLog orderMoneyLog=new OrderMoneyLog("002","002",
                "002","002",2,"002","002","002");
        Integer integer=orderMoneyLogMapper.insert(orderMoneyLog);
        log.info("我是影响行数 {}",integer);
    }

    @Test
    public void updateOrderMoneyLogMapper(){
//        (String bizType, String orderId, String tradeNo,
//                String tenant, int amount, String currency,
//                String authcode, String orderIp)
        OrderMoneyLog orderMoneyLog=new OrderMoneyLog("002",20,true);
        Integer integer=orderMoneyLogMapper.updateByPrimaryKey(orderMoneyLog);
        log.info("我是影响行数 {}",integer);
    }

    @Test
    public void testPay(){

        String timeUnix=getTimeUnix();
        String authCode="134607250875044726";
        String requestNum="7";
//        SweepOrder(String agentNum, String customerNum, String authCode, String machineNum, String shopNum,
//                String requestNum, String amount, String source, String tableNum)
        SweepOrder sweepOrder=new SweepOrder(dlbPayConnfig.getAgentnum(),dlbPayConnfig.getCustomernum(),authCode,
                null,dlbPayConnfig.getShopnum(),requestNum,"0.01","API",null);
        String body=JSONObject.toJSONString(sweepOrder);
        log.info("我是请求体携带的数据 {}",body);
        String url = "https://openapi.duolabao.com/v1/agent/passive/create";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("accessKey",dlbPayConnfig.getAccesskey());
        headers.set("timestamp",timeUnix);
        String sign="secretKey="+dlbPayConnfig.getSecretkey()+"&timestamp="+timeUnix +
                "&path=/v1/agent/passive/create&body="+body;
        log.info("带签名的字符串 {}",sign);
        sign= SHA1.encode(sign);
        log.info("签名后的字符串 {}",sign);
        headers.set("token",sign.toUpperCase());

        HttpEntity<String> entity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        String result = responseEntity.getBody();

        log.info("我是拿到的返回结果 {}",result);

    }

    @Test
    public void testqueryPay(){

        String timeUnix=getTimeUnix();
        String requestNum="2";
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
        log.info("带签名的字符串 {}",sign);
        sign= SHA1.encode(sign).toUpperCase();
        log.info("签名后的字符串 {}",sign);
        headers.set("token",sign);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("agentNum", dlbPayConnfig.getAgentnum());
        requestBody.add("customerNum", dlbPayConnfig.getCustomernum());
        requestBody.add("shopNum", dlbPayConnfig.getShopnum());
        requestBody.add("requestNum", requestNum);

        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(requestBody, headers);

        ResponseEntity<String> responseEntity = null;

        //responseEntity =  restTemplate.getForEntity(url, String.class,requestEntity);

        responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                String.class,
                requestEntity);

        String result = responseEntity.getBody();
        log.info("我是拿到的返回结果 {}",result);

    }


    @Test
    public void testReturnPay(){

        String timeUnix=getTimeUnix();
        String authCode=null;
        String requestNum="4";
//        SweepOrder(String agentNum, String customerNum, String authCode, String machineNum, String shopNum,
//                String requestNum, String amount, String source, String tableNum)
        SweepOrder sweepOrder=new SweepOrder(dlbPayConnfig.getAgentnum(),dlbPayConnfig.getCustomernum(),authCode,
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
        log.info("带签名的字符串 {}",sign);
        sign= SHA1.encode(sign).toUpperCase();
        log.info("签名后的字符串 {}",sign);
        headers.set("token",sign);

        HttpEntity<String> entity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        String result = responseEntity.getBody();

        log.info("我是拿到的返回结果 {}",result);

    }
}
