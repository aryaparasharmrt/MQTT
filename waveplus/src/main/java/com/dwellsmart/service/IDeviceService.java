package com.dwellsmart.service;

import java.util.List;
import java.util.Optional;

import com.dwellsmart.dto.DeviceDTO;
import com.dwellsmart.entity.Device;
import com.dwellsmart.entity.User;

public interface IDeviceService {
	
	void manageDevice(DeviceDTO deviceDTO);
	
	// Create a new device
//	DeviceDTO loggedInDevice(DeviceDTO device);

    // Get a device by its deviceId (unique)
    Optional<DeviceDTO> getDeviceByDeviceId(String deviceId);

    // Get all devices associated with a user
    List<Device> getDevicesByUser(User user);

    // Update device information (by deviceId)
//    Device updateDevice(String deviceId, Device updatedDevice);

    // Mark device as revoked or expired
    void revokeDevice(String deviceId);

    void expireDevice(String deviceId);

    // Delete a device (by deviceId)
    void deleteDevice(String deviceId);

//	void updateDevice(DeviceDTO existingDevice);

//	boolean isTokenRequestValid(String refreshToken, String deviceId);

	Optional<DeviceDTO> getDeviceByDeviceIdAndRefToken(String deviceId, String refreshToken);


}
