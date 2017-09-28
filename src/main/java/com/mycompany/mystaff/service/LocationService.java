package com.mycompany.mystaff.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.mystaff.domain.Location;
import com.mycompany.mystaff.repository.LocationRepository;
import com.mycompany.mystaff.repository.search.LocationSearchRepository;

/**
 * Service Implementation for managing Location.
 */
@Service
@Transactional
public class LocationService {

  private final Logger log = LoggerFactory.getLogger(LocationService.class);

  private final LocationRepository locationRepository;

  private final LocationSearchRepository locationSearchRepository;

  public LocationService(LocationRepository locationRepository, LocationSearchRepository locationSearchRepository) {
    this.locationRepository = locationRepository;
    this.locationSearchRepository = locationSearchRepository;
  }

  /**
   * Save a location.
   *
   * @param location the entity to save
   * @return the persisted entity
   */
  public Location save(Location location) {
    log.debug("Request to save Location : {}", location);

    Location result = locationRepository.save(location);
    locationSearchRepository.save(result);
    return result;
  }

  /**
   * Get all the locations.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public Page<Location> findByCompanyId(Pageable pageable, Long companyId) {
    log.debug("Request to get all Locations by CompanyId : {}", companyId);

    return locationRepository.findByCompanyId(pageable, companyId);
  }

  /**
   * Get one location by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public Location findByIdAndCompanyId(Long id, Long companyId) {
    log.debug("Request to get LocationId : {}, CompanyId : {}", id, companyId);

    return locationRepository.findByIdAndCompanyId(id, companyId);
  }

  /**
   * Delete the location by id.
   *
   * @param id the id of the entity
   */
  public void deleteByIdAndCompanyId(Long id, Long companyId) {
    log.debug("Request to delete LocationId : {}, CompanyId : {}", id, companyId);

    locationRepository.deleteByIdAndCompanyId(id, companyId);
    locationSearchRepository.deleteByIdAndCompanyId(id, companyId);
  }

  /**
   * Search for the location corresponding to the query.
   *
   * @param query the query of the search
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public Page<Location> search(String query, Pageable pageable, Long companyId) {
    log.debug("Request to search for a page of Locations for query {}", query);

    Page<Location> result = locationSearchRepository.search(queryStringQuery(query), pageable);
    return result;
  }

}
