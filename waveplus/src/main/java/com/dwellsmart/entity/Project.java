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
	private Integer projectId;

	@Basic(optional = false)
	@Size(min = 1, max = 750)
	private String projectName;

	@Basic(optional = false)
	@Builder.Default
	private Integer negativeThreshold = 0;

	@Builder.Default
	private Boolean activeStatus = false;

	@Builder.Default
	private Boolean automaticBillingEnabled = false;

	private LocalDate billingDate;

	@Builder.Default
	private Boolean balanceAlertsEnabled = false;

	@Builder.Default
	private Boolean automaticReadingEnabled = false;

	private String logoImagePath; // Store the file path or URL

	private Long phoneNumber;

	@Size(max = 60)
	private String emailId;

	@Builder.Default
	@Basic(optional = false)
	private Double vendingCharge = 50.0;

	@Builder.Default
	private Boolean onlinePaymentActive = false;

	@Builder.Default
	private Integer lowBalanceAlertThreshold = 500;

	private Integer convenienceFee;

	@Builder.Default
	private Boolean prepaidMeteringEnabled = true;

	@Builder.Default
	private Boolean isChildMerchant = true;

	@Builder.Default
	private Boolean failureReportEnabled = false;

	@Size(max = 80)
	private String projectEngineerEmail;

	@Builder.Default
	private Integer minRechargeAmt = 500;

	@Builder.Default
	private Boolean hasSumeruMeters = false;

	@Basic(optional = false)
	@Builder.Default
	private Integer livedataFrozentime = 900000; // up to 15 min.

	@Basic(optional = false)
	@Builder.Default
	private Boolean isAllowedToSetload = true;

	@Builder.Default
	private Boolean isDgPerHrActive = false;

	private String pgKey;

	private String pgSalt;

	private String pgMerchantid;

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
