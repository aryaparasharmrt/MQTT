package com.dwellsmart.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "projects")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Integer projectId;

	@Basic(optional = false)
	@Column(nullable = false, length = 750)
	private String projectName;

	private LocalDate billingDate;
	
	@Column(nullable = false)
	@Builder.Default
	private Integer negativeThreshold = 0;

	@Column(nullable = false)
	@Builder.Default
	private Boolean activeStatus = false;

	@Column(nullable = false)
	@Builder.Default
	private Boolean automaticBillingEnabled = false;

	@Column(nullable = false)
	@Builder.Default
	private Boolean balanceAlertsEnabled = false;

	@Column(nullable = false)
	@Builder.Default
	private Boolean automaticReadingEnabled = false;

	private String logoImagePath; // Store the file path or URL

	@Basic(optional = false)
	@Column(nullable = false)
	private Long phoneNumber;

	@Basic(optional = false)
	@Column(nullable = false, length = 100)
	private String emailId;

	@Column(nullable = false)
	@Builder.Default
	private Double vendingCharge = 50.0;

	@Column(nullable = false)
	@Builder.Default
	private Boolean onlinePaymentActive = false;

	@Column(nullable = false)
	@Builder.Default
	private Integer lowBalanceAlertThreshold = 500;

	@Column(nullable = false)
	@Builder.Default
	private Integer convenienceFee= 0;

	@Column(nullable = false)
	@Builder.Default
	private Boolean prepaidMeteringEnabled = true;

	@Column(nullable = false)
	@Builder.Default
	private Boolean isChildMerchant = true;

	@Column(nullable = false)
	@Builder.Default
	private Boolean failureReportEnabled = false;

	@Column(length = 100)
	private String projectEngineerEmail;

	@Column(nullable = false)
	@Builder.Default
	private Integer minRechargeAmt = 500;

	@Column(nullable = false)
	@Builder.Default
	private Boolean hasSumeruMeters = false;

	@Column(nullable = false)
	@Builder.Default
	private Integer livedataFrozentime = 900000; // up to 15 min.

	@Column(nullable = false)
	@Builder.Default
	private Boolean isAllowedToSetload = true;

	@Column(nullable = false)
	@Builder.Default
	private Boolean isDgPerHrActive = false;

	private String pgKey;

	private String pgSalt;

	private String pgMerchantid;

	private String pgProvider;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	// One-to-many relationship with Site
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)  //orphanRemoval = true
	@Builder.Default
    private List<Site> sites = new ArrayList<>();

	// One-to-Many relationship with User_Roles
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Role> roles;
	
	// One-to-Many relationship with Resident
    @OneToMany(mappedBy = "project",fetch = FetchType.LAZY)
    private List<Resident> residents;
    
    
    @OneToMany(mappedBy = "project",fetch = FetchType.LAZY)
    private List<AccountTransactions> accountTransactions;
    
    

	 // Add site to the project
    public void addSite(Site site) {
        sites.add(site);           // Add site to the list
        site.setProject(this);      // Set the project reference in the site
    }

    // Remove site from the project
    public void removeSite(Site site) {
        sites.remove(site);         // Remove site from the list
        site.setProject(null);      // Remove the project reference from the site
    }
    
    public Site getDefaultSite() {
    	return sites.get(0);
    }
}
