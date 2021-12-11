package com.uyu.device.devicetraining.domain.util;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by windern on 2015/12/1.
 */
public class AccountManager {
    /**
     * sha密码
     * @param Password
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String encodePassword(String Password) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA-512");
        String aftersalt = "uY2015"+Password+"uYanlikang";
        byte[] srcBytes = aftersalt.getBytes();
        //使用srcBytes更新摘要
        sha.update(srcBytes);
        //完成哈希计算，得到result
        byte[] resultBytes = sha.digest();
        String encrypedValue = Base64.encodeToString(resultBytes, Base64.DEFAULT);
        //跟ios相比，多了几个\n
        encrypedValue = encrypedValue.replace("\n","");
        return encrypedValue;
    }
}
