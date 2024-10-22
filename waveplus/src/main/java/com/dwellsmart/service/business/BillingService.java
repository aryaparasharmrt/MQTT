package com.dwellsmart.service.business;

import org.springframework.stereotype.Service;

@Service
public class BillingService {

    public double calculateDailyCharge(int accountId) {
        // Logic to calculate the daily charge (can involve fetching data, applying rules, etc.)
        return 10.0; // Example static value
    }
}
