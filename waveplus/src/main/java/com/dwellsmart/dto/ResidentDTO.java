package com.dwellsmart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResidentDTO {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("primary_owner_name")
	private String primaryOwnerName;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("secondary_owner_name")
	private String secondaryOwnerName;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String username;

	@JsonInclude(JsonInclude.Include.NON_NULL)
//	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$", message = "Password should be at least 6 characters long and include an uppercase letter, a lowercase letter, a number, and a special character")
	@Size(min = 6, message = "Password should be at least 6 characters long")
	private String password;

	@NotBlank(message = "Email is mandatory")
	@Email(message = "Email should be valid")
	private String email;

	@Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number should be a valid format (10-15 digits)")
	@NotNull(message = "Phone number is mandatory")
	@JsonProperty("phone_number")
	private String phoneNumber;

	@NotBlank(message = "Flat number is mandatory")
	@JsonProperty("flat_no")
	private String flatNo;
	
	@JsonProperty("initial_account_balance")
	private Double initialAccountBalance;

	@NotNull(message = "Flat area is mandatory")
	@JsonProperty("flat_area")
	private Long flatArea;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("meter_id")
	private String meterId;

	@NotNull(message = "Site id is mandatory")
	@JsonProperty("site_id")
	private Integer siteId;

	@NotNull(message = "Project id is mandatory")
	@JsonProperty("project_id")
	private Integer projectId;

}
