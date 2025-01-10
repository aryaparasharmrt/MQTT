package com.dwellsmart.factory;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.dwellsmart.constants.MeterType;
import com.dwellsmart.modbus.IMeter;
import com.dwellsmart.modbus.meter.LnT;
import com.dwellsmart.modbus.meter.SumeruVerde;
import com.dwellsmart.modbus.meter.SunStarDS;
import com.dwellsmart.service.CacheService;

import lombok.extern.slf4j.Slf4j;
import net.wimpi.modbus.net.RTUTCPMasterConnection;

@Component
@Slf4j
public class MeterFactory {

	private final CacheService cacheService;

	private final ApplicationContext applicationContext;

	public MeterFactory(ApplicationContext applicationContext, CacheService cacheService) {
		this.cacheService = cacheService;
		this.applicationContext = applicationContext;
	}

	public IMeter getMeter(MeterType meterType, short meterId, RTUTCPMasterConnection connection) {
		switch (meterType) {
		
		case SUNSTAR_DS: // Meter Type 1
		case SUNSTAR_DS_PP: // Meter Type 9
			return applicationContext.getBean(SunStarDS.class, meterId, cacheService.getMeterAddressMap(meterType), connection);
			
		case ENERTRAK: // Meter Type 2
			log.warn("ENERTRAK Meter type not implemented yet.");
			return null;
			
		case SUMERU_VERDE: // Meter Type 4
		case SUMERU_VERDE_MSB: // Meter Type 8
			return applicationContext.getBean(SumeruVerde.class, meterId, cacheService.getMeterAddressMap(meterType), connection);
			
		case LNT1P: // Meter Type 5
			return applicationContext.getBean(LnT.class, meterId, cacheService.getMeterAddressMap(meterType), connection);
			
		case UNKNOWN:
			log.warn("Unknown MeterType ID received. Skipping specific actions.");
			return null;
			
		default:
			log.warn("Unknown meter type or not implemented yet.");
			return null;
		}
	}

}
