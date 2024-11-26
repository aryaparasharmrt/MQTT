package com.dwellsmart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDashboardResponse {

	private String username;
	private String fullname;
	private String email;
	private String projectname;
	private String primaryOwner;
	private String flatNo;

}
