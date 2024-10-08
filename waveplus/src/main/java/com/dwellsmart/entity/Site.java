package com.dwellsmart.entity;

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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sites", uniqueConstraints = {@UniqueConstraint(columnNames = {"site_name", "project_id"})})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Site {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Integer siteId;

	@Basic(optional = false)
	@Column(nullable = false, length = 250)
	private String siteName;

	@Basic(optional = false)
	@Column(nullable = false)
	private Double gridRate;

	@Basic(optional = false)
	@Column(nullable = false)
	private Double dgRate;

	@Column(nullable = false)
	@Builder.Default
	private Double gridDutySurchagre = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double maintainenceRate = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Integer minRechargeAmt = 0;

	@Column(nullable = false)
	@Builder.Default
	private Double dgFixedMmc = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double tAndDLosses = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double fixedMonthMeterGrid = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double interestRate = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Integer dgConsumptionCheckLimit = 0;

	@Column(nullable = false)
	@Builder.Default
	private Integer gridConsumptionCheckLimit = 0;

	@Column(nullable = false)
	@Builder.Default
	private Integer freedgunits = 0;

	@Column(nullable = false)
	@Builder.Default
	private Integer fixedMaintainenceCharge = 0;

	@Column(nullable = false)
	@Builder.Default
	private Double fixedChargePerKva = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double vendingCharge = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double waterCharge = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double fixedMonthChargesCgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double fixedMonthChargesSgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double fixedMonthChargesIgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double unitChargesGridCgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double unitChargesGridSgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double unitChargesGridIgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double electricitySurchargeCgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double electricitySurchargeSgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double electricitySurchargeIgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double dgFixedMmcCgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double dgFixedMmcSgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double dgFixedMmcIgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double unitChargesDgCgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double unitChargesDgSgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double unitChargesDgIgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double tdLossCgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double tdLossSgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double tdLossIgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double waterCgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double waterSgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double waterIgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double vendingCgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double vendingSgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double vendingIgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double commomAreaMaintainenceChargesCgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double commomAreaMaintainenceChargesSgst = 0.0;

	@Column(nullable = false)
	@Builder.Default
	private Double commomAreaMaintainenceChargesIgst = 0.0;

	private Double slab2Rate;

	private Double slab3Rate;

	private Double slab4Rate;

	private Double slab1Limit;

	private Double slab2Limit;

	private Double slab3Limit;

	@ManyToOne(fetch = FetchType.LAZY)
	@Basic(optional = false)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	@OneToMany(mappedBy = "site", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Resident> residents;
}
