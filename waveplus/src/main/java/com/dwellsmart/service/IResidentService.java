
package com.dwellsmart.service;

import java.util.List;

import com.dwellsmart.entity.Resident;

public interface IResidentService {
	
	 public List<Resident> getActiveResidentsByProjectId(Integer projectId);

	void saveResident(Resident resident);

}
