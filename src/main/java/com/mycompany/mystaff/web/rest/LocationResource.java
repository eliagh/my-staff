package com.mycompany.mystaff.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.mystaff.security.jwt.JWTConfigurer;
import com.mycompany.mystaff.security.jwt.TokenProvider;
import com.mycompany.mystaff.service.LocationService;
import com.mycompany.mystaff.service.dto.LocationDTO;
import com.mycompany.mystaff.service.util.ResolveTokenUtil;
import com.mycompany.mystaff.web.rest.util.HeaderUtil;
import com.mycompany.mystaff.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Location.
 */
@RestController
@RequestMapping("/api")
public class LocationResource {

  private final Logger log = LoggerFactory.getLogger(LocationResource.class);

  private static final String ENTITY_NAME = "location";

  private final HttpServletRequest request;

  private final LocationService locationService;

  private final TokenProvider tokenProvider;

  public LocationResource(LocationService locationService, HttpServletRequest request, TokenProvider tokenProvider) {
    this.locationService = locationService;
    this.request = request;
    this.tokenProvider = tokenProvider;
  }

  /**
   * POST /locations : Create a new location.
   *
   * @param locationDTO the locationDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new locationDTO, or with
   *         status 400 (Bad Request) if the location has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/locations")
  @Timed
  public ResponseEntity<LocationDTO> createLocation(@Valid @RequestBody LocationDTO locationDTO) throws URISyntaxException {
    log.debug("REST request to save Location : {}", locationDTO);

    if (locationDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new location cannot already have an ID")).body(null);
    }

    final Long companyId = tokenProvider.getCompanyId(ResolveTokenUtil.resolveToken(request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER)));

    LocationDTO result = locationService.save(locationDTO, companyId);
    return ResponseEntity.created(new URI("/api/locations/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
  }

  /**
   * PUT /locations : Updates an existing location.
   *
   * @param locationDTO the locationDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated locationDTO, or with
   *         status 400 (Bad Request) if the locationDTO is not valid, or with status 500 (Internal
   *         Server Error) if the locationDTO couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/locations")
  @Timed
  public ResponseEntity<LocationDTO> updateLocation(@Valid @RequestBody LocationDTO locationDTO) throws URISyntaxException {
    log.debug("REST request to update Location : {}", locationDTO);

    if (locationDTO.getId() == null) {
      return createLocation(locationDTO);
    }

    final Long companyId = tokenProvider.getCompanyId(ResolveTokenUtil.resolveToken(request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER)));

    LocationDTO result = locationService.save(locationDTO, companyId);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, locationDTO.getId().toString())).body(result);
  }

  /**
   * GET /locations : get all the locations.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of locations in body
   */
  @GetMapping("/locations")
  @Timed
  public ResponseEntity<List<LocationDTO>> getAllLocations(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of Locations");

    final Long companyId = tokenProvider.getCompanyId(ResolveTokenUtil.resolveToken(request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER)));
    Page<LocationDTO> page = locationService.findByCompanyId(pageable, companyId);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/locations");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET /locations/:id : get the "id" location.
   *
   * @param id the id of the locationDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the locationDTO, or with status
   *         404 (Not Found)
   */
  @GetMapping("/locations/{id}")
  @Timed
  public ResponseEntity<LocationDTO> getLocation(@PathVariable Long id) {
    log.debug("REST request to get Location : {}", id);

    final Long companyId = tokenProvider.getCompanyId(ResolveTokenUtil.resolveToken(request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER)));
    LocationDTO locationDTO = locationService.findByIdAndCompanyId(id, companyId);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(locationDTO));
  }

  /**
   * DELETE /locations/:id : delete the "id" location.
   *
   * @param id the id of the locationDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/locations/{id}")
  @Timed
  public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
    log.debug("REST request to delete Location : {}", id);

    final Long companyId = tokenProvider.getCompanyId(ResolveTokenUtil.resolveToken(request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER)));
    locationService.deleteByIdAndCompanyId(id, companyId);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * SEARCH /_search/locations?query=:query : search for the location corresponding to the query.
   *
   * @param query the query of the location search
   * @param pageable the pagination information
   * @return the result of the search
   */
  @GetMapping("/_search/locations")
  @Timed
  public ResponseEntity<List<LocationDTO>> searchLocations(@RequestParam String query, @ApiParam Pageable pageable) {
    log.debug("REST request to search for a page of Locations for query {}", query);

    final Long companyId = tokenProvider.getCompanyId(ResolveTokenUtil.resolveToken(request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER)));
    Page<LocationDTO> page = locationService.search(query, pageable, companyId);
    HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/locations");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

}
