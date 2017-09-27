package com.mycompany.mystaff.web.rest;

import static com.mycompany.mystaff.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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

import com.mycompany.mystaff.MystaffApp;
import com.mycompany.mystaff.domain.Company;
import com.mycompany.mystaff.domain.Customer;
import com.mycompany.mystaff.repository.CustomerRepository;
import com.mycompany.mystaff.repository.search.CustomerSearchRepository;
import com.mycompany.mystaff.service.CustomerService;
import com.mycompany.mystaff.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the CustomerResource REST controller.
 *
 * @see CustomerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MystaffApp.class)
public class CustomerResourceIntTest {

  private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
  private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_MIDLE_NAME = "AAAAAAAAAA";
  private static final String UPDATED_MIDLE_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
  private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
  private static final String UPDATED_LOGIN = "BBBBBBBBBB";

  private static final String DEFAULT_PASSWORD_HASH = "AAAAAAAAAA";
  private static final String UPDATED_PASSWORD_HASH = "BBBBBBBBBB";

  private static final String DEFAULT_PHONE = "AAAAAAAAAA";
  private static final String UPDATED_PHONE = "BBBBBBBBBB";

  private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
  private static final String UPDATED_EMAIL = "BBBBBBBBBB";

  private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
  private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

  private static final Boolean DEFAULT_ACTIVATED = false;
  private static final Boolean UPDATED_ACTIVATED = true;

  private static final String DEFAULT_LANG_KEY = "AAAAA";
  private static final String UPDATED_LANG_KEY = "BBBBB";

  private static final String DEFAULT_ACTIVATION_KEY = "AAAAA";
  private static final String UPDATED_ACTIVATION_KEY = "BBBBB";

  private static final String DEFAULT_RESET_KEY = "AAAAA";
  private static final String UPDATED_RESET_KEY = "BBBBB";

  private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
  private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

  private static final ZonedDateTime DEFAULT_RESET_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
  private static final ZonedDateTime UPDATED_RESET_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

  private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
  private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

  private static final ZonedDateTime DEFAULT_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
  private static final ZonedDateTime UPDATED_LAST_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerService customerService;

  @Autowired
  private CustomerSearchRepository customerSearchRepository;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restCustomerMockMvc;

  private Customer customer;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final CustomerResource customerResource = new CustomerResource(customerService);
    this.restCustomerMockMvc = MockMvcBuilders.standaloneSetup(customerResource).setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static Customer createEntity(EntityManager em) {
    Customer customer = new Customer().firstName(DEFAULT_FIRST_NAME).midleName(DEFAULT_MIDLE_NAME).lastName(DEFAULT_LAST_NAME).login(DEFAULT_LOGIN)
        .passwordHash(DEFAULT_PASSWORD_HASH).phone(DEFAULT_PHONE).email(DEFAULT_EMAIL).imageUrl(DEFAULT_IMAGE_URL).activated(DEFAULT_ACTIVATED).langKey(DEFAULT_LANG_KEY)
        .activationKey(DEFAULT_ACTIVATION_KEY).resetKey(DEFAULT_RESET_KEY).createdDate(DEFAULT_CREATED_DATE).resetDate(DEFAULT_RESET_DATE).lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
        .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    // Add required entity
    Company company = CompanyResourceIntTest.createEntity(em);
    em.persist(company);
    em.flush();
    customer.getCompanies().add(company);
    return customer;
  }

  @Before
  public void initTest() {
    customerSearchRepository.deleteAll();
    customer = createEntity(em);
  }

  @Test
  @Transactional
  public void createCustomer() throws Exception {
    int databaseSizeBeforeCreate = customerRepository.findAll().size();

    // Create the Customer
    restCustomerMockMvc.perform(post("/api/customers").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(customer)))
        .andExpect(status().isCreated());

    // Validate the Customer in the database
    List<Customer> customerList = customerRepository.findAll();
    assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
    Customer testCustomer = customerList.get(customerList.size() - 1);
    assertThat(testCustomer.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
    assertThat(testCustomer.getMidleName()).isEqualTo(DEFAULT_MIDLE_NAME);
    assertThat(testCustomer.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    assertThat(testCustomer.getLogin()).isEqualTo(DEFAULT_LOGIN);
    assertThat(testCustomer.getPasswordHash()).isEqualTo(DEFAULT_PASSWORD_HASH);
    assertThat(testCustomer.getPhone()).isEqualTo(DEFAULT_PHONE);
    assertThat(testCustomer.getEmail()).isEqualTo(DEFAULT_EMAIL);
    assertThat(testCustomer.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
    assertThat(testCustomer.isActivated()).isEqualTo(DEFAULT_ACTIVATED);
    assertThat(testCustomer.getLangKey()).isEqualTo(DEFAULT_LANG_KEY);
    assertThat(testCustomer.getActivationKey()).isEqualTo(DEFAULT_ACTIVATION_KEY);
    assertThat(testCustomer.getResetKey()).isEqualTo(DEFAULT_RESET_KEY);
    assertThat(testCustomer.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    assertThat(testCustomer.getResetDate()).isEqualTo(DEFAULT_RESET_DATE);
    assertThat(testCustomer.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    assertThat(testCustomer.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);

    // Validate the Customer in Elasticsearch
    Customer customerEs = customerSearchRepository.findOne(testCustomer.getId());
    assertThat(customerEs).isEqualToComparingFieldByField(testCustomer);
  }

  @Test
  @Transactional
  public void createCustomerWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = customerRepository.findAll().size();

    // Create the Customer with an existing ID
    customer.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restCustomerMockMvc.perform(post("/api/customers").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(customer)))
        .andExpect(status().isBadRequest());

    // Validate the Customer in the database
    List<Customer> customerList = customerRepository.findAll();
    assertThat(customerList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkFirstNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = customerRepository.findAll().size();
    // set the field null
    customer.setFirstName(null);

    // Create the Customer, which fails.

    restCustomerMockMvc.perform(post("/api/customers").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(customer)))
        .andExpect(status().isBadRequest());

    List<Customer> customerList = customerRepository.findAll();
    assertThat(customerList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkActivatedIsRequired() throws Exception {
    int databaseSizeBeforeTest = customerRepository.findAll().size();
    // set the field null
    customer.setActivated(null);

    // Create the Customer, which fails.

    restCustomerMockMvc.perform(post("/api/customers").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(customer)))
        .andExpect(status().isBadRequest());

    List<Customer> customerList = customerRepository.findAll();
    assertThat(customerList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkCreatedDateIsRequired() throws Exception {
    int databaseSizeBeforeTest = customerRepository.findAll().size();
    // set the field null
    customer.setCreatedDate(null);

    // Create the Customer, which fails.

    restCustomerMockMvc.perform(post("/api/customers").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(customer)))
        .andExpect(status().isBadRequest());

    List<Customer> customerList = customerRepository.findAll();
    assertThat(customerList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLastModifiedByIsRequired() throws Exception {
    int databaseSizeBeforeTest = customerRepository.findAll().size();
    // set the field null
    customer.setLastModifiedBy(null);

    // Create the Customer, which fails.

    restCustomerMockMvc.perform(post("/api/customers").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(customer)))
        .andExpect(status().isBadRequest());

    List<Customer> customerList = customerRepository.findAll();
    assertThat(customerList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLastModifiedDateIsRequired() throws Exception {
    int databaseSizeBeforeTest = customerRepository.findAll().size();
    // set the field null
    customer.setLastModifiedDate(null);

    // Create the Customer, which fails.

    restCustomerMockMvc.perform(post("/api/customers").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(customer)))
        .andExpect(status().isBadRequest());

    List<Customer> customerList = customerRepository.findAll();
    assertThat(customerList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllCustomers() throws Exception {
    // Initialize the database
    customerRepository.saveAndFlush(customer);

    // Get all the customerList
    restCustomerMockMvc.perform(get("/api/customers?sort=id,desc")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue()))).andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
        .andExpect(jsonPath("$.[*].midleName").value(hasItem(DEFAULT_MIDLE_NAME.toString()))).andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
        .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString()))).andExpect(jsonPath("$.[*].passwordHash").value(hasItem(DEFAULT_PASSWORD_HASH.toString())))
        .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString()))).andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
        .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString()))).andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
        .andExpect(jsonPath("$.[*].langKey").value(hasItem(DEFAULT_LANG_KEY.toString())))
        .andExpect(jsonPath("$.[*].activationKey").value(hasItem(DEFAULT_ACTIVATION_KEY.toString())))
        .andExpect(jsonPath("$.[*].resetKey").value(hasItem(DEFAULT_RESET_KEY.toString())))
        .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
        .andExpect(jsonPath("$.[*].resetDate").value(hasItem(sameInstant(DEFAULT_RESET_DATE))))
        .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
        .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_DATE))));
  }

  @Test
  @Transactional
  public void getCustomer() throws Exception {
    // Initialize the database
    customerRepository.saveAndFlush(customer);

    // Get the customer
    restCustomerMockMvc.perform(get("/api/customers/{id}", customer.getId())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(customer.getId().intValue())).andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
        .andExpect(jsonPath("$.midleName").value(DEFAULT_MIDLE_NAME.toString())).andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
        .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString())).andExpect(jsonPath("$.passwordHash").value(DEFAULT_PASSWORD_HASH.toString()))
        .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString())).andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
        .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString())).andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
        .andExpect(jsonPath("$.langKey").value(DEFAULT_LANG_KEY.toString())).andExpect(jsonPath("$.activationKey").value(DEFAULT_ACTIVATION_KEY.toString()))
        .andExpect(jsonPath("$.resetKey").value(DEFAULT_RESET_KEY.toString())).andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
        .andExpect(jsonPath("$.resetDate").value(sameInstant(DEFAULT_RESET_DATE))).andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
        .andExpect(jsonPath("$.lastModifiedDate").value(sameInstant(DEFAULT_LAST_MODIFIED_DATE)));
  }

  @Test
  @Transactional
  public void getNonExistingCustomer() throws Exception {
    // Get the customer
    restCustomerMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateCustomer() throws Exception {
    // Initialize the database
    customerService.save(customer);

    int databaseSizeBeforeUpdate = customerRepository.findAll().size();

    // Update the customer
    Customer updatedCustomer = customerRepository.findOne(customer.getId());
    updatedCustomer.firstName(UPDATED_FIRST_NAME).midleName(UPDATED_MIDLE_NAME).lastName(UPDATED_LAST_NAME).login(UPDATED_LOGIN).passwordHash(UPDATED_PASSWORD_HASH)
        .phone(UPDATED_PHONE).email(UPDATED_EMAIL).imageUrl(UPDATED_IMAGE_URL).activated(UPDATED_ACTIVATED).langKey(UPDATED_LANG_KEY).activationKey(UPDATED_ACTIVATION_KEY)
        .resetKey(UPDATED_RESET_KEY).createdDate(UPDATED_CREATED_DATE).resetDate(UPDATED_RESET_DATE).lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
        .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

    restCustomerMockMvc.perform(put("/api/customers").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(updatedCustomer)))
        .andExpect(status().isOk());

    // Validate the Customer in the database
    List<Customer> customerList = customerRepository.findAll();
    assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    Customer testCustomer = customerList.get(customerList.size() - 1);
    assertThat(testCustomer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
    assertThat(testCustomer.getMidleName()).isEqualTo(UPDATED_MIDLE_NAME);
    assertThat(testCustomer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    assertThat(testCustomer.getLogin()).isEqualTo(UPDATED_LOGIN);
    assertThat(testCustomer.getPasswordHash()).isEqualTo(UPDATED_PASSWORD_HASH);
    assertThat(testCustomer.getPhone()).isEqualTo(UPDATED_PHONE);
    assertThat(testCustomer.getEmail()).isEqualTo(UPDATED_EMAIL);
    assertThat(testCustomer.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
    assertThat(testCustomer.isActivated()).isEqualTo(UPDATED_ACTIVATED);
    assertThat(testCustomer.getLangKey()).isEqualTo(UPDATED_LANG_KEY);
    assertThat(testCustomer.getActivationKey()).isEqualTo(UPDATED_ACTIVATION_KEY);
    assertThat(testCustomer.getResetKey()).isEqualTo(UPDATED_RESET_KEY);
    assertThat(testCustomer.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    assertThat(testCustomer.getResetDate()).isEqualTo(UPDATED_RESET_DATE);
    assertThat(testCustomer.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    assertThat(testCustomer.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);

    // Validate the Customer in Elasticsearch
    Customer customerEs = customerSearchRepository.findOne(testCustomer.getId());
    assertThat(customerEs).isEqualToComparingFieldByField(testCustomer);
  }

  @Test
  @Transactional
  public void updateNonExistingCustomer() throws Exception {
    int databaseSizeBeforeUpdate = customerRepository.findAll().size();

    // Create the Customer

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restCustomerMockMvc.perform(put("/api/customers").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(customer)))
        .andExpect(status().isCreated());

    // Validate the Customer in the database
    List<Customer> customerList = customerRepository.findAll();
    assertThat(customerList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteCustomer() throws Exception {
    // Initialize the database
    customerService.save(customer);

    int databaseSizeBeforeDelete = customerRepository.findAll().size();

    // Get the customer
    restCustomerMockMvc.perform(delete("/api/customers/{id}", customer.getId()).accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

    // Validate Elasticsearch is empty
    boolean customerExistsInEs = customerSearchRepository.exists(customer.getId());
    assertThat(customerExistsInEs).isFalse();

    // Validate the database is empty
    List<Customer> customerList = customerRepository.findAll();
    assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void searchCustomer() throws Exception {
    // Initialize the database
    customerService.save(customer);

    // Search the customer
    restCustomerMockMvc.perform(get("/api/_search/customers?query=id:" + customer.getId())).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
        .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString()))).andExpect(jsonPath("$.[*].midleName").value(hasItem(DEFAULT_MIDLE_NAME.toString())))
        .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString()))).andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
        .andExpect(jsonPath("$.[*].passwordHash").value(hasItem(DEFAULT_PASSWORD_HASH.toString()))).andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
        .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString()))).andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
        .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue()))).andExpect(jsonPath("$.[*].langKey").value(hasItem(DEFAULT_LANG_KEY.toString())))
        .andExpect(jsonPath("$.[*].activationKey").value(hasItem(DEFAULT_ACTIVATION_KEY.toString())))
        .andExpect(jsonPath("$.[*].resetKey").value(hasItem(DEFAULT_RESET_KEY.toString())))
        .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
        .andExpect(jsonPath("$.[*].resetDate").value(hasItem(sameInstant(DEFAULT_RESET_DATE))))
        .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
        .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_DATE))));
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Customer.class);
    Customer customer1 = new Customer();
    customer1.setId(1L);
    Customer customer2 = new Customer();
    customer2.setId(customer1.getId());
    assertThat(customer1).isEqualTo(customer2);
    customer2.setId(2L);
    assertThat(customer1).isNotEqualTo(customer2);
    customer1.setId(null);
    assertThat(customer1).isNotEqualTo(customer2);
  }

}
