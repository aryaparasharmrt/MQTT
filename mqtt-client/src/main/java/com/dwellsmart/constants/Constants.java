package com.dwellsmart.constants;

import java.time.Duration;

public final class Constants {
	
	//Modbus Configuration
	public static final int MODBUS_MAX_RETRIES = 1; 
	
	//Cache Configuration
	public static final Duration MESSAGE_VALIDITY_DURATION = Duration.ofMinutes(10);
    public static final int MAX_CACHE_SIZE = 5000;
    public static final long EXPIRY_DURATION = 6 * 60 * 60 * 1000;  // Cache expiry duration (6 hours in milliseconds)

}
