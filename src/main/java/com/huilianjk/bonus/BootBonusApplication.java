package com.huilianjk.bonus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.huilianjk.bonus.dao")
@SpringBootApplication
public class BootBonusApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootBonusApplication.class, args);
	}
}
