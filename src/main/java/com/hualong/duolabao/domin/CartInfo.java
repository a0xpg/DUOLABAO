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
    private long totalFee;
    /**
     *优惠总金额
     * 单位分
     */
    private long discountFee;
    /**
     *优惠后应付总金额
     * 单位分
     */
    private long actualFee;
    /**
     *  商品信息
     */
    private List<BLBGoodsInfo> items=new ArrayList<BLBGoodsInfo>();
    /**
     *  会员信息
     */
    private MemberInfo memberInfo=new MemberInfo();

    /**
     * 订单赠送的积分
     */
    private Double orderScore;

    /**
     * 主要是用于打印订单上面的当前积分的
     */
    private ScoreInfo scoreInfo;

    public CartInfo(String storeId, String sn, String cartId, String merchantOrderId,
                    long totalFee, long discountFee, long actualFee,
                     List<BLBGoodsInfo> items, MemberInfo memberInfo) {
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
                                          long totalFee, long discountFee, long actualFee,
                                          List<BLBGoodsInfo> items, MemberInfo memberInfo){

        return new CartInfo(storeId,  sn,  cartId,  merchantOrderId,
                 totalFee,  discountFee,  actualFee,  items, memberInfo);
    }




    public static void main(String[] args) {
        String s = "\\u79fb\\5\\u4e92\\u8054\\u7f51\\u5e94\\u7528";
        String s2 = StringEscapeUtils.unescapeJava(s);

        StringEscapeUtils.unescapeJavaScript(s);
        System.out.println(StringEscapeUtils.unescapeJava(s));
        System.out.println(s);
        System.out.println(s2);

        List<BLBGoodsInfo> items=new ArrayList<BLBGoodsInfo>();

        BLBGoodsInfo goodsInfo=new BLBGoodsInfo();
        goodsInfo.setBarcode("2036");
        goodsInfo.setBasePrice(456);

        items.add(goodsInfo);

        goodsInfo=new BLBGoodsInfo();
        goodsInfo.setBarcode("2036");
        goodsInfo.setBasePrice(456);

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
                456, 589, 100, items, null);

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
