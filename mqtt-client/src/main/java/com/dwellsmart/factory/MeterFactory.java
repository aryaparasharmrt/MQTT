package com.dwellsmart.factory;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.dwellsmart.constants.MeterType;
import com.dwellsmart.modbus.IMeter;
import com.dwellsmart.modbus.meter.SunStarDS;
import com.dwellsmart.modbus.meter.SunStarDSPP;
import com.dwellsmart.service.CacheService;

import net.wimpi.modbus.net.RTUTCPMasterConnection;

@Component
public class MeterFactory {

	private final CacheService cacheService;

	private final ApplicationContext applicationContext;

	public MeterFactory(ApplicationContext applicationContext, CacheService cacheService) {
		this.cacheService = cacheService;
		this.applicationContext = applicationContext;
	}

	public IMeter getMeter(MeterType meterType, short meterId, RTUTCPMasterConnection connection) {
		switch (meterType) {
		case SUNSTAR_DS:    //Meter Type 1
			return applicationContext.getBean(SunStarDS.class, meterId, cacheService.getMeterAddressMap(meterType),connection);
		case SUNSTAR_DS_PP:  //Meter Type 9
			return applicationContext.getBean(SunStarDSPP.class, meterId, cacheService.getMeterAddressMap(meterType),connection);
		case ENERTRAK:
			return null;
		default:
			throw new IllegalArgumentException("Unknown meter type");
		}
	}

}
