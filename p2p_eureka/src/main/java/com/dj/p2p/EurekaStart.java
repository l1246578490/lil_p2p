package com.dj.p2p;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaStart {

    public static void main(String[] args) {
        SpringApplication.run(EurekaStart.class, args);
    }

}
