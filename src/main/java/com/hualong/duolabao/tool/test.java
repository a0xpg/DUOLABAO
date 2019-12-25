package com.hualong.duolabao.tool;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2019-07-03.
 */
public class test {
    public static void main(String[] args) {

        String str= UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(str);
        System.out.println(str.length());

    }
}
