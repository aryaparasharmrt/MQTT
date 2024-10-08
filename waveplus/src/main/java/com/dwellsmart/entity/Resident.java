package com.dwellsmart.entity;

import java.util.ArrayList;
import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "residents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resident {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long residentId;
	


	
	
//	@Column(nullable = false, length = 40)
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="site_id")
	private Site site;

	@Column(length = 150)
	private String customerName;

	@Column(length = 80)
	private String emailId;

	@Column
	private Long phoneNumber;

	@Column(nullable = false, length = 20)
	private String flatNo;

	@Column(nullable = false)
	private Long flatArea;

	@Column(name = "meter_id", length = 40)
	private String meterId;

	@Column(name = "status", length = 5)
	@Builder.Default
	private String status = "A";  //Active - A , Inactive - I 
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@Basic(optional = false)
	@JoinColumn(name = "user_id",nullable = false) // referencedColumnName = "username", insertable = false, updatable = false -> we will discuss later
	private User user;

	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@Basic(optional = false)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;
	
	// One-to-One relationship with Resident
	@OneToMany(mappedBy = "resident",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@Builder.Default
	private List<Account> accounts = new ArrayList<>();
	
	 // Add site to the project
    public void addAccount(Account account) {
        accounts.add(account);           // Add site to the list
        account.setResident(this);      // Set the project reference in the site
    }

    // Remove site from the project
    public void removeAccount(Account account) {
        accounts.remove(account);         // Remove site from the list
        account.setResident(null);      // Remove the project reference from the site
    }
	
	
//	@Column(name = "date_of_possession")
//	@Temporal(TemporalType.DATE)
//	private Date dateOfPossession;

//	@Column(name = "transfer_memorandum_number", length = 50)
//	private String transferMemorandumNumber;

//	@Column(name = "society_membership_number", length = 50)
//	private String societyMembershipNumber;
//
//	@Column(name = "membership_receipt", length = 20)
//	private String membershipReceipt;
//
//	@Column(name = "receipt_date")
//	@Temporal(TemporalType.DATE)
//	private Date receiptDate;
//
//	@Column(name = "receipt_amt", precision = 10, scale = 2)
//	private Long receiptAmt;

//	@Column(name = "img_name", columnDefinition = "TEXT")
//	private String imgName;
//
//	@Column(name = "img")
//	private byte[] img;

}
