package com.demo.gateway.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil {
    static String base64EncodedIV = "MTIzNDU2Nzg5MTIzNDU2Nw==";  //1234567891234567
    public static String decrypt(String encryptedText, String base64EncodedKey) {
        try {
            // 解码密钥

            byte[] decodedKey = Base64.getDecoder().decode(base64EncodedKey);
            SecretKeySpec keySpec = new SecretKeySpec(decodedKey, "AES");
            byte[] decodedIV = Base64.getDecoder().decode(base64EncodedIV);
            IvParameterSpec ivSpec = new IvParameterSpec(decodedIV);
            // 创建 AES 密码器
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            // 解密
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));

            // 将解密后的字节数组转换为字符串并返回
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String encrypt(String plainText, String base64EncodedKey) {
        try {
            // 解码密钥
            byte[] decodedKey = Base64.getDecoder().decode(base64EncodedKey);
            SecretKeySpec keySpec = new SecretKeySpec(decodedKey, "AES");

            byte[] decodedIV = Base64.getDecoder().decode(base64EncodedIV);
            IvParameterSpec ivSpec = new IvParameterSpec(decodedIV);

            // 创建 AES 密码器
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            // 加密
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());


            // 将加密后的字节数组和 Base64 编码的初始化向量一起返回
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
