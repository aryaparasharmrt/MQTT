package com.dwellsmart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dwellsmart.entity.Device;
import com.dwellsmart.entity.User;

public interface DeviceRepository extends JpaRepository<Device, Long> {

	List<Device> findByUser(User user);

	Optional<Device> findByDeviceId(String deviceId);

	void deleteByDeviceId(String deviceId);

}
