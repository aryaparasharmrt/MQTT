package com.dwellsmart.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwellsmart.constants.MeterType;
import com.dwellsmart.pojo.MeterAddressMap;

import net.wimpi.modbus.net.RTUTCPMasterConnection;

@Service
public class CacheService {
	
//	@Autowired
//	private ModbusService modbusService;

	private final Map<MeterType, MeterAddressMap> meterAddressMapCache = new ConcurrentHashMap<>();
	
	
	 private final Map<String, RTUTCPMasterConnection> masterConnectionCache = new ConcurrentHashMap<>();

	    public RTUTCPMasterConnection getRTUTCPMasterConnection(String ipAddress) {
	        return masterConnectionCache.computeIfAbsent(ipAddress, this::createConnection);
	    }

	    private RTUTCPMasterConnection createConnection(String ipAddress) {
	    	
//	    	modbusService.getConnectionToModbusServer(ipAddress);
	        return null;
//	        try {
//	            // Create a new Modbus connection
//	            ModbusConnection connection = new ModbusConnection(ipAddress);
//	            connection.connect(); // Actual connection logic
//	            return connection;
//	        } catch (Exception e) {
//	            throw new RuntimeException("Failed to connect to IP: " + ipAddress, e);
//	        }
	    }
	    

	// Method to get the meter address map based on meter type
	public MeterAddressMap getMeterAddressMap(MeterType meterType) {
		return meterAddressMapCache.computeIfAbsent(meterType, this::loadMeterAddressMap);
	}

	// Method to load meter address map for a given meter type
	private MeterAddressMap loadMeterAddressMap(MeterType meterType) {
		MeterAddressMap addressMap = new MeterAddressMap();

		switch (meterType) {

		case SUNSTAR_DS:
			addressMap.setMeterType(meterType);
			addressMap.setKwhRegisterAddress(8316);
			addressMap.setKwhRegisterAddressesCount(98);
			addressMap.setConnectRegisterAddress(770);
			addressMap.setConnectRegisterValue(20560);
			addressMap.setDisconnectRegisterAddress(770);
			addressMap.setDisconnectRegisterValue(0);
			addressMap.setOtherRegisterAddress(12343);
			addressMap.setOtherRegistersCount(52);
			addressMap.setDgKwhRegisterAddress(0);
			addressMap.setLoadRegisterAddress(785);
			addressMap.setOverloadattemptsRegAddress(817);
			addressMap.setOverloadattemptsdelayRegAddress(819);
			return addressMap;

		case SUNSTAR_DS_PP:
			addressMap.setMeterType(meterType);
			addressMap.setKwhRegisterAddress(8316);
			addressMap.setKwhRegisterAddressesCount(98);
			addressMap.setConnectRegisterAddress(770);
			addressMap.setConnectRegisterValue(20560);
			addressMap.setDisconnectRegisterAddress(770);
			addressMap.setDisconnectRegisterValue(0);
			addressMap.setOtherRegisterAddress(12343);
			addressMap.setOtherRegistersCount(52);
			addressMap.setDgKwhRegisterAddress(0);
			addressMap.setLoadRegisterAddress(785);
			addressMap.setOverloadattemptsRegAddress(817);
			addressMap.setOverloadattemptsdelayRegAddress(819);
			addressMap.setPasswordRegisterAddress(2672);
			addressMap.setValidatorRegisterAddress(2929);
			addressMap.setChangeMeterIdAddress(769);
			return addressMap;

		case ENERTRAK:
			addressMap.setMeterType(meterType);
			addressMap.setKwhRegisterAddress(16508);
			addressMap.setKwhRegisterAddressesCount(98);
			addressMap.setConnectRegisterAddress(2);
			addressMap.setConnectRegisterValue(20560);
			addressMap.setDisconnectRegisterAddress(2);
			addressMap.setDisconnectRegisterValue(0);
			addressMap.setOtherRegisterAddress(20535);
			addressMap.setOtherRegistersCount(52);
			addressMap.setDgKwhRegisterAddress(0);
			addressMap.setLoadRegisterAddress(17);
			addressMap.setOverloadattemptsRegAddress(49);
			addressMap.setOverloadattemptsdelayRegAddress(51);
			return addressMap;
		}
		return addressMap;
	}
}