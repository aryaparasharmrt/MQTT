package com.dwellsmart.modbus;

import com.dwellsmart.dto.MeterData;

public interface IMeter {
	
	boolean connect();
	boolean disconnect();
	MeterData readMeter();
	

}
