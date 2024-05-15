package com.demo.gateway;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.gateway.utils.AESUtil;

public class test {
    public static void main(String[] args) {
        String str = "CvCb2zEd9u1VL0zEfulTyppeV0QmnjSwFLjZHEsXdzYdYcXuEo+p3ayKLUwt1mN+";
        //String encrypt = AESUtil.encrypt(str, "MTIzNDU2Nzg5MTIzNDU2Nw==");
        String decrypt = AESUtil.decrypt(str, "MTIzNDU2Nzg5MTIzNDU2Nw==");
        System.out.println(decrypt);

        JSONObject jsonObject = JSON.parseObject("{\"bookname\":\"算法笔记\",\"borrownum\":2,\"isbn\":\"92392321222\",\"lendTime\":\"2021-12-18 10:57:02\",\"readerId\":13,\"returnTime\":\"2021-12-18 11:03:55\",\"status\":\"1\"}");
        System.out.println(jsonObject.getString("bookname"));
    }
}
