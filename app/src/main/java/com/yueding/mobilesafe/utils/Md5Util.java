package com.yueding.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yueding on 2017/8/28.
 * MD5加密工具类
 */

public class Md5Util {

    /**
     * 一次MD5算法加密
     * @param content 需要加密内容
     * @return 加密后的结果
     */
    public static String getMD5(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            return getHashString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }

    /**
     * MD5算法加密100次
     * @param content 需要加密内容
     * @return 加密后的结果
     */
    public static String getMD5Pro(String content){
        String ss = content;
        for(int i = 0;i < 100;i++){
            ss = getMD5(ss);
        }
        return ss;
    }
}
