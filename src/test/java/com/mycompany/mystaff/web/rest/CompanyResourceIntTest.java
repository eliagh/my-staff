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
import org.springframework.util.Base64Utils;

import com.mycompany.mystaff.MystaffApp;
import com.mycompany.mystaff.domain.Company;
import com.mycompany.mystaff.repository.CompanyRepository;
import com.mycompany.mystaff.repository.search.CompanySearchRepository;
import com.mycompany.mystaff.service.CompanyService;
import com.mycompany.mystaff.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the CompanyResource REST controller.
 *
 * @see CompanyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MystaffApp.class)
public class CompanyResourceIntTest {

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
  private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(2, "1");
  private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
  private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

  private static final String DEFAULT_SECTOR = "AAAAAAAAAA";
  private static final String UPDATED_SECTOR = "BBBBBBBBBB";

  private static final String DEFAULT_THEMA = "AAAAAAAAAA";
  private static final String UPDATED_THEMA = "BBBBBBBBBB";

  @Autowired
  private CompanyRepository companyRepository;

  @Autowired
  private CompanyService companyService;

  @Autowired
  private CompanySearchRepository companySearchRepository;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restCompanyMockMvc;

  private Company company;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final CompanyResource companyResource = new CompanyResource(companyService);
    this.restCompanyMockMvc = MockMvcBuilders.standaloneSetup(companyResource).setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static Company createEntity(EntityManager em) {
    Company company = new Company().name(DEFAULT_NAME).logo(DEFAULT_LOGO).logoContentType(DEFAULT_LOGO_CONTENT_TYPE).sector(DEFAULT_SECTOR).thema(DEFAULT_THEMA);
    return company;
  }

  @Before
  public void initTest() {
    companySearchRepository.deleteAll();
    company = createEntity(em);
  }

  @Test
  @Transactional
  public void createCompany() throws Exception {
    int databaseSizeBeforeCreate = companyRepository.findAll().size();

    // Create the Company
    restCompanyMockMvc.perform(post("/api/companies").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(company)))
        .andExpect(status().isCreated());

    // Validate the Company in the database
    List<Company> companyList = companyRepository.findAll();
    assertThat(companyList).hasSize(databaseSizeBeforeCreate + 1);
    Company testCompany = companyList.get(companyList.size() - 1);
    assertThat(testCompany.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testCompany.getLogo()).isEqualTo(DEFAULT_LOGO);
    assertThat(testCompany.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
    assertThat(testCompany.getSector()).isEqualTo(DEFAULT_SECTOR);
    assertThat(testCompany.getThema()).isEqualTo(DEFAULT_THEMA);

    // Validate the Company in Elasticsearch
    Company companyEs = companySearchRepository.findOne(testCompany.getId());
    assertThat(companyEs).isEqualToComparingFieldByField(testCompany);
  }

  @Test
  @Transactional
  public void createCompanyWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = companyRepository.findAll().size();

    // Create the Company with an existing ID
    company.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restCompanyMockMvc.perform(post("/api/companies").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(company)))
        .andExpect(status().isBadRequest());

    // Validate the Company in the database
    List<Company> companyList = companyRepository.findAll();
    assertThat(companyList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = companyRepository.findAll().size();
    // set the field null
    company.setName(null);

    // Create the Company, which fails.

    restCompanyMockMvc.perform(post("/api/companies").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(company)))
        .andExpect(status().isBadRequest());

    List<Company> companyList = companyRepository.findAll();
    assertThat(companyList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkSectorIsRequired() throws Exception {
    int databaseSizeBeforeTest = companyRepository.findAll().size();
    // set the field null
    company.setSector(null);

    // Create the Company, which fails.

    restCompanyMockMvc.perform(post("/api/companies").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(company)))
        .andExpect(status().isBadRequest());

    List<Company> companyList = companyRepository.findAll();
    assertThat(companyList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkThemaIsRequired() throws Exception {
    int databaseSizeBeforeTest = companyRepository.findAll().size();
    // set the field null
    company.setThema(null);

    // Create the Company, which fails.

    restCompanyMockMvc.perform(post("/api/companies").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(company)))
        .andExpect(status().isBadRequest());

    List<Company> companyList = companyRepository.findAll();
    assertThat(companyList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllCompanies() throws Exception {
    // Initialize the database
    companyRepository.saveAndFlush(company);

    // Get all the companyList
    restCompanyMockMvc.perform(get("/api/companies?sort=id,desc")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue()))).andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
        .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO)))).andExpect(jsonPath("$.[*].sector").value(hasItem(DEFAULT_SECTOR.toString())))
        .andExpect(jsonPath("$.[*].thema").value(hasItem(DEFAULT_THEMA.toString())));
  }

  @Test
  @Transactional
  public void getCompany() throws Exception {
    // Initialize the database
    companyRepository.saveAndFlush(company);

    // Get the company
    restCompanyMockMvc.perform(get("/api/companies/{id}", company.getId())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(company.getId().intValue())).andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
        .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE)).andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)))
        .andExpect(jsonPath("$.sector").value(DEFAULT_SECTOR.toString())).andExpect(jsonPath("$.thema").value(DEFAULT_THEMA.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingCompany() throws Exception {
    // Get the company
    restCompanyMockMvc.perform(get("/api/companies/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateCompany() throws Exception {
    // Initialize the database
    companyService.save(company);

    int databaseSizeBeforeUpdate = companyRepository.findAll().size();

    // Update the company
    Company updatedCompany = companyRepository.findOne(company.getId());
    updatedCompany.name(UPDATED_NAME).logo(UPDATED_LOGO).logoContentType(UPDATED_LOGO_CONTENT_TYPE).sector(UPDATED_SECTOR).thema(UPDATED_THEMA);

    restCompanyMockMvc.perform(put("/api/companies").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(updatedCompany)))
        .andExpect(status().isOk());

    // Validate the Company in the database
    List<Company> companyList = companyRepository.findAll();
    assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    Company testCompany = companyList.get(companyList.size() - 1);
    assertThat(testCompany.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testCompany.getLogo()).isEqualTo(UPDATED_LOGO);
    assertThat(testCompany.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
    assertThat(testCompany.getSector()).isEqualTo(UPDATED_SECTOR);
    assertThat(testCompany.getThema()).isEqualTo(UPDATED_THEMA);

    // Validate the Company in Elasticsearch
    Company companyEs = companySearchRepository.findOne(testCompany.getId());
    assertThat(companyEs).isEqualToComparingFieldByField(testCompany);
  }

  @Test
  @Transactional
  public void updateNonExistingCompany() throws Exception {
    int databaseSizeBeforeUpdate = companyRepository.findAll().size();

    // Create the Company

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restCompanyMockMvc.perform(put("/api/companies").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(company)))
        .andExpect(status().isCreated());

    // Validate the Company in the database
    List<Company> companyList = companyRepository.findAll();
    assertThat(companyList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteCompany() throws Exception {
    // Initialize the database
    companyService.save(company);

    int databaseSizeBeforeDelete = companyRepository.findAll().size();

    // Get the company
    restCompanyMockMvc.perform(delete("/api/companies/{id}", company.getId()).accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

    // Validate Elasticsearch is empty
    boolean companyExistsInEs = companySearchRepository.exists(company.getId());
    assertThat(companyExistsInEs).isFalse();

    // Validate the database is empty
    List<Company> companyList = companyRepository.findAll();
    assertThat(companyList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void searchCompany() throws Exception {
    // Initialize the database
    companyService.save(company);

    // Search the company
    restCompanyMockMvc.perform(get("/api/_search/companies?query=id:" + company.getId())).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString()))).andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
        .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO)))).andExpect(jsonPath("$.[*].sector").value(hasItem(DEFAULT_SECTOR.toString())))
        .andExpect(jsonPath("$.[*].thema").value(hasItem(DEFAULT_THEMA.toString())));
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Company.class);
    Company company1 = new Company();
    company1.setId(1L);
    Company company2 = new Company();
    company2.setId(company1.getId());
    assertThat(company1).isEqualTo(company2);
    company2.setId(2L);
    assertThat(company1).isNotEqualTo(company2);
    company1.setId(null);
    assertThat(company1).isNotEqualTo(company2);
  }

}
