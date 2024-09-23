package com.dwellsmart.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projects")
@Data
@Builder
@NoArgsConstructor // Required for JPA
@AllArgsConstructor
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false) // For not null value
	@Column(name = "project_id")
	private Integer projectId;

	@Basic(optional = false)
	@Size(min = 1, max = 750)
	@Column(name = "project_name")
	private String projectName;

	@Column(name = "negative_threshold")
	@Basic(optional = false)
	@Builder.Default
	private Integer negativeThrashold = 0;

	@Column(name = "active_status")
	@Builder.Default
	private Boolean activeStatus = false;

	@Column(name = "automatic_billing_enabled")
	@Builder.Default
	private Boolean automaticBillingEnabled = false;

	@Column(name = "billing_date")
	private LocalDate billingDate;

	@Column(name = "balance_alerts_enabled")
	@Builder.Default
	private Boolean balanceAlertsEnabled = false;

	@Column(name = "automatic_reading_enabled")
	@Builder.Default
	private Boolean automaticReadingEnabled = false;

	@Column(name = "logo_image_path")
	private String logoImagePath; // Store the file path or URL

	@Column(name = "phone_number")
	private Long phoneNumber;

	@Size(max = 60)
	@Column(name = "email_id")
	private String emailId;

	@Column(name = "vending_charge")
	@Builder.Default
	@Basic(optional = false)
	private Double vendingCharge = 50.0;

	@Column(name = "online_payment_active")
	@Builder.Default
	private Boolean onlinePaymentActive = false;

	@Column(name = "low_balance_alert_threshold")
	@Builder.Default
	private Integer lowBalanceAlertThreshold = 500;

	@Column(name = "convenience_fee")
	private Integer convenienceFee;

	@Column(name = "prepaid_metering_enabled")
	@Builder.Default
	private Boolean prepaidMeteringEnabled = true;

	@Column(name = "is_child_merchant")
	@Builder.Default
	private Boolean isChildMerchant = true;

	@Column(name = "failure_report_enabled")
	@Builder.Default
	private Boolean failureReportEnabled = false;

	@Size(max = 80)
	@Column(name = "project_engineer_email")
	private String projectEngineerEmail;

	@Column(name = "min_recharge_amt")
	@Builder.Default
	private Integer minRechargeAmt = 500;

	@Column(name = "has_sumeru_meters")
	@Builder.Default
	private Boolean hasSumeruMeters = false;

	@Basic(optional = false)
	@Column(name = "livedata_frozentime")
	@Builder.Default
	private Integer livedataFrozentime = 900000; // up to 15 min.

	@Basic(optional = false)
	@Column(name = "is_allowed_to_setload")
	@Builder.Default
	private Boolean isAllowedToSetload = true;

	@Column(name = "is_dg_per_hr_active")
	@Builder.Default
	private Boolean isDgPerHrActive = false;

	@Column(name = "pg_key")
	private String pgKey;

	@Column(name = "pg_salt")
	private String pgSalt;

	@Column(name = "pg_merchantid")
	private String pgMerchantid;

	@Column(name = "pg_provider")
	private String pgProvider;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	// One-to-many relationship with Site
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Site> sites;

	// One-to-Many relationship with User_Roles
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Role> roles;

}
