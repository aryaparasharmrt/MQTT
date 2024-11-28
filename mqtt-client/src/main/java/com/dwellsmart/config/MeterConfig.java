package com.dwellsmart.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.dwellsmart.modbus.meter.SunStarDS;
import com.dwellsmart.modbus.meter.SunStarDSPP;
import com.dwellsmart.pojo.MeterAddressMap;

import net.wimpi.modbus.net.RTUTCPMasterConnection;

@Configuration
public class MeterConfig {

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	SunStarDS sunStarMeterDwellSMARTV9(short slaveId, MeterAddressMap addressMap, RTUTCPMasterConnection connection) {
		return new SunStarDS(slaveId, addressMap, connection);
	}
	
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	SunStarDSPP sunStarMeterDwellSMARTV9PP(short slaveId, MeterAddressMap addressMap, RTUTCPMasterConnection connection) {
		return new SunStarDSPP(slaveId, addressMap, connection);
	}

}
