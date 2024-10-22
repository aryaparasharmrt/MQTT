package com.dwellsmart.client;

import org.springframework.web.bind.annotation.GetMapping;

import com.dwellsmart.dto.response.dummy.ReadingResponse;

//@FeignClient(name="Meter-Service")
public interface MeterFeignClient {

	@GetMapping("/reading")
	public ReadingResponse getMeterReading(String meterRefId);

}