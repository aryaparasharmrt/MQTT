package com.dwellsmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MQTTClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MQTTClientApplication.class, args);
		
		System.out.println("Application Started...."); //TODO for logger

		synchronized (MQTTClientApplication.class) {
			try {
				MQTTClientApplication.class.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
