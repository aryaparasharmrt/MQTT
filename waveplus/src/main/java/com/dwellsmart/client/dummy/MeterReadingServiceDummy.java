package com.dwellsmart.client.dummy;

import org.springframework.stereotype.Component;

import com.dwellsmart.client.MeterFeignClient;
import com.dwellsmart.dto.response.dummy.ReadingResponse;

@Component
public class MeterReadingServiceDummy implements MeterFeignClient {

	@Override
	public ReadingResponse getMeterReading(String meterRefId) {

		ReadingResponse response = new ReadingResponse();
		response.setDgReading(10.0);
		response.setGridReading(20.0);
		response.setMeterRefId(meterRefId);
		return response;

	}

}
