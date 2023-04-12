package com.login.demo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * Description: 加密解密处理
 */
public class DataProcessUtils {
    private static final String CHARSET = "UTF-8";

    public static String getSign(Map<String, String> requestMap, String appKey) {
        return hmacSHA256Encrypt(requestMap2Str(requestMap), appKey);
    }

    private static String hmacSHA256Encrypt(String encryptText, String encryptKey) {
        byte[] result = null;
        try {
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKeySpec signinKey = new SecretKeySpec(encryptKey.getBytes(CHARSET), "HmacSHA256");
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance("HmacSHA256");
            //用给定密钥初始化 Mac 对象
            mac.init(signinKey);
            //完成 Mac 操作
            byte[] rawHmac = mac.doFinal(encryptText.getBytes(CHARSET));
            return bytesToHexString(rawHmac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String requestMap2Str(Map<String, String> requestMap) {
        String[] keys = requestMap.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : keys) {
            if (!str.equals("sign")) {
                stringBuilder.append(str).append(requestMap.get(str));
            }
        }
        return stringBuilder.toString();
    }

    /**
     * AES加密
     */
    public static String aesEncrypt(String sSrc, String sKey, String siv) {
        try {
            if (sSrc == null || sSrc.length() == 0) {
                return null;
            }
            if (sKey == null) {
                throw new Exception("encrypt key is null");
            }
            if (sKey.length() != 16) {
                throw new Exception("encrypt key length error");
            }
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes(CHARSET), "AES");
            IvParameterSpec iv = new IvParameterSpec(siv.getBytes(CHARSET));
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            return bytesToHexString(cipher.doFinal(sSrc.getBytes(CHARSET)));
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);

        for (byte aBArray : bArray) {
            String sTemp = Integer.toHexString(255 & aBArray);
            if (sTemp.length() < 2) {
                sb.append(0);
            }

            sb.append(sTemp.toUpperCase());
        }

        return sb.toString();
    }

    /**
     * MD5加密
     *
     * @param str 需加密字符串
     * @return
     */
    public static String md5(String str) {
        try {
            MessageDigest alga = MessageDigest.getInstance("MD5");
            alga.update(str.getBytes());
            byte[] digesta = alga.digest();
            return byte2hex(digesta);
        } catch (Exception e) {
        }
        return "";
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1) {
                hs = new StringBuilder().append(hs).append("0").append(stmp).toString();
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }


    /**
     * Description: AES解密
     */
    public static String decrypt(String sSrc, String appSecret) throws Exception {
        String md5Key = md5(appSecret).toUpperCase();//32位MD5转大写
        String sKey = md5Key.substring(0, 16);
        String siv = md5Key.substring(16);
        byte[] Decrypt = hexToBytes(sSrc);//转为字节数组
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes(CHARSET), "AES");
        IvParameterSpec iv = new IvParameterSpec(siv.getBytes(CHARSET));
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);//使用解密模式初始化
        return new String(cipher.doFinal(Decrypt), CHARSET);
    }

    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else {
            char[] hex = str.toCharArray();
            int length = hex.length / 2;
            byte[] raw = new byte[length];

            for (int i = 0; i < length; ++i) {
                int high = Character.digit(hex[i * 2], 16);
                int low = Character.digit(hex[i * 2 + 1], 16);
                int value = high << 4 | low;
                if (value > 127) {
                    value -= 256;
                }

                raw[i] = (byte) value;
            }

            return raw;
        }
    }

    //获取包签名MD5
    public static String getPackageSign(Context context) {
        String signStr = "-1";
        if (context != null) {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo;
            try {
                packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            } catch (PackageManager.NameNotFoundException e) {
                packageInfo = null;
            }
            if (null != packageInfo) {
                Signature[] signs = packageInfo.signatures;
                if (null != signs && signs.length > 0) {
                    Signature sign = signs[0];
                    signStr = encryptionMD5(sign.toByteArray()).toUpperCase();
                }
            }
        }
        return signStr;
    }

    private static String encryptionMD5(byte[] byteStr) {
        MessageDigest messageDigest;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
            for (byte aByteArray : byteArray) {
                if (Integer.toHexString(0xFF & aByteArray).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & aByteArray));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & aByteArray));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }
}
