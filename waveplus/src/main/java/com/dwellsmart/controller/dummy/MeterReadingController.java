package com.dwellsmart.controller.dummy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dwellsmart.constants.Endpoints;
import com.dwellsmart.dto.response.dummy.ReadingResponse;
import com.dwellsmart.scheduler.DailyChargesScheduler;

import jakarta.validation.Valid;

@RestController
@RequestMapping(Endpoints.BASE)
public class MeterReadingController {
	
	
	  // Endpoint to simulate a transaction that interacts with another microservice
    @PostMapping("/process")
    public ResponseEntity<String> processTransaction(@Valid @RequestBody ReadingResponse readingResponse) {
        // Log incoming request data
        System.out.println("Received meter reading data: " + readingResponse);

        // Simulate interaction with a second microservice
        // For now, we will use dummy data instead of actually calling another service.
        String dummyResponse = callDummyService(readingResponse.getMeterRefId());

        // Log or return the dummy response
        return ResponseEntity.ok("Transaction processed successfully with dummy response: " + dummyResponse);
    }

    // Dummy method to simulate the interaction with the second microservice
    private String callDummyService(String meterId) {
        // Simulate different dummy responses based on the meter ID
        if (meterId != null && meterId.equalsIgnoreCase("12345")) {
            // Dummy response for a specific meterId
            return "{ \"dg_reading\": 100.5, \"grid_reading\": 200.75, \"status\": \"SUCCESS\" }";
        } else {
            // General dummy response
            return "{ \"dg_reading\": 50.0, \"grid_reading\": 150.0, \"status\": \"SUCCESS\" }";
        }
    }
    
    @Autowired
    private DailyChargesScheduler chargesScheduler;
    
    @GetMapping("/run")
    public ResponseEntity<String> runDailyChargesBatch() {
    	chargesScheduler.runDailyCharges();
        return ResponseEntity.ok("Batch Run successfully");
    }

}
