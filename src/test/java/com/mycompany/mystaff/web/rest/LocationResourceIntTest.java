package com.mycompany.mystaff.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.mystaff.MystaffApp;
import com.mycompany.mystaff.domain.Company;
import com.mycompany.mystaff.domain.Location;
import com.mycompany.mystaff.repository.LocationRepository;
import com.mycompany.mystaff.repository.search.LocationSearchRepository;
import com.mycompany.mystaff.security.jwt.TokenProvider;
import com.mycompany.mystaff.service.LocationService;
import com.mycompany.mystaff.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the LocationResource REST controller.
 *
 * @see LocationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MystaffApp.class)
public class LocationResourceIntTest {

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_ADDRESS_1 = "AAAAAAAAAA";
  private static final String UPDATED_ADDRESS_1 = "BBBBBBBBBB";

  private static final String DEFAULT_ADDRESS_2 = "AAAAAAAAAA";
  private static final String UPDATED_ADDRESS_2 = "BBBBBBBBBB";

  private static final String DEFAULT_ADDRESS_3 = "AAAAAAAAAA";
  private static final String UPDATED_ADDRESS_3 = "BBBBBBBBBB";

  private static final Integer DEFAULT_NUMBER = 1;
  private static final Integer UPDATED_NUMBER = 2;

  private static final String DEFAULT_POSTCODE = "AAAAAAAAAA";
  private static final String UPDATED_POSTCODE = "BBBBBBBBBB";

  private static final String DEFAULT_CITY = "AAAAAAAAAA";
  private static final String UPDATED_CITY = "BBBBBBBBBB";

  private static final String DEFAULT_STATE_PROVINCE = "AAAAAAAAAA";
  private static final String UPDATED_STATE_PROVINCE = "BBBBBBBBBB";

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private LocationService locationService;

  @Autowired
  private LocationSearchRepository locationSearchRepository;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private HttpServletRequest request;

  private MockMvc restLocationMockMvc;

  private Location location;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final LocationResource locationResource = new LocationResource(locationService, request, tokenProvider);
    this.restLocationMockMvc = MockMvcBuilders.standaloneSetup(locationResource).setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static Location createEntity(EntityManager em) {
    Location location = new Location().name(DEFAULT_NAME).address1(DEFAULT_ADDRESS_1).address2(DEFAULT_ADDRESS_2).address3(DEFAULT_ADDRESS_3).number(DEFAULT_NUMBER)
        .postcode(DEFAULT_POSTCODE).city(DEFAULT_CITY).stateProvince(DEFAULT_STATE_PROVINCE);
    // Add required entity
    Company company = CompanyResourceIntTest.createEntity(em);
    em.persist(company);
    em.flush();
    location.setCompany(company);
    return location;
  }

  @Before
  public void initTest() {
    locationSearchRepository.deleteAll();
    location = createEntity(em);
  }

  @Test
  @Transactional
  public void createLocation() throws Exception {
    int databaseSizeBeforeCreate = locationRepository.findAll().size();

    // Create the Location
    restLocationMockMvc.perform(post("/api/locations").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(location)))
        .andExpect(status().isCreated());

    // Validate the Location in the database
    List<Location> locationList = locationRepository.findAll();
    assertThat(locationList).hasSize(databaseSizeBeforeCreate + 1);
    Location testLocation = locationList.get(locationList.size() - 1);
    assertThat(testLocation.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testLocation.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
    assertThat(testLocation.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
    assertThat(testLocation.getAddress3()).isEqualTo(DEFAULT_ADDRESS_3);
    assertThat(testLocation.getNumber()).isEqualTo(DEFAULT_NUMBER);
    assertThat(testLocation.getPostcode()).isEqualTo(DEFAULT_POSTCODE);
    assertThat(testLocation.getCity()).isEqualTo(DEFAULT_CITY);
    assertThat(testLocation.getStateProvince()).isEqualTo(DEFAULT_STATE_PROVINCE);

    // Validate the Location in Elasticsearch
    Location locationEs = locationSearchRepository.findOne(testLocation.getId());
    assertThat(locationEs).isEqualToComparingFieldByField(testLocation);
  }

  @Test
  @Transactional
  public void createLocationWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = locationRepository.findAll().size();

    // Create the Location with an existing ID
    location.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restLocationMockMvc.perform(post("/api/locations").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(location)))
        .andExpect(status().isBadRequest());

    // Validate the Location in the database
    List<Location> locationList = locationRepository.findAll();
    assertThat(locationList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = locationRepository.findAll().size();
    // set the field null
    location.setName(null);

    // Create the Location, which fails.

    restLocationMockMvc.perform(post("/api/locations").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(location)))
        .andExpect(status().isBadRequest());

    List<Location> locationList = locationRepository.findAll();
    assertThat(locationList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllLocations() throws Exception {
    // Initialize the database
    locationRepository.saveAndFlush(location);

    // Get all the locationList
    restLocationMockMvc.perform(get("/api/locations?sort=id,desc")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue()))).andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1.toString()))).andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
        .andExpect(jsonPath("$.[*].address3").value(hasItem(DEFAULT_ADDRESS_3.toString()))).andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
        .andExpect(jsonPath("$.[*].postcode").value(hasItem(DEFAULT_POSTCODE.toString()))).andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
        .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE.toString())));
  }

  @Test
  @Transactional
  public void getLocation() throws Exception {
    // Initialize the database
    locationRepository.saveAndFlush(location);

    // Get the location
    restLocationMockMvc.perform(get("/api/locations/{id}", location.getId())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(location.getId().intValue())).andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
        .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS_1.toString())).andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2.toString()))
        .andExpect(jsonPath("$.address3").value(DEFAULT_ADDRESS_3.toString())).andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
        .andExpect(jsonPath("$.postcode").value(DEFAULT_POSTCODE.toString())).andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
        .andExpect(jsonPath("$.stateProvince").value(DEFAULT_STATE_PROVINCE.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingLocation() throws Exception {
    // Get the location
    restLocationMockMvc.perform(get("/api/locations/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateLocation() throws Exception {
    // Initialize the database
    locationService.save(location);

    int databaseSizeBeforeUpdate = locationRepository.findAll().size();

    // Update the location
    Location updatedLocation = locationRepository.findOne(location.getId());
    updatedLocation.name(UPDATED_NAME).address1(UPDATED_ADDRESS_1).address2(UPDATED_ADDRESS_2).address3(UPDATED_ADDRESS_3).number(UPDATED_NUMBER).postcode(UPDATED_POSTCODE)
        .city(UPDATED_CITY).stateProvince(UPDATED_STATE_PROVINCE);

    restLocationMockMvc.perform(put("/api/locations").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(updatedLocation)))
        .andExpect(status().isOk());

    // Validate the Location in the database
    List<Location> locationList = locationRepository.findAll();
    assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    Location testLocation = locationList.get(locationList.size() - 1);
    assertThat(testLocation.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testLocation.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
    assertThat(testLocation.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
    assertThat(testLocation.getAddress3()).isEqualTo(UPDATED_ADDRESS_3);
    assertThat(testLocation.getNumber()).isEqualTo(UPDATED_NUMBER);
    assertThat(testLocation.getPostcode()).isEqualTo(UPDATED_POSTCODE);
    assertThat(testLocation.getCity()).isEqualTo(UPDATED_CITY);
    assertThat(testLocation.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);

    // Validate the Location in Elasticsearch
    Location locationEs = locationSearchRepository.findOne(testLocation.getId());
    assertThat(locationEs).isEqualToComparingFieldByField(testLocation);
  }

  @Test
  @Transactional
  public void updateNonExistingLocation() throws Exception {
    int databaseSizeBeforeUpdate = locationRepository.findAll().size();

    // Create the Location

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restLocationMockMvc.perform(put("/api/locations").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(location)))
        .andExpect(status().isCreated());

    // Validate the Location in the database
    List<Location> locationList = locationRepository.findAll();
    assertThat(locationList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteLocation() throws Exception {
    // Initialize the database
    locationService.save(location);

    int databaseSizeBeforeDelete = locationRepository.findAll().size();

    // Get the location
    restLocationMockMvc.perform(delete("/api/locations/{id}", location.getId()).accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

    // Validate Elasticsearch is empty
    boolean locationExistsInEs = locationSearchRepository.exists(location.getId());
    assertThat(locationExistsInEs).isFalse();

    // Validate the database is empty
    List<Location> locationList = locationRepository.findAll();
    assertThat(locationList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void searchLocation() throws Exception {
    // Initialize the database
    locationService.save(location);

    // Search the location
    restLocationMockMvc.perform(get("/api/_search/locations?query=id:" + location.getId())).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString()))).andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
        .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2.toString()))).andExpect(jsonPath("$.[*].address3").value(hasItem(DEFAULT_ADDRESS_3.toString())))
        .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER))).andExpect(jsonPath("$.[*].postcode").value(hasItem(DEFAULT_POSTCODE.toString())))
        .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString()))).andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE.toString())));
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Location.class);
    Location location1 = new Location();
    location1.setId(1L);
    Location location2 = new Location();
    location2.setId(location1.getId());
    assertThat(location1).isEqualTo(location2);
    location2.setId(2L);
    assertThat(location1).isNotEqualTo(location2);
    location1.setId(null);
    assertThat(location1).isNotEqualTo(location2);
  }

}
