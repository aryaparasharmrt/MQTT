package com.dwellsmart.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dwellsmart.constants.Endpoints;
import com.dwellsmart.dto.DeviceDTO;
import com.dwellsmart.entity.Device;
import com.dwellsmart.entity.User;
import com.dwellsmart.repository.DeviceRepository;
import com.dwellsmart.service.impl.DeviceService;
import com.dwellsmart.service.impl.UserService;

import jakarta.persistence.EntityManager;

@RestController
@RequestMapping(Endpoints.BASE+"/demo")
public class DemoController {

	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DeviceRepository deviceRepository;
	
	@Autowired
	private EntityManager entityManager;
	
  @PostMapping
  public ResponseEntity<String> sayHello(@RequestBody String deviceId,@RequestParam String os,@RequestParam String user) {
	  
	  System.out.println("New User Request :: From Demo Controller");
	  User findUser = userService.findByUserName(user);
	  
//	  Device deviceByDeviceId = deviceRepository.findByDeviceId(deviceId).get();	  
	  // Entity ko detach karne ka code
//	    entityManager.detach(deviceByDeviceId);
	    entityManager.detach(findUser);
	    
//	    deviceByDeviceId.setOS(os);
	  
	    findUser.setFullname(user+os);
	  
//	  Set<Device> devices = user.getDevices();
//	  Device device = Device.builder().deviceId("first1").loginDate(LocalDateTime.now()).user(user).deviceType("web").tokenCreatedAt(LocalDateTime.now()).refreshToken("fisrt1").build();
//	  
////	  devices.add(device);
//	  devices.remove(device);
	  
	  
	  System.out.println("Save User Aagin...");
	  
	  userService.saveUser(findUser);
//	  deviceRepository.save(deviceByDeviceId);
	  
	  
    return ResponseEntity.ok("Hello from secured endpoint");
  }

}
