package com.td.server;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Slf4j
@SpringBootApplication
@MapperScan("com.td.server.mapper")
public class TdAdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TdAdminServerApplication.class, args);
        log.info("服务器启动成功");
    }

}
