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

		// Fetch existing device by deviceId
		Optional<Device> existingDevice = deviceRepository.findByDeviceId(deviceDTO.getDeviceId());

		if (!deviceDTO.isRevoked()) { // Login Request

			// Find the user by username
			if (deviceDTO.getUsername() != null) {
				User user = userRepository.findByUsername(deviceDTO.getUsername())
						.orElseThrow(() -> new UsernameNotFoundException("User not found"));

				Set<Device> userDevices = user.getDevices();

				// Handle New Device Logic (if not an existing device)
				if (existingDevice.isEmpty()) {

					// Remove revoked or oldest device if user has more than 5 devices
					if (userDevices.size() >= 5) {
						Optional<Device> revokedDevice = userDevices.stream().filter(Device::isRevoked).findFirst();

						if (revokedDevice.isPresent()) {
							// Remove the revoked device
							userDevices.remove(revokedDevice.get());
						} else {
							// Remove the oldest device if no revoked device is found
							Device oldestDevice = userDevices.stream().min(Comparator.comparing(Device::getLoginDate))
									.orElseThrow(() -> new RuntimeException("No devices found for the user"));

							userDevices.remove(oldestDevice);
						}
					}

					// Create and save new device
					Device newDevice = this.toDevice(deviceDTO, user);

					// Save the new device
					deviceRepository.save(newDevice);

				} else {
					// Update existing device (Login request for existing device)
					Device device = existingDevice.get();
					device.setRefreshToken(deviceDTO.getRefreshToken());
					device.setTokenCreatedAt(deviceDTO.getTokenCreatedAt());
					device.setLoginDate(deviceDTO.getLoginDate());

					// Save the updated device
					deviceRepository.save(device);
				}

			} else {
				throw new ApplicationException("User is mandatory for mobile devices");
			}

		} else { // Logout Request

			// Handle logout by revoking the existing device
			if (existingDevice.isPresent()) {
				Device device = existingDevice.get();
				device.setRevoked(deviceDTO.isRevoked()); // Mark the device as revoked
				deviceRepository.save(device);
			} else {
				throw new ApplicationException("Device not found for revoking");
			}
		}
	}

	@Override
	public Optional<DeviceDTO> getDeviceByDeviceIdAndRefToken(String deviceId, String refreshToken) {

		// Fetch the device by deviceId
		Optional<Device> optionalDevice = deviceRepository.findByDeviceId(deviceId);

		// Check if the device is present
		if (optionalDevice.isPresent()) {
			Device device = optionalDevice.get();

			DeviceDTO deviceDto = this.toDeviceDTO(device);

			// Check refresh token validity and device is not revoked
			if (deviceDto.getRefreshToken() != null && deviceDto.getRefreshToken().equals(refreshToken)
					&& !deviceDto.isRevoked()) {

				// Check if the token has expired (7 days validity)
				LocalDateTime tokenCreatedAt = deviceDto.getTokenCreatedAt();

				if (tokenCreatedAt != null) {
					LocalDateTime tokenExpiryDate = tokenCreatedAt.plusDays(7);
					LocalDateTime now = LocalDateTime.now();

					// If token has not expired, return the DTO
					if (now.isBefore(tokenExpiryDate)) {
						return Optional.of(deviceDto);
					}
				}
			}
		}

		// If any condition fails, return Optional.empty()
		return Optional.empty();
	}

	@Override
	public Optional<DeviceDTO> getDeviceByDeviceId(String deviceId) {

		// Fetch the device by deviceId
		Optional<Device> byDeviceId = deviceRepository.findByDeviceId(deviceId);

		DeviceDTO deviceDto = null;

		// Check if the device is present
		if (byDeviceId.isPresent()) {
			Device device = byDeviceId.get();

			deviceDto = this.toDeviceDTO(device);
			// Check refresh token validity
			if (device.getRefreshToken() != null) {
				LocalDateTime tokenCreatedAt = device.getTokenCreatedAt();

				// Check if the token has expired (7 days validity)
				if (tokenCreatedAt != null) {
					LocalDateTime tokenExpiryDate = tokenCreatedAt.plusDays(7);
					LocalDateTime now = LocalDateTime.now();

//					convertValue.setExpired(now.isAfter(tokenExpiryDate));
				} else {
//					convertValue.setExpired(true);
				}
			} else {
				// No refresh token present, mark the device as expired
//				convertValue.setExpired(true);
			}
		}

		return Optional.ofNullable(deviceDto);
	}

	@Override
	public List<Device> getDevicesByUser(User user) {
		return deviceRepository.findByUser(user);
	}

	// Convert Device to DeviceDTO
	public DeviceDTO toDeviceDTO(Device device) {

		DeviceDTO deviceDTO = objectMapper.convertValue(device, DeviceDTO.class);
		if (device.getUser() != null) {
			deviceDTO.setUsername(device.getUser().getUsername());
		}
		return deviceDTO;
	}

	// Convert DeviceDTO to Device
	public Device toDevice(DeviceDTO deviceDTO, User user) {

		// Use ObjectMapper to convert DeviceDTO to Device
		Device device = objectMapper.convertValue(deviceDTO, Device.class);

		if (user != null) {
			device.setUser(user);
		}

		return device;
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

}
