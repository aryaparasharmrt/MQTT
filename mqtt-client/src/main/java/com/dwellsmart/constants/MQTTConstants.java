package com.dwellsmart.constants;

import java.time.Duration;

public final class MQTTConstants {

	public static final String ENDPOINT = "a23bq50uzv6v7o-ats.iot.ap-south-1.amazonaws.com";
	public static final String CERT_PATH = "D:\\OneDrive\\Team Shared\\mqttcerts\\TestLaptopNikhil.cert.pem";
	public static final String KEY_PATH = "D:\\OneDrive\\Team Shared\\mqttcerts\\TestLaptopNikhil.private.key";
	public static final String CA_PATH = "D:\\OneDrive\\Team Shared\\mqttcerts\\root-CA.crt";
	
//	public static final String BASE_PATH = System.getProperty("user.dir"); // Current working directory for linux
//	public static final String CERT_PATH = BASE_PATH + "/mqttcerts/TestLaptopNikhil.cert.pem";
//	public static final String KEY_PATH = BASE_PATH + "/mqttcerts/TestLaptopNikhil.private.key";
//	public static final String CA_PATH = BASE_PATH + "/mqttcerts/root-CA.crt";


	public static final String SUBSCRIBE_TOPIC = "sdk/modbus/request/1";
	public static final String PUBLISH_TOPIC = "sdk/modbus/response/2";
	public static final String CLIENT_ID = "basestation2";
	
	public static final int MAX_RETRIES = 1; 
	
	public static final Duration MESSAGE_VALIDITY_DURATION = Duration.ofMinutes(10);

}
