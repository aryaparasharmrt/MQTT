package com.dwellsmart.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.dwellsmart.constants.TransactionMode;
import com.dwellsmart.constants.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
//@Table(name = "account_transactions", indexes = {
//        @Index(name = "account_transactions_account_id_idx", columnList = "account_id"),
//        @Index(name = "account_transactions_txn_dte_idx", columnList = "txn_dte")
//})
//@Table(name = "account_transactions", uniqueConstraints = @UniqueConstraint(columnNames = "gatewaytxnid"))
@Table(name = "account_transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "txn_id", nullable = false)
    private Long txnId;

    @CreationTimestamp
	@Column(updatable = false,nullable = false)
	private LocalDateTime txnDte;

    @Column(nullable = false)
    private Double txnAmt;

    @Enumerated(EnumType.STRING) 
    @Column(nullable = false)
	private TransactionType transactionType;

    @Enumerated(EnumType.STRING) 
    @Column(nullable = false)
	private TransactionMode transactionMode;

//    @Column(name = "cheque_number")
//    private BigDecimal chequeNumber;
//
//    @Column(name = "cheque_dte")
//    private LocalDate chequeDte;
//
//    @Column(name = "cheque_status", length = 20)
//    private String chequeStatus;
//
//    @Column(name = "bank_name", length = 80)
//    private String bankName;
//
//    @Column(name = "bank_account_number")
//    private BigDecimal bankAccountNumber;
//
//    @Column(name = "account_holder_name", length = 150)
//    private String accountHolderName;
//
//    @Column(name = "account_holder_contact_no")
//    private BigDecimal accountHolderContactNo;
//
//    @Column(name = "txn_notes", length = 300)
//    private String txnNotes;
//
//    @Column(name = "account_balance")
//    private BigDecimal accountBalance;
//
//    @Column(name = "prev_balance")
//    private BigDecimal prevBalance;
//
//    @Column(name = "receipt_no", length = 50)
//    private String receiptNo;
//
//    @Column(name = "debit_reason", length = 25)
//    private String debitReason;
//
//    @Column(name = "mihpayid", length = 100)
//    private String mihpayid;
//
//    @Column(name = "payment_mode", length = 30)
//    private String paymentMode;
//
//    @Column(name = "unmappedstatus", length = 30)
//    private String unmappedstatus;
//
//    @Column(name = "gatewaytxnid", length = 30, unique = true)
//    private String gatewayTxnId;
//
//    @Column(name = "pg_type", length = 30)
//    private String pgType;
//
//    @Column(name = "encryptedpaymentid", length = 150)
//    private String encryptedPaymentId;
//
//    @Column(name = "bank_ref_num", length = 100)
//    private String bankRefNum;
//
//    @Column(name = "bankcode", length = 30)
//    private String bankCode;
//
//    @Column(name = "name_on_card", length = 50)
//    private String nameOnCard;
//
//    @Column(name = "cardnum", length = 20)
//    private String cardNum;
//
//    @Column(name = "amount_split", length = 30)
//    private String amountSplit;
//
//    @Column(name = "payumoneyid", length = 1000)
//    private String payuMoneyId;
//
//    @Column(name = "txn_time")
//    private Long txnTime;
    
    @ManyToOne(fetch = FetchType.LAZY)  //cascade = CascadeType.ALL
	@Basic(optional = false)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;
    
    @ManyToOne(fetch = FetchType.LAZY) //cascade = CascadeType.ALL
	@Basic(optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    
    @ManyToOne(fetch = FetchType.LAZY)  //cascade = CascadeType.ALL
    @JoinColumn(name = "user_id")  // For Admin
	private User user;
    
}
