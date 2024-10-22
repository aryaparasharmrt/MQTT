package com.dwellsmart.service.impl;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dwellsmart.entity.Account;
import com.dwellsmart.entity.AccountTransaction;
import com.dwellsmart.repository.AccountRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

//	@Autowired
//	private AccountTransactionService transactionService;

	// Method to retrieve account details by accountId
	public Optional<Account> getAccountById(Long accountId) {
		return accountRepository.findById(accountId);
	}

	// Method to update the account balance
	@Transactional
	public Account updateAccountBalance(Long accountId, Double newBalance) {
		Optional<Account> optionalAccount = accountRepository.findById(accountId);
		if (optionalAccount.isPresent()) {
			Account account = optionalAccount.get();
			account.setAccountBalance(newBalance);
//            account.setLastUpdatedDate(new java.util.Date());
			return accountRepository.save(account);
		} else {
			throw new RuntimeException("Account not found with id: " + accountId);
		}
	}

	private void checkUniqueReceiptNumber(AccountTransaction txn) throws Exception {
//		if (!transactionService.isTxnUnique(txn)) {
//			throw new Exception("Invalid Transaction, Receipt Number is already in use! ");
//		}
	}

	public Account addNewAccount(Account account) {
		accountRepository.save(account);
//	  accountRepository.flush();
		return account;
	}

	// Additional methods for account management, such as credit/debit, can be added
	// here
//
//        @PersistenceContext(unitName = "waveplusEJBPU")
//        private EntityManager em;
////        private static Properties properties= null;
//        @EJB
//        CustomerService customerService;

//        @EJB
//        DwellSmartCommunicationService dwellSmartCommunicationService;
//        @EJB
//        MeterMapService meterMapService;
//        @EJB
//        ProjectService projectService;
//
//        public void persist(Object object) {
//            em.persist(object);
//        }
//
//        @Transactional
//        public AccountTransactions transactAccount(AccountTransactions transaction, Accounts account, int projectId) throws AccountTransactionException {
//            Logger.getLogger(AccountService.class.getName()).log(Level.INFO, "Method Entry : transactAccount for account{0}", account.getAccountId());
//            validateTransaction(transaction);
//
//            // Fetch account with pessimistic lock
//            Accounts lockedAccount = em.find(Accounts.class, account.getAccountId(), LockModeType.PESSIMISTIC_WRITE);
//            if (lockedAccount == null) {
//                throw new AccountTransactionException("Account not found for update");
//            }
//
//            // Copy additional values from the passed account object to the locked account
//            this.copyAdditionalValues(lockedAccount, account);
//            
//            transaction.setAccountId(lockedAccount.getAccountId());
//            transaction.setTxnDte(CommonUtils.getSysDate());
//            transaction.setTxnTime(BigInteger.valueOf(System.currentTimeMillis()));
//            transaction.setPrevBalance(lockedAccount.getAccountBalance().setScale(2, BigDecimal.ROUND_HALF_UP));
//
//            if (transaction.getTxnType().equalsIgnoreCase(DwellSmartConstants.txnTypeCredit) || transaction.getTxnType().equalsIgnoreCase(DwellSmartConstants.txnTypeRecharge)) {
//                lockedAccount.setLastRechargeAmount(transaction.getTxnAmt());
//                lockedAccount.setLastRechargeDate(CommonUtils.getSysDate());
//                lockedAccount.setAccountBalance(lockedAccount.getAccountBalance().add(transaction.getTxnAmt()));
//            } else if (transaction.getTxnType().equalsIgnoreCase(DwellSmartConstants.txnTypeDebit) || transaction.getTxnType().equalsIgnoreCase(DwellSmartConstants.txnTypeAutoDebit)) {
//                lockedAccount.setAccountBalance(lockedAccount.getAccountBalance().subtract(transaction.getTxnAmt()));
//            }
//
//            transaction.setAccountBalance(lockedAccount.getAccountBalance());
//
//            account = em.merge(lockedAccount);
//            em.persist(transaction);
//            if (transaction.getTxnType()
//                    .equalsIgnoreCase(DwellSmartConstants.txnTypeRecharge)) {
//                try {
//                    Projects project = projectService.retrieveProjectByProjectId(transaction.getProjectId());
//                    Customers customer = customerService.findCustomerByAccountId(account.getAccountId());
//                    if (project.getBalanceAlertsEnabled()) {
//                        sendTransactionSuccessSMS(customer, transaction);
//                    }
//                    sendTransactionSuccessEmail(customer, transaction, project);
//                } catch (IOException ex) {
//                    Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (Exception ex) {
//                    Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            Logger.getLogger(AccountService.class.getName()).log(Level.INFO, "Method Exit: transactAccount for account{0}", account.getAccountId());
//            return transaction;
//        }
//
//        private void copyAdditionalValues(Accounts lockedAccount, Accounts sourceAccount) {
//            // Add all additional fields that need to be copied here
//            lockedAccount.setLastUpdatedDate(sourceAccount.getLastUpdatedDate());
//            // ... copy other fields as needed
//        }
//
//        public void validateTransaction(AccountTransactions transaction) throws AccountTransactionException {
//
//            if (!transaction.getTxnType()
//                    .equalsIgnoreCase(DwellSmartConstants.txnTypeAutoDebit) && !CommonUtils.isNull(transaction.getReceiptNo())) {
////                checkUniqueReceiptNumber(transaction);
//                checkMandatoryDetails(transaction);
//            }
//        }
//
//
//        private void checkMandatoryDetails(AccountTransactions transaction) throws AccountTransactionException {
//            if (transaction.getTxnAmt() == null || transaction.getTxnAmt().equals(BigDecimal.ZERO)) {
//                throw new AccountTransactionException("Invalid Transaction, Transaction amount should not be ZERO!");
//            }
//            if (transaction.getTxnType().equalsIgnoreCase(DwellSmartConstants.txnTypeRecharge)) {
//
//                if ((transaction.getTxnMode().equalsIgnoreCase(DwellSmartConstants.txnModeCheque) && null == transaction.getChequeNumber())) {
//                    throw new AccountTransactionException("Invalid Transaction, Incorrect Cheque Details!");
//                }
//                if ((transaction.getTxnMode().equalsIgnoreCase(DwellSmartConstants.txnModeNEFT) && null == transaction.getChequeNumber())) {
//                    throw new AccountTransactionException("Invalid Transaction, Incorrect NEFT Details!");
//                }
//
//            }
//        }
//
//        private void sendTransactionSuccessEmail(Customers customer,
//                AccountTransactions transactions, Projects project) throws IOException {
//            Logger.getLogger(AccountService.class.getName()).log(Level.INFO, "Entering Email Routine");
//            if (customer.getEmailPreference().toString().equalsIgnoreCase(DwellSmartConstants.customerEmailPreferenceY)) {
//
//                SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
//                StringBuilder message = new StringBuilder(200);
//                message.append("<html><body>");
//                message.append("Dear ").append(customer.getCustomerName());
//                message.append("<p>");
//                message.append(project.getTxnMailText());
//                message.append("</p>");
//                message.append("<br/>");
//                message.append("<br/>");
//                message.append("<div align=\"center\" style=\"vertical-align:bottom\">"
//                        + "<table style='border:2px solid black'>");
//
//                message.append("<tr bgcolor=\"#a0cc3a\">");
//                message.append("<td>");
//                message.append("Txn Id");
//                message.append("</td>");
//
//                message.append("<td>");
//                message.append(transactions.getTxnId());
//                message.append("</td>");
//                message.append("<tr>");
//
//                message.append("<tr bgcolor=\"#FFF\">");
//                message.append("<td>");
//                message.append("Transaction Date");
//                message.append("</td>");
//
//                message.append("<td>");
//                message.append(fmt.format(transactions.getTxnDte()));
//                message.append("</td>");
//                message.append("<tr>");
//
//                message.append("<tr bgcolor=\"#a0cc3a\">");
//                message.append("<td>");
//                message.append("Gateway TxnId");
//                message.append("</td>");
//
//                message.append("<td>");
//                message.append(transactions.getGatewaytxnid());
//                message.append("</td>");
//                message.append("<tr>");
//
//                message.append("<tr bgcolor=\"#FFF\">");
//                message.append("<td>");
//                message.append("Bank Ref Number");
//                message.append("</td>");
//
//                message.append("<td>");
//                message.append(transactions.getBankRefNum());
//                message.append("</td>");
//                message.append("<tr>");
//
//                message.append("<tr bgcolor=\"#a0cc3a\">");
//                message.append("<td>");
//                message.append("Flat Number");
//                message.append("</td>");
//
//                message.append("<td>");
//                message.append(customer.getFlatNo());
//                message.append("</td>");
//                message.append("<tr>");
//
//                message.append("<tr bgcolor=\"#FFF\">");
//                message.append("<td>");
//                message.append("Owner Name");
//                message.append("</td>");
//
//                message.append("<td>");
//                message.append(customer.getCustomerName());
//                message.append("</td>");
//                message.append("<tr>");
//
//                message.append("<tr bgcolor=\"#a0cc3a\">");
//                message.append("<td>");
//                message.append("Transaction Amount");
//                message.append("</td>");
//
//                message.append("<td>");
//                message.append(transactions.getTxnAmt());
//                message.append("</td>");
//                message.append("<tr>");
//
//                message.append("<tr bgcolor=\"#FFF\">");
//                message.append("<td>");
//                message.append("Account Balance");
//                message.append("</td>");
//
//                message.append("<td>");
//                message.append(transactions.getAccountBalance());
//                message.append("</td>");
//                message.append("<tr>");
//
//                message.append("<tr bgcolor=\"#a0cc3a\">");
//                message.append("<td>");
//                message.append("Payment Mode");
//                message.append("</td>");
//
//                message.append("<td>");
//                message.append(transactions.getTxnMode());
//                message.append("</td>");
//                message.append("<tr>");
//                if ((transactions.getTxnMode().equalsIgnoreCase(DwellSmartConstants.txnModeCheque)
//                        || transactions.getTxnMode().equalsIgnoreCase(DwellSmartConstants.txnModeNEFT)) && null != transactions.getChequeNumber()) {
//
//                    message.append("<tr bgcolor=\"#FFF\">");
//                    message.append("<td>");
//                    message.append("Cheque Number/NEFT Transaction No.");
//                    message.append("</td>");
//
//                    message.append("<td>");
//                    message.append(transactions.getChequeNumber());
//                    message.append("</td>");
//                    message.append("<tr>");
//
//                    message.append("<tr bgcolor=\"#a0cc3a\">");
//                    message.append("<td>");
//                    message.append("Cheque Date");
//                    message.append("</td>");
//
//                    message.append("<td>");
//
//                    message.append(fmt.format(transactions.getChequeDte()));
//                    message.append("</td>");
//                    message.append("<tr>");
//
//                    message.append("<tr bgcolor=\"#FFF\">");
//                    message.append("<td>");
//                    message.append("Bank Name");
//                    message.append("</td>");
//
//                    message.append("<td>");
//                    message.append(transactions.getBankName());
//                    message.append("</td>");
//                    message.append("<tr>");
//
//                    message.append("<tr bgcolor=\"#a0cc3a\">");
//                    message.append("<td>");
//                    message.append("Bank A/C Number");
//                    message.append("</td>");
//
//                    message.append("<td>");
//                    message.append(transactions.getBankAccountNumber());
//                    message.append("</td>");
//                    message.append("<tr>");
//                }
//
//                message.append("</div></table>");
//
//                message.append("<br/>");
//                message.append("<br/>");
//                message.append("<p>Thanks,");
//                message.append("<br/>");
//                message.append(project.getAccountsSignatureLine1());
//                message.append("<br/>");
//                message.append("<br/>");
//                message.append(project.getAccountsSignatureLine2());
//                message.append("</p></body></html>");
//
//                dwellSmartCommunicationService.sendEmail(customer.getEmailId(), project.getTxnMailSubject(), message.toString(),
//                        customer.getFlatNo(), customer.getProjectId(), true);
//            }
//            Logger.getLogger(AccountService.class.getName()).log(Level.INFO, "Exiting Email Routine");
//        }
//
//        private void sendTransactionSuccessSMS(Customers customer,
//                AccountTransactions transaction) throws IOException {
//            Logger.getLogger(AccountService.class.getName()).log(Level.INFO, "Entering SMS Routine");
//            if (null != customer.getPhoneNumber() && !"".equals(customer.getPhoneNumber().toString())) {
//                StringBuilder message = new StringBuilder(200);
//                message.append("Dear ");
//                message.append(customer.getCustomerName());
//                message.append(",\n Thank you for the payment of Rs ");
//                message.append(transaction.getTxnAmt());
//                message.append(" for your flat ");
//                message.append(customer.getFlatNo());
//                message.append("on DwellSMART platform. New balance is Rs");
//                message.append(customer.getAccountId().getAccountBalance());
//                message.append(".");
//
//                dwellSmartCommunicationService.sendMessage(customer.getPhoneNumber().toString(), message.toString(),
//                        customer.getFlatNo(), customer.getProjectId(), true, (String) CommonUtils.getDwellSmartProperties().get("recharge_sms_template_id"));
//            }
//            Logger.getLogger(AccountService.class.getName()).log(Level.INFO, "Exiting SMS Routine");
//        }
//

//    }

}
