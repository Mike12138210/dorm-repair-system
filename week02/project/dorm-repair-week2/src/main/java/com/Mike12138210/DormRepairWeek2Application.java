package com.Mike12138210;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 引导类，SpringBoot项目的入口

@MapperScan("com.Mike12138210.mapper")
@SpringBootApplication
public class DormRepairWeek2Application {
	public static void main(String[] args) {
        SpringApplication.run(DormRepairWeek2Application.class, args);
	}
}