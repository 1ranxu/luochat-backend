package com.luoying.luochat.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/1 12:37
 */
@SpringBootApplication(scanBasePackages = {"com.luoying.luochat"})
@ServletComponentScan
public class LuoChatCustomApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuoChatCustomApplication.class,args);
    }

}