package com.dwellsmart.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dwellsmart.constants.Endpoints;
import com.dwellsmart.entity.Site;
import com.dwellsmart.service.ISiteService;

@RestController
@RequestMapping(Endpoints.BASE+ Endpoints.SITES)
public class SiteController {

    @Autowired
    private ISiteService siteService;

    @GetMapping
    public List<Site> getAllSites() {
        return siteService.getAllSites();
    }

    @GetMapping("/{siteId}")
    public ResponseEntity<Site> getSiteById(@PathVariable Integer siteId) {
        Optional<Site> site = siteService.getSiteById(siteId);
        return site.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Site> createSite(@RequestBody Site site) {
        Site newSite = siteService.createSite(site);
        return ResponseEntity.ok(newSite);
    }

    @PutMapping("/{siteId}")
    public ResponseEntity<Site> updateSite(@PathVariable Integer siteId, @RequestBody Site updatedSite) {
        try {
            Site site = siteService.updateSite(siteId, updatedSite);
            return ResponseEntity.ok(site);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{siteId}")
    public ResponseEntity<Void> deleteSite(@PathVariable Integer siteId) {
        siteService.deleteSite(siteId);
        return ResponseEntity.noContent().build();
    }
}
