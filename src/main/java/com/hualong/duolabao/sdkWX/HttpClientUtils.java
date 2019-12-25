package com.hualong.duolabao.sdkWX;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static com.hualong.duolabao.sdkWX.WXPayUtil.MD5;
import static com.hualong.duolabao.sdkWX.WXPayUtil.getCurrentTimestamp;
import static com.hualong.duolabao.tool.String_Tool.getTimeUnix;

/**
 * Created by Administrator on 2019-12-12.
 */
public class HttpClientUtils {

    public static String requestOnce(final String mchID, String urlSuffix,  String data,
                                     int connectTimeoutMs, int readTimeoutMs) throws Exception {
        BasicHttpClientConnectionManager connManager;

        connManager = new BasicHttpClientConnectionManager(
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", SSLConnectionSocketFactory.getSocketFactory())
                        .build(),
                null,
                null,
                null
        );

        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connManager)
                .build();

//        String url = "https://" + domain + urlSuffix;
        HttpPost httpPost = new HttpPost(urlSuffix);

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs).setConnectTimeout(connectTimeoutMs).build();
        httpPost.setConfig(requestConfig);

        StringEntity postEntity = new StringEntity(data, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 " + mchID);  // TODO: 很重要，用来检测 sdk 的使用情况，要不要加上商户信息？
        httpPost.setEntity(postEntity);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, "UTF-8");
    }

    public static Map<String, String> fillRequestData(Map<String, String> reqData,String key) throws Exception {
        reqData.put("version", "1");
//        reqData.put("now",getTimeUnix());
        reqData.put("now",String.valueOf(getCurrentTimestamp()));
        reqData.put("nonce_str", WXPayUtil.generateUUID());
        reqData.put("sign_type", WXPayConstants.MD5);
        reqData.put("sign", generateSignature(reqData, key,"MD5"));
        return reqData;
    }

    public static String generateSignature(Map<String, String> data, String key, String signType) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(WXPayConstants.FIELD_SIGN)) {
                continue;
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        sb.append("key=").append(key);
        return MD5(sb.toString()).toUpperCase();

    }



    public static void main(String[] args){
        try {
//            String data="<xml><appid>wx7a16caec7c2d6bd8</appid>\n" +
//                    "<device_id>000101</device_id>\n" +
//                    "<mch_id>1335237301</mch_id>\n" +
//                    "<nonce_str>6B7E3E000586CB8C6AC9BBA9BE3E3371</nonce_str>\n" +
//                    "<now>1576137819</now>\n" +
//                    "<rawdata>PIeKknpE+a0ZBmhVi2+DtrKfV030+vNqpjl0v0u0uK8UmHwgvl2JixHRW4Ad0h8EQAURn3tUyOF/M76RwGsFbVboQMyZSV1HcK5uG2v8fN5+xpctALjGyAEGeHoVK3mqtCbsEIP6DtyuYEjkrZ5+jf3LvZprOo3YF8xnx8WzqD+gZKNiNOBQ1cNq3+HvhN95WMQfcelM4bswRmmqKF+rYMSjpm6nq2hp8OjGqIJjtXxCwvEaRSDCi89LMhPWXOwGRaaTdSS5V857hk00VC54MzchateQLsKjC5d2AX9Vj/DORmhc0gB+CDLDhJnNmQlwE8S1Zy3TVDvrUR91c9NUpZNJPSCftxX058W/cZgvliYG2YDqcflYDg71JO3VQ9IzB+mcPvvtRTO/LiEYV6h+DHuvuVe55T0O3Tbob3TqsMRtGPFjgWiFwbZCaXQ9kPCo3uIX3lmNjUnItYna7uyWcqXGVaYTEuHv7DUB2OIN72zxkdrJhO8OIQ4bUrReFHqSf+RjlSu0DnyHCG7PvxUmd6MLEFS/cFh7fHFaU9NStpCFWn6QpkSWeuJFQENZk+JiQiwTFY+2rWtHt2htU2Ohjz5HB3ZfLR6Awsef+9ANR7qzEyepfzgsdCEl6Zp0OSVcAVsToVHz2+z2C9FeQqaSA7RN4ADdEX1ZKqO8WJCBrsUORyxA08f0F8gjShagOMrg2sWCOK62saFvDKA38kdSkd62RV+1SAsCAfkoynJSyVwDIvNmDmoPICJ8iYO7xInr9EjFnFSk8iq+6Wm5ll+P95GiVPKgJUPaKXrVd7ByW4HcLYHcteM44iQM4bAuX8rII6R5th6i/ZvPXO33oHzHYmQ3</rawdata>\n" +
//                    "<sign>A3506592F543D4F8F35E09A1AE9B7566</sign>\n" +
//                    "<sign_type>MD5</sign_type>\n" +
//                    "<store_id>0001</store_id>\n" +
//                    "<store_name>华隆0001测试门店</store_name>\n" +
//                    "<version>1</version>\n" +
//                    "</xml>";
//            String res=requestOnce("123","https://payapp.weixin.qq.com/face/get_wxpayface_authinfo",
//                    data,2000,2000);
//            System.out.println(res);
//
//            Map<String, String>  map=WXPayUtil.xmlToMap(res);
            System.out.println( System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
