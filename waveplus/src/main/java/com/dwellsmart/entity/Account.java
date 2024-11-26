package com.dwellsmart.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long accountId;

	@Column(nullable = false)
	@Builder.Default
	private Double accountBalance = 0.0;

//	    @Column(name = "last_recharge_date")
//	    @Temporal(TemporalType.DATE)
//	    private Date lastRechargeDate;

//	    @Column(name = "last_recharge_amount")
//	    private BigDecimal lastRechargeAmount;

//	    @Column(name = "interest_amount")
//	    private BigDecimal interestAmount;

//	    @Column(name = "last_invoice_id")
//	    private BigDecimal lastInvoiceId;

	@Column(nullable = false)
	@Builder.Default
	private Double lastDgReading = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double lastGridReading = 0.0;

	@Column(nullable = false)
    private LocalDateTime lastReadingDate;

//	    @Column(name = "date_last_synched_manually")
//	    @Temporal(TemporalType.DATE)
//	    private Date dateLastSynchedManually;

//	    @Column(name = "last_invoice_dg_reading")
//	    private BigDecimal lastInvoiceDgReading;

//	    @Column(name = "last_invoice_grid_reading")
//	    private BigDecimal lastInvoiceGridReading;

//	    @Column(name = "connection_kv")
//	    private BigDecimal connectionKv;

//	    @Column(name = "last_account_balance", nullable = false, columnDefinition = "numeric default 0.0")
//	    @Column(name = "last_account_balance")
//	    private BigDecimal lastAccountBalance;

//	    @Column(name = "last_inv_date")
//	    @Temporal(TemporalType.DATE)
//	    private Date lastInvDate;

	@CreationTimestamp
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resident_id", nullable = false)
	private Resident resident;

	@OneToMany(mappedBy = "account", fetch = FetchType.LAZY) //, cascade = CascadeType.ALL - remove casecade operation think later
	private List<AccountTransaction> accountTransactions;

}
