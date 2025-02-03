package com.dwellsmart.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.dwellsmart.constants.Constants;
import com.dwellsmart.constants.MeterType;
import com.dwellsmart.pojo.MeterAddressMap;

import lombok.extern.slf4j.Slf4j;
import net.wimpi.modbus.net.RTUTCPMasterConnection;

import static com.dwellsmart.constants.Constants.*;

@Service
@Slf4j
public class CacheService {

	
	private final Map<MeterType, MeterAddressMap> meterAddressMapCache = new ConcurrentHashMap<>();

	private final Map<String, RTUTCPMasterConnection> masterConnectionCache = new ConcurrentHashMap<>();
	
	// Cache to store message ID and timestamp
    private final ConcurrentHashMap<String, Instant> messageIdCache = new ConcurrentHashMap<>();
    

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
	
	/**
     * Validates if the message ID can be processed based on the 10-minute rule.
     * @param messageId The message ID to validate.
     * @return true if the message ID is valid for processing, false otherwise.
     */
    public boolean isValid(String messageId) {
    	 // If cache exceeds max size, remove old entries (older than 6 hours)
        if (messageIdCache.size() >= MAX_CACHE_SIZE) {
            removeOldestEntry();
        }
       
        Instant currentTimestamp = Instant.now();

		// Check if the messageId exists in the cache and is within the validity period
		Instant lastTimestamp = messageIdCache.get(messageId);
		if (lastTimestamp != null && Duration.between(lastTimestamp, currentTimestamp).compareTo(Constants.MESSAGE_VALIDITY_DURATION) < 0) {
			return false; // Reject if within validity period
		}

        // Update or add the message ID with the current timestamp
        messageIdCache.put(messageId, currentTimestamp);
        return true;
    }

	// Remove entries older than 6 hours
    private void removeOldestEntry() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<String, Instant>> iterator = messageIdCache.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Instant> entry = iterator.next();
            // Check if the entry is older than 6 hours
            if (now - entry.getValue().toEpochMilli() > EXPIRY_DURATION) {
                iterator.remove(); // Remove the expired entry
            }
        }
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
			addressMap.setPasswordRegisterAddress(meterType == MeterType.SUNSTAR_DS_PP ? 2672 : 0);
			addressMap.setValidatorRegisterAddress(meterType == MeterType.SUNSTAR_DS_PP ? 2929 : 0);
			addressMap.setChangeMeterIdAddress(meterType == MeterType.SUNSTAR_DS_PP ? 769 : 0);
			return addressMap;

		case ENERTRAK:
			addressMap.setMeterType(meterType);
			addressMap.setKwhRegisterAddress(16555);
			addressMap.setKwhRegisterAddressesCount(4);
			addressMap.setConnectRegisterAddress(2);
			addressMap.setConnectRegisterValue(20560);
			addressMap.setDisconnectRegisterAddress(2);
			addressMap.setDisconnectRegisterValue(0);
			addressMap.setOtherRegisterAddress(20535);
			addressMap.setOtherRegistersCount(44);
			addressMap.setDgKwhRegisterAddress(0);
			addressMap.setLoadRegisterAddress(17);
			addressMap.setOverloadattemptsRegAddress(49);
			addressMap.setOverloadattemptsdelayRegAddress(51);
			return addressMap;
			
		case SUMERU_VERDE:
		case SUMERU_VERDE_MSB:
			addressMap.setMeterType(meterType);
			addressMap.setKwhRegisterAddress(2304);
			addressMap.setKwhRegisterAddressesCount(16);
			addressMap.setConnectRegisterAddress(3664);
			addressMap.setConnectRegisterValue(61440);
			addressMap.setDisconnectRegisterAddress(3664);
			addressMap.setDisconnectRegisterValue(3840);
			addressMap.setOtherRegisterAddress(772);
			addressMap.setOtherRegistersCount(28);
			addressMap.setDgKwhRegisterAddress(0);
			addressMap.setLoadRegisterAddress(1026);
			addressMap.setEbdgRegisterAddress(3122);
			addressMap.setEbValue(0);
			addressMap.setDgValue(256);
			addressMap.setEnableMsbFirst(meterType == MeterType.SUMERU_VERDE_MSB);
			return addressMap;
			
		case LNT1P:
			addressMap.setMeterType(meterType);
			addressMap.setKwhRegisterAddress(13);
			addressMap.setKwhRegisterAddressesCount(30);
			addressMap.setConnectRegisterAddress(4);
			addressMap.setConnectRegisterValue(0);
			addressMap.setDisconnectRegisterAddress(4);
			addressMap.setDisconnectRegisterValue(1);
			addressMap.setOtherRegisterAddress(4);
			addressMap.setOtherRegistersCount(9);
			addressMap.setDgKwhRegisterAddress(41);
			addressMap.setLoadRegisterAddress(19);
			return addressMap;
			
		default:
			log.warn("There in no address map available");
			break;
		}
		return addressMap;
	}
}