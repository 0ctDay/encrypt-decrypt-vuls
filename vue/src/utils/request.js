import axios from 'axios'
import router from "../router";
import CryptoJS from 'crypto-js';
import {ElMessage} from "element-plus";


const request = axios.create({
    baseURL: 'http://192.168.202.220:9100',
    timeout: 5000
})

const key = CryptoJS.enc.Utf8.parse('1234567891234567');
const iv = CryptoJS.enc.Utf8.parse('1234567891234567');
// 加密函数
function encrypt(text) {
  const cipher = CryptoJS.AES.encrypt(text, key, { iv: iv, mode: CryptoJS.mode.CBC,padding: CryptoJS.pad.Pkcs7  });
  return cipher.toString();
}

// 解密函数
function decrypt(ciphertext) {
  const bytes = CryptoJS.AES.decrypt(ciphertext, key, { iv: iv, mode: CryptoJS.mode.CBC,padding: CryptoJS.pad.Pkcs7 });
  return bytes.toString(CryptoJS.enc.Utf8);
}

function getUuid() {
    const hexDigits = "0123456789abcdef";
    let s = Array.from({ length: 32 }, () => hexDigits.substr(Math.floor(Math.random() * 0x10), 1));
    s[14] = "4";
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
    s[8] = s[13] = s[18] = s[23];
    return s.join("");
  }
function sortASCII(obj) {
    obj = removeEmptyValues(obj);
    var arr = new Array();
    var num = 0;
    for (var i in obj) {
        arr[num] = i;
        num++;
    }
    var sortArr = arr.sort();
    var sortObj = {};
    for (var i in sortArr) {
        sortObj[sortArr[i]] = obj[sortArr[i]];
    }
    return sortObj;
}
function removeEmptyValues(obj) {
    for (let key in obj) {
        if (obj[key] === null || obj[key] === undefined || obj[key] === '') {
            delete obj[key];
        }
    }
    return obj;
}
// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['X-Requested-With'] = 'XMLHttpRequest'
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
      // 设置请求头
    //取出sessionStorage里面缓存的用户信息
    let userJson = sessionStorage.getItem("user")
    
    //签名
    const timestamp = Date.parse(new Date());
    const data = JSON.stringify(sortASCII(config.data));
    const requestId = getUuid();
    const sign = CryptoJS.MD5(data + requestId + timestamp);


    config.headers['timestamp'] = timestamp
    config.headers['requestId'] = requestId
    config.headers['sign'] = sign
    
    config.data = encrypt(data);

    //处理请求体
    
    const hash = window.location.hash;
    const path = hash.slice(1);
    if(userJson)
    {
        try{
            let token = JSON.parse(userJson).token
            config.headers['token'] = token
        
        }
        catch(e){
            ElMessage.error(e)
        }
    }
    else if(path.startsWith('/forget')){
    
    }
    else{
        router.push("/login")
    }
    
    
    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {
            res = decrypt(res)
            return res
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = decrypt(res)
            res = res ? JSON.parse(res) : res
        }
        return res;
    },
    error => {
        console.log('err' + error) // for debug
        return Promise.reject(error)
    }
)


export default request

