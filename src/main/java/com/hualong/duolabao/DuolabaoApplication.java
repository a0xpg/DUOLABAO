package com.hualong.duolabao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement  //开启事务处理
@SpringBootApplication
@EnableScheduling
@ServletComponentScan
public class DuolabaoApplication  extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(DuolabaoApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DuolabaoApplication.class);

	}

}
