package com.dwellsmart.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwellsmart.entity.Project;
import com.dwellsmart.entity.Site;
import com.dwellsmart.repository.SiteRepository;
import com.dwellsmart.service.IProjectService;
import com.dwellsmart.service.ISiteService;

@Service
public class SiteService implements ISiteService {
	
	@Autowired
    private SiteRepository siteRepository;
	
//	@Autowired
//	private IProjectService projectService;

    @Override
    public List<Site> getAllSites() {
        return siteRepository.findAll();
    }

    @Override
    public Optional<Site> getSiteById(Integer siteId) {
        return siteRepository.findById(siteId);
    }

    @Override
    public Site createSite(Site site) {
        return siteRepository.save(site);
    }

    @Override
    public Site updateSite(Integer siteId, Site updatedSite) {
        return siteRepository.findById(siteId).map(existingSite -> {
            // Update the fields of the existing site
            existingSite.setSiteName(updatedSite.getSiteName());
            existingSite.setGridRate(updatedSite.getGridRate());
            existingSite.setDgRate(updatedSite.getDgRate());
            // Update other fields as necessary
            return siteRepository.save(existingSite);
        }).orElseThrow(() -> new RuntimeException("Site not found with id " + siteId));
    }

    @Override
    public void deleteSite(Integer siteId) {
        siteRepository.deleteById(siteId);
    }
    
    
    public Site createNewSite(Integer projectId ) {
//        try {
//        	
//        	Project projectById = projectService.getProjectById(projectId);
//
//            if (projectService.isProjectActive(projectId)) {
//            	
//            	
//                site.setProjectId(projectId);
//                siteService.addNewSite(site);
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful. New Site is ready for use. " + site.getSiteId(), null);
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            } else {
//                throw new InactiveProjectException("Project is inactive, Kindly contact support team.");
//            }
//        } catch (DuplicateSiteException ex) {
//            Logger.getLogger(ManageSiteBean.class.getName()).log(Level.SEVERE, null, ex);
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null);
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        } catch (InactiveProjectException ex) {
//            Logger.getLogger(ManageSiteBean.class.getName()).log(Level.SEVERE, null, ex);
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null);
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
        return null;
    }

}
