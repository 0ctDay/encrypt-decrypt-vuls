package com.demo.gateway;

import com.demo.gateway.utils.AESUtil;

public class test {
    public static void main(String[] args) {
        String str = "0uMDI+g2bagWZ6+wlwBGQYjPFu1pJ8oA39/HzrpZT7kX2qT0VPUYlrieNNqEtcWTUWyz3Xn8aIlE8IqyB/AJP+Z4PZunY7NXOdscNam/u8Gmpfzxj8VupNaKw05pXPkNcUGEjVRIUJqmGUEUMJyixuInQkuSqs6InJG9ry4gJE/Iw8AyJwexuD5njeVqAwvwzaocFftGQOzSD0GFzyEyoTNgb0R10wkdt166+qv5XtozWrJ2/BQGk4rkW3zakbqf";
        //String encrypt = AESUtil.encrypt(str, "MTIzNDU2Nzg5MTIzNDU2Nw==");
        String decrypt = AESUtil.decrypt(str, "MTIzNDU2Nzg5MTIzNDU2Nw==");
        System.out.println(decrypt);
    }
}
