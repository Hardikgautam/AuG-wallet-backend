package com.prac_icsd2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IcsdPracApplication {

	public static void main(String[] args) {
		SpringApplication.run(IcsdPracApplication.class, args);
	}

}
