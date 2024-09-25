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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Entity
@Table(name = "sites")
@Data
public class Site {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer siteId;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    private String siteName;
    
//    @Basic(optional = false)
//    @NotNull
    private Double gridRate;
    
//    @Basic(optional = false)
//    @NotNull
    private Double dgRate;
    
//    @Basic(optional = false)
//    @NotNull
    private Double gridDutySurchagre;
    
//    @Basic(optional = false)
//    @NotNull
    private Double maintainenceRate;
    
//    @Basic(optional = false)
//    @NotNull
    private Double dgFixedMmc;
    
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "t_and_d_losses")
//    private double tAndDLosses;
    
//    @Basic(optional = false)
//    @NotNull
    private Double fixedMonthMeterGrid;
    
//    @Basic(optional = false)
//    @NotNull
    private Double interestRate;
    
//    @Basic(optional = false)
//    @NotNull
    private Integer dgConsumptionCheckLimit;
    
//    @Basic(optional = false)
//    @NotNull
    private int gridConsumptionCheckLimit;
    
//    @Basic(optional = false)
//    @NotNull
    private Double freedgunits;
    
//    @Basic(optional = false)
//    @NotNull
    private Double fixedMaintainenceCharge;
    

    
 // Many-to-one relationship with Project
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    
//    @Basic(optional = false)
//    @NotNull
    private Double fixedChargePerKva;
    
    private Double vendingCharge;
    
//    @Column(name = "fixed_month_charges_cgst")
//    private Double fixedMonthChargesCgst;
//    
//    @Column(name = "fixed_month_charges_sgst")
//    private Double fixedMonthChargesSgst;
//    
//    @Column(name = "fixed_month_charges_igst")
//    private Double fixedMonthChargesIgst;
//    
//    @Column(name = "unit_charges_grid_cgst")
//    private Double unitChargesGridCgst;
//    
//    @Column(name = "unit_charges_grid_sgst")
//    private Double unitChargesGridSgst;
//    
//    @Column(name = "unit_charges_grid_igst")
//    private Double unitChargesGridIgst;
//    
//    @Column(name = "electricity_surcharge_cgst")
//    private Double electricitySurchargeCgst;
//    
//    @Column(name = "electricity_surcharge_sgst")
//    private Double electricitySurchargeSgst;
//    
//    @Column(name = "electricity_surcharge_igst")
//    private Double electricitySurchargeIgst;
//    
//    @Column(name = "dg_fixed_mmc_cgst")
//    private Double dgFixedMmcCgst;
//    
//    @Column(name = "dg_fixed_mmc_sgst")
//    private Double dgFixedMmcSgst;
//    
//    @Column(name = "dg_fixed_mmc_igst")
//    private Double dgFixedMmcIgst;
//    
//    @Column(name = "unit_charges_dg_cgst")
//    private Double unitChargesDgCgst;
//    
//    @Column(name = "unit_charges_dg_sgst")
//    private Double unitChargesDgSgst;
//    
//    @Column(name = "unit_charges_dg_igst")
//    private Double unitChargesDgIgst;
//    
//    @Column(name = "td_loss_cgst")
//    private Double tdLossCgst;
//    
//    @Column(name = "td_loss_sgst")
//    private Double tdLossSgst;
//    
//    @Column(name = "td_loss_igst")
//    private Double tdLossIgst;
//    
//    @Column(name = "water_cgst")
//    private Double waterCgst;
//    
//    @Column(name = "water_sgst")
//    private Double waterSgst;
//    
//    @Column(name = "water_igst")
//    private Double waterIgst;
//    
//    @Column(name = "vending_cgst")
//    private Double vendingCgst;
//    
//    @Column(name = "vending_sgst")
//    private Double vendingSgst;
//    
//    @Column(name = "vending_igst")
//    private Double vendingIgst;
//    
//    @Column(name = "commom_area_maintainence_charges_cgst")
//    private Double commomAreaMaintainenceChargesCgst;
//    
//    @Column(name = "commom_area_maintainence_charges_sgst")
//    private Double commomAreaMaintainenceChargesSgst;
//    
//    @Column(name = "commom_area_maintainence_charges_igst")
//    private Double commomAreaMaintainenceChargesIgst;
//    
//    @Column(name = "slab2_rate")
//    private Double slab2Rate;
//    
//    @Column(name = "slab3_rate")
//    private Double slab3Rate;
//    
//    @Column(name = "slab4_rate")
//    private Double slab4Rate;
//    
//    @Column(name = "slab1_limit")
//    private Double slab1Limit;
//    
//    @Column(name = "slab2_limit")
//    private Double slab2Limit;
//    
//    @Column(name = "slab3_limit")
//    private Double slab3Limit;


}

