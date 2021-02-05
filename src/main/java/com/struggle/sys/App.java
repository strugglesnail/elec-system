package com.struggle.sys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @auther strugglesnail
 * @date 2021/1/18 21:48
 * @desc
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan({"com.struggle.sys.mapper"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
