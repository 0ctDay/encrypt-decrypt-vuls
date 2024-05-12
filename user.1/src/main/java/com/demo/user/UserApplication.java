package com.demo.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Description:
 * @Author: rosh
 * @Date: 2021/10/24 23:59
 * //
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UserApplication {


    public static void main(String[] args) {

        SpringApplication.run(UserApplication.class);

    }


}
