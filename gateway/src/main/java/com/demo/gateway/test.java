package com.demo.gateway;

import com.demo.gateway.utils.AESUtil;

public class test {
    public static void main(String[] args) {
        String str = "CvCb2zEd9u1VL0zEfulTyppeV0QmnjSwFLjZHEsXdzYdYcXuEo+p3ayKLUwt1mN+";
        //String encrypt = AESUtil.encrypt(str, "MTIzNDU2Nzg5MTIzNDU2Nw==");
        String decrypt = AESUtil.decrypt(str, "MTIzNDU2Nzg5MTIzNDU2Nw==");
        System.out.println(decrypt);
    }
}
