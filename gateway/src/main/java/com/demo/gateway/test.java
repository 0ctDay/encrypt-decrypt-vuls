package com.demo.gateway;

import com.demo.gateway.utils.AESUtil;

public class test {
    public static void main(String[] args) {
        String str = "uMDI+g2bagWZ6+wlwBGQYjPFu1pJ8oA39/HzrpZT7nbUDdZogctMTxiPY //vF2stkEFLwjhiJTlPZObyLKInxjIzojhVH3gIMVQ0mcbmp3fNbYVds8cN9jkSYxEgp1eWCXbPCDxKsTuXD6q671vlNGg2m7Qu/rW+2jelZWGYVse2KI6rLfkB54r23ZVHOU2gNEC6DYIvn2+vdtOe89pd7hM1XbRyBHYVjwM/0TTHrOU4k8d0oP5/vbmLHFUgYpXq4R4ZxsXa70+/w9xAjRU+THnItxiLx2HZgCros+F2I5uT0a4lBXtOZVUGYJe2PX08oi2ySmrgVrBYQhWX4z7/nW+0DCbBbOcT2rS9zWflFqUKOseKRRVrSE02Zk6+RgfX9yn/BTbhPjJezI9NdbZYQ==";
        //String encrypt = AESUtil.encrypt(str, "MTIzNDU2Nzg5MTIzNDU2Nw==");
        String decrypt = AESUtil.decrypt(str, "MTIzNDU2Nzg5MTIzNDU2Nw==");
        System.out.println(decrypt);
    }
}
