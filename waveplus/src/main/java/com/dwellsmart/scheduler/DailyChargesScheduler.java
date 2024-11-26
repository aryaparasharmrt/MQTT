package com.dwellsmart.scheduler;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dwellsmart.entity.Project;
import com.dwellsmart.entity.Resident;
import com.dwellsmart.service.IProjectService;
import com.dwellsmart.service.IResidentService;
import com.dwellsmart.service.business.DailyChargesService;

@Component
public class DailyChargesScheduler {

	@Autowired
	private DailyChargesService dailyChargesService;

	@Autowired
	private IProjectService projectService;

	@Autowired
	private IResidentService residentService;

//    @Scheduled(cron = "0 0 1 * * ?")  // Schedule for daily at 1 AM
	public void runDailyCharges() {

		List<Project> allProjects = projectService.getAllProjects();
		for (Project project : allProjects) {
			
		 if (project.getActiveStatus() && project.getAutomaticBillingEnabled()) {

			List<Resident> activeResidents = residentService.getActiveResidentsByProjectId(project.getProjectId());
//			activeResidents.forEach(dailyChargesService::processResidentDailyCharges);
			for (Resident resident : activeResidents) {
				Resident processResidentDailyCharges = dailyChargesService.processResidentDailyCharges(resident);
//				residentService.saveResident(processResidentDailyCharges);
			}
		}
		 }
	}

	private List<Integer> fetchAllAccountIds() {
		// Logic to fetch account IDs from the database
		return Arrays.asList(1, 2, 3, 4, 5); // Example static list
	}
}
