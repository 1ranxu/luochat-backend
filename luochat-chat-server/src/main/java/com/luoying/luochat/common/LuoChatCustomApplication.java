package com.luoying.luochat.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author 落樱的悔恨
 * @Date 2024/1/1 12:37
 */
@SpringBootApplication(scanBasePackages = {"com.luoying.luochat"})
@MapperScan({"com.luoying.luochat.common.**.mapper"})
public class LuoChatCustomApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuoChatCustomApplication.class,args);
    }

}