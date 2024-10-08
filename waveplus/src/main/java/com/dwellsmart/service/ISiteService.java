package com.dwellsmart.service;

import java.util.List;
import java.util.Optional;

import com.dwellsmart.entity.Site;

public interface ISiteService {

	   List<Site> getAllSites();
	    Optional<Site> getSiteById(Integer siteId);
	    Site createSite(Site site);
	    Site updateSite(Integer siteId, Site updatedSite);
	    void deleteSite(Integer siteId);
}
