package com.hualong.duolabao.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.*;

public class Md5 {

    private static final Logger logger = LoggerFactory.getLogger(Md5.class);

	private static final String hexDigIts[] = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};

    /**
     * MD5加密
     * @param origin 需要加密的字符串
     * @param charsetname 编码
     * @return
     */
    public static String MD5Encode(String origin, String charsetname){
        String resultString = null;
        try{
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if(null == charsetname || "".equals(charsetname)){
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            }else{
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
            }
        }catch (Exception e){
            logger.error("MD5加密出错",e.getMessage());
        }
        return resultString;
    }


    public static String byteArrayToHexString(byte b[]){
        StringBuffer resultSb = new StringBuffer();
        for(int i = 0; i < b.length; i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    public static String byteToHexString(byte b){
        int n = b;
        if(n < 0){
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigIts[d1] + hexDigIts[d2];
    }

    public static  String getOrderSign(Map<String,String> paramValues){
        try{
            StringBuilder sb=new StringBuilder();
            List<String> paramNames=new ArrayList<String>(paramValues.size());
            paramNames.addAll(paramValues.keySet());
            Collections.sort(paramNames);
            for(String paraName:paramNames){
                if(paraName.equals("sign") || paraName.equals("")){
                    continue;
                }
                sb.append(paramValues.get(paraName));
            }

            System.out.println("排序 "+sb.toString());
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
            logger.error("得到的排序出错了",e.getMessage());
        }
        return "";
    }

    public static String sign(String appSecret,Map<String,String> paramValues){
        try{
            System.out.println("秘钥"+MD5Encode((appSecret+getOrderSign(paramValues)+appSecret),"UTF-8"));
            //签名加密大写转换
           return MD5Encode((appSecret+getOrderSign(paramValues)+appSecret),"UTF-8").toUpperCase();
        }catch (Exception e){
            e.printStackTrace();
           logger.error("得到签名出错了",e.getMessage());
        }
        return "";
    }

    public static boolean signIsOk(String appSecret,Map<String,String> paramValues,String sign){
        try{
            return sign(appSecret,paramValues).equals(sign);
        }catch (Exception e){
            logger.error("",e.getMessage());
        }
        return false;
    }

    /**
     *
     * @param appSecret  秘钥
     * @param param      需要做签名校验的字符串
     * @param sign       需要检验的签名
     * @return
     */
    public static boolean loginsignIsOk(String appSecret,String param,String sign){
        try{
            System.out.println("得到的签名  "+MD5Encode((appSecret+param+appSecret).trim(),"UTF-8").toUpperCase());
            return MD5Encode((appSecret+param+appSecret).trim(),"UTF-8").toUpperCase().equals(sign.trim());
        }catch (Exception e){
            logger.error("",e.getMessage());
        }
        return false;
    }

    /**
     *
     * @param args 主函数
     * @see  这里是签名
     */
    public static void main(String[] args) {

        Map<String,String> paramValues11=new HashMap<String,String>();
        System.out.println("paramValues11长度  "+paramValues11.size());

        Map<String,String> paramValues=new HashMap<String,String>();
        paramValues.put("appId","abc");
        System.out.println("paramValues长度  "+paramValues.size());
        System.out.println("我是拿到的签名排序:"+getOrderSign(paramValues));
        System.out.println("我是拿到的签名    :"+sign("f19aad29b6af9cc85b86d84e0340dcc3",paramValues));
        System.out.println(signIsOk("123456",paramValues,"5d79b4258ad6391ace4be029eae670ee"));

		System.out.println(MD5Encode("13456789555555","UTF-8"));

	}

}
