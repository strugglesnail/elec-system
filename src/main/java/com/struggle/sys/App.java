package com.struggle.sys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableWebFlux
@MapperScan({"com.struggle.sys.mapper"})
public class App {
    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
    }
}
