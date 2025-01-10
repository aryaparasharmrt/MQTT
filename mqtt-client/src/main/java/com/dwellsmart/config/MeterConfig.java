package com.dwellsmart.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.dwellsmart.modbus.meter.LnT;
import com.dwellsmart.modbus.meter.SumeruVerde;
import com.dwellsmart.modbus.meter.SunStarDS;
import com.dwellsmart.pojo.MeterAddressMap;

import net.wimpi.modbus.net.RTUTCPMasterConnection;

@Configuration
public class MeterConfig {

	
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	SunStarDS sunStarMeterDwellSMART(short slaveId, MeterAddressMap addressMap, RTUTCPMasterConnection connection) {
		return new SunStarDS(slaveId, addressMap, connection);
	}
	
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	SumeruVerde sumeruVerdeMeter(short slaveId, MeterAddressMap addressMap, RTUTCPMasterConnection connection) {
		return new SumeruVerde(slaveId, addressMap, connection);
	}
	
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	LnT lntSinglePhaseMeter(short slaveId, MeterAddressMap addressMap, RTUTCPMasterConnection connection) {
		return new LnT(slaveId, addressMap, connection);
	}

}
