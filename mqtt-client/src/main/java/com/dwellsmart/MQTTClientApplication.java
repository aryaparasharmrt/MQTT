package com.dwellsmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class MQTTClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MQTTClientApplication.class, args);
		
		log.info("Application Started....");

		synchronized (MQTTClientApplication.class) {
			try {
				MQTTClientApplication.class.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
