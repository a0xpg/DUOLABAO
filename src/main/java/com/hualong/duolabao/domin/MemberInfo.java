package com.hualong.duolabao.domin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.*;
import lombok.experimental.Accessors;


import java.math.BigDecimal;

/**
 * Created by Administrator on 2019-07-09.
 */

@Data
@ToString(callSuper = true)
@Accessors(chain=true)
@EqualsAndHashCode(callSuper = true)
public class MemberInfo extends DlbCommon{

    private String userId;
    private String userType;
    private String userNick;
    private String cardNum;
    private String phoneNum;
    private String score;
    /**
     * 新增的我们线下的数据  方便得到会员价
     */
    private String bDiscount;
    private String fPFrate;

    /**
     * 客户应该增加的积分
     */
    private Double addScore;

    /**
     *
     * @param storeId
     * @param sn
     * @param cartId
     * @param addScore
     */
    public MemberInfo(String storeId, String sn, String cartId, double addScore) {
        super(storeId, sn, cartId);
        this.addScore = addScore;
    }

    public MemberInfo(String storeId, String sn, String cartId, String userId,
                      String userType, String userNick, String cardNum,
                      String phoneNum, String score, String bDiscount, String fPFrate) {
        super(storeId, sn, cartId);
        this.userId = userId;
        this.userType = userType;
        this.userNick = userNick;
        this.cardNum = cardNum;
        this.phoneNum = phoneNum;
        this.score = score;
        this.bDiscount = bDiscount;
        this.fPFrate = fPFrate;
    }

    public MemberInfo() {
    }

    public MemberInfo(String userId, String userType, String userNick, String cardNum, String phoneNum, String score) {
        this.userId = userId;
        this.userType = userType;
        this.userNick = userNick;
        this.cardNum = cardNum;
        this.phoneNum = phoneNum;
        this.score = score;
    }

    public MemberInfo(String storeId, String sn, String cartId, String userId,
                      String userType, String userNick, String cardNum,
                      String phoneNum, String score) {
        super(storeId, sn, cartId);
        this.userId = userId;
        this.userType = userType;
        this.userNick = userNick;
        this.cardNum = cardNum;
        this.phoneNum = phoneNum;
        this.score = score;
    }

    public static void main(String[] args) {
        MemberInfo memberInfo=new MemberInfo();
        memberInfo.setCartId("123");

        System.out.println(JSON.toJSONString(memberInfo));

        //JSON.parseObject()
    }

}
