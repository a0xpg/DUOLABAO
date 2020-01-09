package com.hualong.duolabao;

import com.alibaba.fastjson.JSONObject;
import com.hualong.duolabao.config.MailConfig;
import com.hualong.duolabao.domin.VipOffine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Administrator on 2019-12-26.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MialSendTest {

    private static final Logger log= LoggerFactory.getLogger(MialSendTest.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConfig mailConfig;

    public  void sendSimpleMail(JavaMailSender mailSender,MailConfig mailConfig,String content) {

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

    @Test
    public void testSend(){
        String[] a=new String[]{"2970337314@qq.com","1084716544@qq.com"};
        log.info(mailConfig.getMailaddr());
        log.info(mailConfig.getTenant());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailConfig.getMailaddr());
        message.setTo("1459139958@qq.com");
        message.setSubject(mailConfig.getTenant() +" "+mailConfig.getTenantname() +" 程序异常了");

        message.setText("张明阳 \n"
                +"张明阳 \n"+"张明阳 \n"+"张明阳 \n"+"张明阳 \n"
                +mailConfig.getTenant() +" "+mailConfig.getTenantname() +" 程序异常了");

//        message.setText("<html>" +
//                            "<body>" + " <div>张明阳</div><div>张明阳</div> " +
//                "<a href='http://www.blogjava.net/fancydeepin/'>http://www.blogjava.net/fancydeepin/</a>" +
//                            "</body>" +
//                            "</html>");

        mailSender.send(message);

    }
}
