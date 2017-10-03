package com.mycompany.mystaff.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.mystaff.domain.Company;
import com.mycompany.mystaff.domain.Location;
import com.mycompany.mystaff.repository.LocationRepository;
import com.mycompany.mystaff.repository.search.LocationSearchRepository;
import com.mycompany.mystaff.service.dto.LocationDTO;
import com.mycompany.mystaff.service.mapper.LocationMapper;

/**
 * Service Implementation for managing Location.
 */
@Service
@Transactional
public class LocationService {

  private final Logger log = LoggerFactory.getLogger(LocationService.class);

  private final LocationRepository locationRepository;

  private final LocationMapper locationMapper;

  private final LocationSearchRepository locationSearchRepository;

  public LocationService(LocationRepository locationRepository, LocationMapper locationMapper, LocationSearchRepository locationSearchRepository) {
    this.locationRepository = locationRepository;
    this.locationMapper = locationMapper;
    this.locationSearchRepository = locationSearchRepository;
  }

  /**
   * Save a location.
   *
   * @param locationDTO the entity to save
   * @return the persisted entity
   */
  public LocationDTO save(LocationDTO locationDTO, Long companyId) {
    log.debug("Request to save Location : {}, companyId : {}", locationDTO, companyId);

    Location location = locationMapper.toEntity(locationDTO);
    location = locationRepository.save(location);
    location.setCompany(new Company(companyId));
    LocationDTO result = locationMapper.toDto(location);
    locationSearchRepository.save(location);
    return result;
  }

  /**
   * Get all the locations.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public Page<LocationDTO> findByCompanyId(Pageable pageable, Long companyId) {
    log.debug("Request to get all Locations by CompanyId : {}", companyId);

    return locationRepository.findByCompanyId(pageable, companyId).map(locationMapper::toDto);
  }

  /**
   * Get one location by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public LocationDTO findByIdAndCompanyId(Long id, Long companyId) {
    log.debug("Request to get LocationId : {}, CompanyId : {}", id, companyId);

    Location location = locationRepository.findByIdAndCompanyId(id, companyId);
    return locationMapper.toDto(location);
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
  public Page<LocationDTO> search(String query, Pageable pageable, Long companyId) {
    log.debug("Request to search for a page of Locations for query {}", query);

    Page<Location> result = locationSearchRepository.search(queryStringQuery(query), pageable);
    return result.map(locationMapper::toDto);
  }

}
