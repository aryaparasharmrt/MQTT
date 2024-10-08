package com.dwellsmart.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "devices")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@Basic(optional = false)
	@Column(unique = true, nullable = false)
	private String deviceId;

	@Basic(optional = false)
	@Column(nullable = false)
	private String deviceType; // Example: mobile, tablet, etc.

	private String OS; // Example: Android, iOS, etc.

	private String version;
	
	@Basic(optional = false)
	@Column(nullable = false)
	private LocalDateTime loginDate;

	@Column(nullable = false)
	@Builder.Default
	private boolean revoked = false;

	@Basic(optional = false)
	@Column(nullable = false)
	private LocalDateTime tokenCreatedAt;

	@Basic(optional = false)
	@Column(unique = true, nullable =  false)
	private String refreshToken; // Device-specific refresh token
	
	@ManyToOne(fetch = FetchType.LAZY) // nullable = false - we will think later
	@Basic(optional = false)
	@JoinColumn(name = "user_id" , nullable = false) // referencedColumnName = "username") 
	@JsonBackReference
	private User user;

}
