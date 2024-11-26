package com.dwellsmart.entity;

import jakarta.persistence.Basic;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "meter_maps")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeterMap {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private Long rowId;

	@Basic(optional = false)
	@Column(nullable = false, length = 40)
	private String cardIpAddress;
	
	@Basic(optional = false)
	@Column(nullable = false,length = 10)
	private String meterPortId;
	
	@Column(nullable = false)
	@Builder.Default
	private Boolean isActive = true;
	
	@Column(nullable = false)
	@Builder.Default
	private Boolean overrideadmin = false;
	
	@Basic(optional = false)
	@Column(nullable = false)
	private Integer meterPhase;
	
	@Column(nullable = false)
	@Builder.Default
	private Boolean isMeterFaulty = false;
	
	@Column(length = 3200)
	private String faultyReason;
	
	@Column(length= 600)
	private String remark;
	
	@Column(length= 50) //In days Like: 2 days, 4 days...
	private String readingNotUpdatedSince;
	
	@ManyToOne(fetch = FetchType.LAZY)  //cascade = CascadeType.ALL
	@Basic(optional = false)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@Basic(optional= false)
	@JoinColumn(name= "site_id",nullable = false)
	private Site site;
	
	
	// One-to-One relationship with Resident
	@OneToOne(mappedBy = "metermap",fetch = FetchType.LAZY) //, cascade = CascadeType.ALL
	private Resident resident;
	
	
	

}