package com.supets.commons.utils;

import android.annotation.SuppressLint;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class EncryptUtils {
    /**
     * base64加密
     *
     * @param str
     * @return
     */
    public static String encryptBASE64(String str) {
        return encryptBASE64(str,"supets");
    }

    public static String encryptBASE64(String str,String key) {
        if (str == null || str.length() == 0) {
            return null;
        }
        String mStr = key.concat(str);
        try {
            byte[] encode = mStr.getBytes("UTF-8");
            // base64 加密
            return new String(Base64.encode(encode, 0, encode.length, Base64.NO_WRAP), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String decodeBASE64(String str) {
        return decodeBASE64(str,"supets");
    }

    public static String decodeBASE64(String str,String key) {
        if (str == null || str.length() == 0) {
            return "";
        }
        try {
            byte[] encode = str.getBytes("UTF-8");
            String string = new String(Base64.decode(encode, 0, encode.length, Base64.NO_WRAP), "UTF-8");
            return string.substring(key.length());
        } catch (Exception e) {
        }

        return "";
    }


    @SuppressLint("TrulyRandom")
    public static String rsa(String input, String publicKeyStr) {
        try {
            RSAPublicKey publicKey = readRSAPublicKey(publicKeyStr);

            int key_len = publicKey.getModulus().bitLength() / 8;
            int length = key_len - 11;

            String[] datas = splitString(input, length / 3);
            StringBuilder sb = new StringBuilder();

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            for (String b : datas) {
                sb.append(Base64.encodeToString(cipher.doFinal(b.getBytes("UTF-8")), Base64.NO_WRAP));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private static RSAPublicKey readRSAPublicKey(String key) throws Exception {
        byte[] decoded = Base64.decode(key, Base64.NO_WRAP);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(spec);
    }

    private static String[] splitString(String str, int len) {
        int size = (str.length() + len - 1) / len;
        String[] arr = new String[size];
        for (int i = 0; i < size; i++) {
            int length = i == size - 1 ? str.length() - (i * len) : len;
            arr[i] = str.substring(i * len, i * len + length);
        }
        return arr;
    }

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes("UTF-8"));
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                if ((b & 0xFF) < 0x10)
                    sb.append("0");
                sb.append(Integer.toHexString(b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            return input;
        }
    }
}
