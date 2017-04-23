package com.diplom.ilya.diplom.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by user on 12.04.17.
 */
public class Utils {
    public static String getMD5(String s) throws NoSuchAlgorithmException {
        String plaintext = s;
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(plaintext.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(16);
        while(hashtext.length() < 32 ){
            hashtext = "0"+hashtext;
        }
        return hashtext;
    }

    public static String encodeURL(String source) {
        System.out.println("source = [" + source + "]");
        String result = "";
        for (String s: source.split("/")) {
            try {
                result += "/" + URLEncoder.encode(s, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result.replace("+", "%20");
    }
}
