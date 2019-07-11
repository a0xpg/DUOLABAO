package com.hualong.duolabao.tool;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * @模块名：taf
 * @包名：com.tit.taf.encryption
 * @类名称： ThreeDESUtil
 * @类描述：【类描述】针对des算法进行了改进，有了三重des算法（DESede）。
 * 针对des算法的密钥长度较短以及迭代次数偏少问题做了相应改进，提高了安全强度。
 * 不过desede算法处理速度较慢，密钥计算时间较长，加密效率不高问题使得对称加密算法的发展不容乐观。
 * @版本：1.0
 * @创建人：cc
 * @创建时间：2018年8月24日上午10:04:40
 *
 *  加密方式 ：算法模式: CBC 秘钥长度: 256 秘钥偏移量: 01234567 补码方式: PKCS5Padding 加密结果编码方式: base64 字符集编码: UTF-8
 *
 *   key "123456788765432112345678",
 *  vi  "01234567"
 */

public class ThreeDESUtil {
    /**
     * 定义 加密算法,可用 DES,DESede,Blowfish
     */
    private static final String Algorithm = "DESede";

    /**
     * 算法/模式/补码方式
     */
    public static final String ALGORITHM_DES = "DESede/CBC/PKCS5Padding";

    // 加解密统一使用的编码方式
    private final static String ENCODING = "UTF-8";

    /**
     *
     * @方法名：encrypt 3des加密
     * @方法描述【方法功能描述】
     * @param data 明文
     * @param keystr 密钥
     * @param iv 向量
     * @return 加密后密文
     * @throws Exception
     * @修改描述【修改描述】
     * @版本：1.0
     * @创建人：cc
     * @创建时间：2018年8月27日 上午9:52:19
     * @修改人：cc
     * @修改时间：2018年8月27日 上午9:52:19
     */
    public static String encrypt(String data, String keystr, String iv) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(keystr.getBytes(ENCODING));
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(Algorithm);
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(data.getBytes());
        return Base64.encode(encryptData);
    }

    /**
     *
     * @方法名：decrypt 3des解密
     * @方法描述【方法功能描述】
     * @param data 密文
     * @param keystr 密钥
     * @param iv 向量
     * @return 加密后明文
     * @throws Exception
     * @修改描述【修改描述】
     * @版本：1.0
     * @创建人：cc
     * @创建时间：2018年8月27日 上午9:53:16
     * @修改人：cc
     * @修改时间：2018年8月27日 上午9:53:16
     */
    public static String decrypt(String data, String keystr, String iv) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(keystr.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(Algorithm);
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] decryptData = cipher.doFinal(Base64.decode(data));
        return new String(decryptData, ENCODING);
    }

    public static void main(String[] args) throws Exception {
//        System.out.println(ThreeDESUtil.encrypt("cc", "123456788765432112345678", "01234567"));
//        System.out.println(ThreeDESUtil.decrypt("+TFTNLC5gvg=", "123456788765432112345678", "01234567"));

        String abc=
                ThreeDESUtil.
                        encrypt("20181211101847eyJleHAiOjE1NDI3OTE4NDU",
                                "123456788765432112345678",
                                "01234567");
        System.out.println(abc);

        System.out.println(abc.replaceAll("=","").replaceAll("\\+","").replaceAll("\\s*","").replaceAll("\\n","").trim());
        try{
            System.out.println(ThreeDESUtil.decrypt("xGCVLBExxztet6+I11c3uBVQiwhq+6Nb1dnfFBmBmDW3colqYRg+SQ==", "123456788765432112345678", "01234567"));
        }catch (Exception e){
            System.out.println("zmy");
            //throw  new Exception("出错了");
    }

    }
}

