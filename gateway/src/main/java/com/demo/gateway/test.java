package com.demo.gateway;

import com.demo.gateway.utils.AESUtil;

public class test {
    public static void main(String[] args) {
        String str = "0uMDI+g2bagWZ6+wlwBGQYjPFu1pJ8oA39/HzrpZT7nbUDdZogctMTxiPY//vF2stkEFLwjhiJTlPZObyLKInxjIzojhVH3gIMVQ0mcbmp3fNbYVds8cN9jkSYxEgp1e90mT1WH+BGUSsET6ZQd0iXnxXuie7GXhfTHb92NpSGjCtag/H4uXl0D6b2a5SRNUzfweu3O5A7sgO2sAO0i+4d6MNDFBZrTos1G4E4nT6McEGytLFCybV05lapbpY2XwhWoDG1cZOiUFnnxhaAOruvSyi0A3jaz0CZYSEDHEYtzILLEQVLR++cG83Sh4T8UK1aKERw/uV9k/Z6qxtB+4AjtagvGpMyNEr94QkJvX2L9AA3K3hZ96YQFlpD0NxLF99mFcFlAXEVhN3V7PJTqKeA==";
        //String encrypt = AESUtil.encrypt(str, "MTIzNDU2Nzg5MTIzNDU2Nw==");
        String decrypt = AESUtil.decrypt(str, "MTIzNDU2Nzg5MTIzNDU2Nw==");
        System.out.println(decrypt);
    }
}
