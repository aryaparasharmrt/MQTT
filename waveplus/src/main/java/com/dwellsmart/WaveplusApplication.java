package com.dwellsmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//@SpringBootApplication(scanBasePackages = { "com.cpt.payments" },exclude = {UserDetailsServiceAutoConfiguration.class })
//@EnableAsync
//@EnableScheduling
//@EnableDiscoveryClient
@SpringBootApplication
public class WaveplusApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaveplusApplication.class, args);
		System.out.println("application started");
	}
	
}
