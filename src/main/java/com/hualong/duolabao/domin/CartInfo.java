package com.hualong.duolabao.domin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringEscapeUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019-07-02.
 */
@Data
@ToString(callSuper = true)
@Accessors(chain=true)
@EqualsAndHashCode(callSuper = true)
public class CartInfo extends DlbCommon{
    /**
     *商户订单号
     */
    private String merchantOrderId;
    /**
     *合计总金额
     * 单位分
     */
    private BigDecimal totalFee;
    /**
     *优惠总金额
     * 单位分
     */
    private BigDecimal discountFee;
    /**
     *优惠后应付总金额
     * 单位分
     */
    private BigDecimal actualFee;
    /**
     *  商品信息
     */
    private List<GoodsInfo> items=new ArrayList<GoodsInfo>();
    /**
     *  会员信息
     */
    private MemberInfo memberInfo;

    private CartInfo(String storeId, String sn, String cartId, String merchantOrderId,
                     BigDecimal totalFee, BigDecimal discountFee, BigDecimal actualFee,
                     List<GoodsInfo> items, MemberInfo memberInfo) {
        super(storeId, sn, cartId);
        this.merchantOrderId = merchantOrderId;
        this.totalFee = totalFee;
        this.discountFee = discountFee;
        this.actualFee = actualFee;
        this.items = items;
        this.memberInfo = memberInfo;
    }

    public CartInfo(){

    }
    public static CartInfo CartInfoResult(String storeId, String sn, String cartId, String merchantOrderId,
                                          BigDecimal totalFee, BigDecimal discountFee, BigDecimal actualFee,
                                          List<GoodsInfo> items, MemberInfo memberInfo){

        return new CartInfo(storeId,  sn,  cartId,  merchantOrderId,
                 totalFee,  discountFee,  actualFee,  items, memberInfo);
    }

    public static String CartInfoToJsonString(String storeId, String sn, String cartId, String merchantOrderId,
                                          BigDecimal totalFee, BigDecimal discountFee, BigDecimal actualFee,
                                          List<GoodsInfo> items, MemberInfo memberInfo){

        return JSON.toJSONString(new CartInfo(storeId,  sn,  cartId,  merchantOrderId,
                totalFee,  discountFee,  actualFee,  items, memberInfo));


    }


    public static void main(String[] args) {
        String s = "\\u79fb\\5\\u4e92\\u8054\\u7f51\\u5e94\\u7528";
        String s2 = StringEscapeUtils.unescapeJava(s);

        StringEscapeUtils.unescapeJavaScript(s);
        System.out.println(StringEscapeUtils.unescapeJava(s));
        System.out.println(s);
        System.out.println(s2);

        List<GoodsInfo> items=new ArrayList<GoodsInfo>();

        GoodsInfo goodsInfo=new GoodsInfo();
        goodsInfo.setBarcode("2036");
        goodsInfo.setBasePrice(BigDecimal.valueOf(5.96));

        items.add(goodsInfo);

        goodsInfo=new GoodsInfo();
        goodsInfo.setBarcode("2036");
        goodsInfo.setBasePrice(BigDecimal.valueOf(5.96));

        items.add(goodsInfo);

        String s1=JSON.toJSONString(items);

        System.out.println(s1);


        MemberInfo memberInfo=new MemberInfo();
        memberInfo.setCartId("456");
        memberInfo.setCardNum("568");
        memberInfo.setPhoneNum("986");

        // WriteNullListAsEmpty 将Collection类型字段的字段空值输出为[]
        // WriteNullStringAsEmpty 将字符串类型字段的空值输出为空字符串 ""
        // WriteNullNumberAsZero 将数值类型字段的空值输出为0
        // WriteNullBooleanAsFalse 将Boolean类型字段的空值输出为false

        CartInfo cartInfo=new CartInfo("123", "123", "123", "568",
                BigDecimal.valueOf(5.96), BigDecimal.valueOf(5.96), BigDecimal.valueOf(5.96), null, null);

        s1=JSON.toJSONString(cartInfo,SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.PrettyFormat);

        System.out.println(s1);

        s1=JSON.toJSONString(cartInfo,SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteMapNullValue);

        System.out.println(s1);

//        System.out.println(StringEscapeUtils.unescapeJavaScript(s1));
//
//        cartInfo=new CartInfo(s1, "123", "123", "568",
//                BigDecimal.valueOf(5.96), BigDecimal.valueOf(5.96), BigDecimal.valueOf(5.96), null, memberInfo);
//
//        s1=JSON.toJSONString(cartInfo, SerializerFeature.WriteNullListAsEmpty);
//        System.out.println(s1);
//
//        System.out.println(StringEscapeUtils.unescapeJavaScript(s1));



    }

}
