package com.hualong.duolabao.dao;

import com.hualong.duolabao.result.ResultMsg;

/**
 * Created by Administrator on 2018-11-01.
 */
public class test {
    public static void main(String[] args){
        String jsonStr="{\n" +
                "\t\"sale\": [{\n" +
                "\t\t\"actualMoney\": \"25.30\",\n" +
                "\t\t\"saleTime\": \"2018-11-24 16:44:30\",\n" +
                "\t\t\"cStoreNo\": \"000\",\n" +
                "\t\t\"saleTypeName\": \"支付宝支付\",\n" +
                "\t\t\"saleType\": \"1\",\n" +
                "\t\t\"appId\": \"abc\",\n" +
                "\t\t\"discountMoney\": \"4.70\",\n" +
                "\t\t\"saleAllMoney\": \"30\",\n" +
                "\t\t\"cSheetNo\": \"07\",\n" +
                "\t\t\"cWHno\": \"000\"\n" +
                "\t}],\n" +
                "\t\"saleDatial\": [{\n" +
                "\t\t\"cStoreNo\": \"000\",\n" +
                "\t\t\"discountPrice\": \"19.8\",\n" +
                "\t\t\"cGoodsName\": \"测试商品1\",\n" +
                "\t\t\"discountMoney\": \"1.0\",\n" +
                "\t\t\"cSheetNo\": \"07\",\n" +
                "\t\t\"cWHno\": \"000\",\n" +
                "\t\t\"cGoodsNo\": \"111111111\",\n" +
                "\t\t\"fNormalPrice\": \"20.36\",\n" +
                "\t\t\"bAuditing\": \"0\",\n" +
                "\t\t\"iSeed\": \"0\",\n" +
                "\t\t\"fQuantity\": \"12\",\n" +
                "\t\t\"appId\": \"abc\",\n" +
                "\t\t\"cBarcode\": \"555555555\"\n" +
                "\t}, {\n" +
                "\t\t\"cStoreNo\": \"000\",\n" +
                "\t\t\"discountPrice\": \"19.8\",\n" +
                "\t\t\"cGoodsName\": \"测试商品1\",\n" +
                "\t\t\"discountMoney\": \"1.0\",\n" +
                "\t\t\"cSheetNo\": \"07\",\n" +
                "\t\t\"cWHno\": \"000\",\n" +
                "\t\t\"cGoodsNo\": \"111111111\",\n" +
                "\t\t\"fNormalPrice\": \"20.36\",\n" +
                "\t\t\"bAuditing\": \"0\",\n" +
                "\t\t\"iSeed\": \"1\",\n" +
                "\t\t\"fQuantity\": \"12\",\n" +
                "\t\t\"appId\": \"abc\",\n" +
                "\t\t\"cBarcode\": \"555555555\"\n" +
                "\t}, {\n" +
                "\t\t\"cStoreNo\": \"000\",\n" +
                "\t\t\"discountPrice\": \"19.8\",\n" +
                "\t\t\"cGoodsName\": \"测试商品1\",\n" +
                "\t\t\"discountMoney\": \"1.0\",\n" +
                "\t\t\"cSheetNo\": \"07\",\n" +
                "\t\t\"cWHno\": \"000\",\n" +
                "\t\t\"cGoodsNo\": \"111111111\",\n" +
                "\t\t\"fNormalPrice\": \"20.36\",\n" +
                "\t\t\"bAuditing\": \"0\",\n" +
                "\t\t\"iSeed\": \"2\",\n" +
                "\t\t\"fQuantity\": \"12\",\n" +
                "\t\t\"appId\": \"abc\",\n" +
                "\t\t\"cBarcode\": \"555555555\"\n" +
                "\t}]\n" +
                "}";
        jsonStr=jsonStr.replaceAll("\\t","").replaceAll("\\n","");
        System.out.println(jsonStr);

        ResultMsg resultMsg=new ResultMsg();
        System.out.println(resultMsg==null);
    }
}
