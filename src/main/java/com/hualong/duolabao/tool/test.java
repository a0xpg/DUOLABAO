package com.hualong.duolabao.tool;

/**
 * Created by Administrator on 2019-07-03.
 */
public class test {
    public static void main(String[] args) {
        Integer[] data={1,2,3,4,5,6,7,8};
        System.out.println(data[2]);
        data[2]+=(data[2]+6);

        data[2]=data[2]+data[2]+6;
        System.out.println(data[2]);

    }
}
