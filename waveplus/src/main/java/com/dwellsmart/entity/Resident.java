package com.dwellsmart.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "residents")
public class Resident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long residentId;

    private String address;
    private String contactInfo;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "username")  // insertable = false, updatable = false -> we will discuss later
    private User user; 
}
