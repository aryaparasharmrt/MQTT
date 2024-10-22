package com.dwellsmart.service.business;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwellsmart.client.MeterFeignClient;
import com.dwellsmart.constants.TransactionMode;
import com.dwellsmart.constants.TransactionType;
import com.dwellsmart.dto.response.dummy.ReadingResponse;
import com.dwellsmart.entity.Account;
import com.dwellsmart.entity.AccountTransaction;
import com.dwellsmart.entity.Resident;
import com.dwellsmart.entity.Site;
import com.dwellsmart.service.impl.AccountTransactionService;

import jakarta.transaction.Transactional;

@Service
public class DailyChargesService {
	
	@Autowired
	private MeterFeignClient meterFeignClient;

	@Autowired
	private AccountTransactionService accountTransactionService; // For saving transactions

//	@Autowired
//	private BillingService billingService; // To calculate daily charges

//	public void deductDailyCharges(int accountId) {
//		// Step 1: Calculate the daily charge
//		double dailyCharge = billingService.calculateDailyCharge(accountId);
//
//		// Step 2: Create a new debit transaction for the account
//		AccountTransactionRequest request = new AccountTransactionRequest();
//		request.setAccountId((long)accountId);
//		request.setTransactionType(TransactionType.DEBIT);
//		request.setTransactionMode(TransactionMode.AUTO);
//		request.setAmount(dailyCharge);
//
//		// Step 3: Save the transaction (and apply any other business rules as needed)
//		accountTransactionService.processTransaction(request);
//	}
	
	@Transactional
	public Resident processResidentDailyCharges(Resident resident) {
	    // Step 1: Fetch the site entity from the resident
	    Site site = resident.getSite();
	    if (site == null) {
	        throw new RuntimeException("Site information is missing for resident.");
	    }

	    // Step 2: Fetch the meter_ref_id from the resident to get DG and Grid readings
	    String meterRefId = resident.getMeterRefId();
	    if (meterRefId == null) {
	        throw new RuntimeException("Meter reference ID is missing for resident.");
	    }

	    // Call external service to get meter readings for DG and Grid (assumed method call)
	    ReadingResponse meterReadings = meterFeignClient.getMeterReading(meterRefId); // Implement the microservice call
	    double dgReading = meterReadings.getDgReading();
	    double gridReading = meterReadings.getGridReading();
	    
		// Step 3: Calculate the charges for DG and Grid
	    
	    double dgCharge = dgReading * site.getDgRate(); // Charge for DG consumption
	    
		double gridCharge = gridReading * site.getGridRate(); // Charge for Grid consumption
	    double maintenanceCharge = site.getMaintainenceRate(); // Maintenance charge

	    // Total charges for the resident
	    double totalCharges = dgCharge + gridCharge + maintenanceCharge;

	    // Step 4: Fetch the resident's account entity and create a new AccountTransaction
	    Account account = resident.getAccount();
	    if (account == null) {
	        throw new RuntimeException("Account information is missing for resident.");
	    }
	    
	    account.setAccountBalance(account.getAccountBalance()-totalCharges);

	    AccountTransaction accountTransaction = new AccountTransaction();
	    accountTransaction.setTransactionMode(TransactionMode.AUTO);
	    accountTransaction.setTransactionType(TransactionType.DEBIT);
	    accountTransaction.setAccount(account);
	    accountTransaction.setProject(resident.getProject()); // Assuming project ID comes from Site entity
	    accountTransaction.setTxnAmt(totalCharges);
	    accountTransaction.setTxnDte(LocalDateTime.now()); // Set the current date

	    // Step 5: Add the account transaction to the account's transaction list
	    account.getAccountTransactions().add(accountTransaction);

	    // Step 6: Persist the changes using EntityManager (assumed to be available in the class)
//	    entityManager.persist(accountTransaction);
	    
	    return resident;
	}

}
