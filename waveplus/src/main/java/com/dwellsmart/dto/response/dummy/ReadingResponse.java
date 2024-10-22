package com.dwellsmart.dto.response.dummy;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReadingResponse {

	private String meterRefId; // Optional since it can be null

	@NotNull(message = "Grid reading is required")
	@DecimalMin(value = "0.0", message = "Grid reading must be positive")
	private Double gridReading;

	@NotNull(message = "DG reading is required")
	@DecimalMin(value = "0.0", message = "DG reading must be positive")
	private Double dgReading;

//	private LocalDate readingDate; // Nullable field

//	private String readingMode; // Nullable field, could add enum if there's a set of valid modes

//	private Long timestamp; // Nullable, can store timestamp in milliseconds

}
