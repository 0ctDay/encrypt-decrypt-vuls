package com.demo.excepiton;


import lombok.Getter;

/**
 * @Description:
 * @Author: Rosh
 * @Date: 2021/8/10 17:39
 */
@Getter
public class RSAException extends RuntimeException {

    private final String message;


    public RSAException(String message) {
        this.message = message;
    }


}
