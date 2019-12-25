package com.hualong.duolabao.sdkWX;

import com.hualong.duolabao.tool.ReadConfig;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by Administrator on 2019-12-12.
 */
public class MyPayConfigImpl extends WXPayConfig {

    private byte[] certData;
    private String appid;
    private String mchid;
    private String key;
    private static MyPayConfigImpl INSTANCE;

    public MyPayConfigImpl(String appid, String mchid, String key,String certFilePath) throws Exception  {
        this.appid = appid;
        this.mchid = mchid;
        this.key = key;

        if(certFilePath!=null){
            File file = new File(certFilePath);
            InputStream certStream = new FileInputStream(file);
            this.certData = new byte[(int) file.length()];
            certStream.read(this.certData);
            certStream.close();
        }

    }

    public void setCertData(byte[] certData) {
        this.certData = certData;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }


    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private MyPayConfigImpl() throws Exception {

        String certPath = "G:\\IDEA\\微信支付\\java-sdk-v3\\src\\main\\resources\\apiclient_cert.p12";
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        //InputStream certStream = this.getClass().getResourceAsStream("/apiclient_cert.p12");;
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public static MyPayConfigImpl getInstance() throws Exception {
        if (INSTANCE == null) {
            synchronized (MyPayConfigImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MyPayConfigImpl();
                }
            }
        } else {

        }
        return INSTANCE;
    }

    public String getAppID() {

        return appid;
    }


    public String getMchID() {
        return mchid;
    }


    public String getKey() {
      return key;
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis;
        certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }


    public int getHttpConnectTimeoutMs() {
        return 2000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    IWXPayDomain getWXPayDomain() {
        return WXPayDomainSimpleImpl.instance();
    }

    public String getPrimaryDomain() {
        return "api.mch.weixin.qq.com";
    }

    public String getAlternateDomain() {
        return "api2.mch.weixin.qq.com";
    }

    @Override
    public int getReportWorkerNum() {
        return 1;
    }

    @Override
    public int getReportBatchSize() {
        return 2;
    }

}