package com.dwellsmart.service.impl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dwellsmart.dto.DeviceDTO;
import com.dwellsmart.entity.Device;
import com.dwellsmart.entity.User;
import com.dwellsmart.exception.ApplicationException;
import com.dwellsmart.exception.ResourceNotFoundException;
import com.dwellsmart.repository.DeviceRepository;
import com.dwellsmart.repository.UserRepository;
import com.dwellsmart.service.IDeviceService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceService implements IDeviceService {

	private final DeviceRepository deviceRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;
	
	
	
	@Transactional
	public void manageDevice(DeviceDTO deviceDTO) {
		
		 // Check if this is a new device or update an existing one
	    Optional<Device> existingDevice = deviceRepository.findByDeviceId(deviceDTO.getDeviceId());
	    
	    if(!deviceDTO.isRevoked()) { //login Request
		
		
	    // Find the user by username
		if (deviceDTO.getUsername() != null) {
			User user = userRepository.findByUsername(deviceDTO.getUsername())
					.orElseThrow(() -> new UsernameNotFoundException("User not found"));
			
			Set<Device> userDevices = user.getDevices();

	    // Check if the user has more than 5 devices
	    if (userDevices.size() >= 5  && existingDevice.isEmpty()) {
	        // Remove the oldest device (based on loginDate or id)
	        Device oldestDevice = userDevices.stream()
	                .min(Comparator.comparing(Device::getLoginDate)) // Or use ID if no loginDate
	                .orElseThrow(() -> new RuntimeException("No devices found for the user"));
	        
	        // Remove the oldest device
	        userDevices.remove(oldestDevice);
	    }

	    Device device;
	    
	    if (existingDevice.isPresent()) {
	        // Update existing device
	        device = existingDevice.get();
	    } else {
	        // Create new device
	        device = new Device();
	        device.setUser(user);
	    }

	    // Update device fields
	    device.setDeviceId(deviceDTO.getDeviceId());
	    device.setDeviceType(deviceDTO.getDeviceType());
	    device.setRevoked(deviceDTO.isRevoked());
	    device.setTokenCreatedAt(LocalDateTime.now());
	    device.setRefreshToken(deviceDTO.getRefreshToken());
	    
	    // Set loginDate to the current date and time
	    device.setLoginDate(LocalDateTime.now());

	    // If the device is expired, set the expiration time
//	    if (deviceDTO.isExpired()) {
//	        device.setExpired(true);
//	        device.setLoginDate(LocalDateTime.now()); // Update to current time
//	    }

	    // Save the updated or new device
	    deviceRepository.save(device);
	    
		} else {
			throw new ApplicationException("User is mandatory for mobile devices");
		}
		
	    }
	}


	@Override
	public DeviceDTO loggedInDevice(DeviceDTO deviceDTO) {

        Device device = objectMapper.convertValue(deviceDTO, Device.class);
		

//        if (Constants.CLIENT_AND.equalsIgnoreCase(deviceDTO.getDeviceType())||Constants.CLIENT_IOS.equalsIgnoreCase(deviceDTO.getDeviceType())) {
		if (deviceDTO.getUsername() != null) {
			User user = userRepository.findByUsername(deviceDTO.getUsername())
					.orElseThrow(() -> new ResourceNotFoundException("User not found"));
			device.setUser(user);
		} else {
			throw new IllegalArgumentException("User is mandatory for mobile devices");
		}
//        }

		Device savedDevice = deviceRepository.save(device);

        return objectMapper.convertValue(savedDevice, DeviceDTO.class);

	}

	@Override
	public Optional<DeviceDTO> getDeviceByDeviceId(String deviceId) {
		
				Optional<Device> byDeviceId = deviceRepository.findByDeviceId(deviceId);
				
				DeviceDTO convertValue = objectMapper.convertValue(byDeviceId,DeviceDTO.class);
				
				System.out.println(convertValue);
				
				return Optional.ofNullable(convertValue);
	}

	@Override
	public List<Device> getDevicesByUser(User user) {
		return deviceRepository.findByUser(user);
	}

	@Override
	public Device updateDevice(String deviceId, Device updatedDevice) {
		return deviceRepository.findByDeviceId(deviceId).map(device -> {
			device.setDeviceType(updatedDevice.getDeviceType());
			device.setOS(updatedDevice.getOS());
			device.setVersion(updatedDevice.getVersion());
			device.setRevoked(updatedDevice.isRevoked());
//			device.setExpired(updatedDevice.isExpired());
			return deviceRepository.save(device);
		}).orElseThrow(() -> new ApplicationException("Device not found"));
	}

	@Override
	public void revokeDevice(String deviceId) {
		deviceRepository.findByDeviceId(deviceId).ifPresent(device -> {
			device.setRevoked(true);
			deviceRepository.save(device);
		});
	}

	@Override
	public void expireDevice(String deviceId) {
		deviceRepository.findByDeviceId(deviceId).ifPresent(device -> {
//			device.setExpired(true);
			deviceRepository.save(device);
		});
	}

	@Override
	public void deleteDevice(String deviceId) {
		deviceRepository.deleteByDeviceId(deviceId);
	}

	@Override
	public void updateDevice(DeviceDTO existingDevice) {
		
		// TODO Auto-generated method stub
		
	}

}
