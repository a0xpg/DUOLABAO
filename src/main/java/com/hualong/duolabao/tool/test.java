package com.hualong.duolabao.tool;

import java.util.Date;

/**
 * Created by Administrator on 2019-07-03.
 */
public class test {
    public static void main(String[] args) {

        double a=123.056;
        Long l=new Double(a).longValue();
        System.out.println("l       :"+l);
        String str="123456789";
        System.out.println(str.substring(3,7));

        Date date = new Date();
        long time = date.getTime();

//mysq 时间戳只有10位 要做处理
        String dateline = time + "";
        dateline = dateline.substring(0, 10);
        System.out.println(dateline);

        String.valueOf((new Date()).getTime()).substring(0,10);
        System.out.println(String.valueOf((new Date()).getTime()).substring(0,10));

    }
}
