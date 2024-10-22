package com.dwellsmart.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "residents", uniqueConstraints = {@UniqueConstraint(columnNames = {"flat_no", "project_id"})})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"user","project","site","account"})
public class Resident {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long residentId;

	@Column(length = 150)
	private String primaryOwnerName;

	@Column(length = 150)
	private String secondaryOwnerName;

	@Basic(optional = false)
	@Column(nullable = false, length = 80)
	private String emailId;

	@Basic(optional = false)
	@Column(nullable = false)
	private String phoneNumber;

	@Basic(optional = false)
	@Column(nullable = false, length = 20)
	private String flatNo;

	@Basic(optional = false)
	@Column(nullable = false)
	private Long flatArea;

	@Column(length = 40)
	private String meterRefId;   

	@Column(length = 20)
	private String occupancyType;

	@Column(nullable = false)
	@Builder.Default
	private Boolean emailPreference = false;

	@Column(nullable = false)
	@Builder.Default
	private Boolean smsPreference = false;

	@Column(nullable = false)
	@Builder.Default
	private Boolean isActive = true;

	@OneToOne(fetch = FetchType.LAZY) //, cascade = CascadeType.ALL
	@Basic(optional = false)
	@JoinColumn(name = "user_id", nullable = false) // referencedColumnName = "username", insertable = false, updatable
	private User user; // = false -> we will discuss later

	@ManyToOne(fetch = FetchType.LAZY)  //cascade = CascadeType.ALL
	@Basic(optional = false)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id", nullable = false)
	private Site site;

	@OneToOne(mappedBy = "resident", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Account account;

	// One-to-One relationship with Resident
//	@OneToMany(mappedBy = "resident",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//	@Builder.Default
//	private List<Account> accounts = new ArrayList<>();

//    public void addAccount(Account account) {
//        accounts.add(account);          
//        account.setResident(this);     
//    }
//
//    public void removeAccount(Account account) {
//        accounts.remove(account);       
//        account.setResident(null);      
//    }		

}
