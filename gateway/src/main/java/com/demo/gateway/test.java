package com.demo.gateway;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.gateway.utils.AESUtil;

public class test {
    public static void main(String[] args) {
        String str = "UwIp0SbT5vF5Q7hb/uYPNFCgg37rUzAEmWF+CVMGWhF+g/0VGEMNafllCiTJSHLio9Uj3ZLn79ZyXsIbomhNCg==";
        //String encrypt = AESUtil.encrypt(str, "MTIzNDU2Nzg5MTIzNDU2Nw==");
        String decrypt = AESUtil.decrypt(str, "MTIzNDU2Nzg5MTIzNDU2Nw==");
        System.out.println(decrypt);

    }
}
