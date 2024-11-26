package com.dwellsmart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminDashboardResponse {

	private String username;
	private String fullname;
	private String email;
	private String projectname;
}
