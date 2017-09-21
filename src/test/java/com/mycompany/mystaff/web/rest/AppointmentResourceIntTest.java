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
import com.mycompany.mystaff.domain.Activity;
import com.mycompany.mystaff.domain.Appointment;
import com.mycompany.mystaff.domain.Customer;
import com.mycompany.mystaff.domain.Location;
import com.mycompany.mystaff.domain.User;
import com.mycompany.mystaff.repository.AppointmentRepository;
import com.mycompany.mystaff.repository.search.AppointmentSearchRepository;
import com.mycompany.mystaff.service.AppointmentService;
import com.mycompany.mystaff.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the AppointmentResource REST controller.
 *
 * @see AppointmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MystaffApp.class)
public class AppointmentResourceIntTest {

  private static final ZonedDateTime DEFAULT_WHEN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
  private static final ZonedDateTime UPDATED_WHEN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";

  private static final Boolean DEFAULT_IS_RECURRING = false;
  private static final Boolean UPDATED_IS_RECURRING = true;

  private static final Boolean DEFAULT_IS_FLEXIBLE = false;
  private static final Boolean UPDATED_IS_FLEXIBLE = true;

  private static final String DEFAULT_NOTES = "AAAAAAAAAA";
  private static final String UPDATED_NOTES = "BBBBBBBBBB";

  private static final String DEFAULT_REMINDER = "AAAAAAAAAA";
  private static final String UPDATED_REMINDER = "BBBBBBBBBB";

  @Autowired
  private AppointmentRepository appointmentRepository;

  @Autowired
  private AppointmentService appointmentService;

  @Autowired
  private AppointmentSearchRepository appointmentSearchRepository;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restAppointmentMockMvc;

  private Appointment appointment;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final AppointmentResource appointmentResource = new AppointmentResource(appointmentService);
    this.restAppointmentMockMvc = MockMvcBuilders.standaloneSetup(appointmentResource).setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static Appointment createEntity(EntityManager em) {
    Appointment appointment = new Appointment().when(DEFAULT_WHEN).label(DEFAULT_LABEL).isRecurring(DEFAULT_IS_RECURRING).isFlexible(DEFAULT_IS_FLEXIBLE).notes(DEFAULT_NOTES)
        .reminder(DEFAULT_REMINDER);
    // Add required entity
    Customer customer = CustomerResourceIntTest.createEntity(em);
    em.persist(customer);
    em.flush();
    appointment.setCustomer(customer);
    // Add required entity
    Activity activityBooked = ActivityResourceIntTest.createEntity(em);
    em.persist(activityBooked);
    em.flush();
    appointment.setActivityBooked(activityBooked);
    // Add required entity
    Location location = LocationResourceIntTest.createEntity(em);
    em.persist(location);
    em.flush();
    appointment.setLocation(location);
    // Add required entity
    User provider = UserResourceIntTest.createEntity(em);
    em.persist(provider);
    em.flush();
    appointment.setProvider(provider);
    return appointment;
  }

  @Before
  public void initTest() {
    appointmentSearchRepository.deleteAll();
    appointment = createEntity(em);
  }

  @Test
  @Transactional
  public void createAppointment() throws Exception {
    int databaseSizeBeforeCreate = appointmentRepository.findAll().size();

    // Create the Appointment
    restAppointmentMockMvc.perform(post("/api/appointments").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(appointment)))
        .andExpect(status().isCreated());

    // Validate the Appointment in the database
    List<Appointment> appointmentList = appointmentRepository.findAll();
    assertThat(appointmentList).hasSize(databaseSizeBeforeCreate + 1);
    Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
    assertThat(testAppointment.getWhen()).isEqualTo(DEFAULT_WHEN);
    assertThat(testAppointment.getLabel()).isEqualTo(DEFAULT_LABEL);
    assertThat(testAppointment.isIsRecurring()).isEqualTo(DEFAULT_IS_RECURRING);
    assertThat(testAppointment.isIsFlexible()).isEqualTo(DEFAULT_IS_FLEXIBLE);
    assertThat(testAppointment.getNotes()).isEqualTo(DEFAULT_NOTES);
    assertThat(testAppointment.getReminder()).isEqualTo(DEFAULT_REMINDER);

    // Validate the Appointment in Elasticsearch
    Appointment appointmentEs = appointmentSearchRepository.findOne(testAppointment.getId());
    assertThat(appointmentEs).isEqualToComparingFieldByField(testAppointment);
  }

  @Test
  @Transactional
  public void createAppointmentWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = appointmentRepository.findAll().size();

    // Create the Appointment with an existing ID
    appointment.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restAppointmentMockMvc.perform(post("/api/appointments").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(appointment)))
        .andExpect(status().isBadRequest());

    // Validate the Appointment in the database
    List<Appointment> appointmentList = appointmentRepository.findAll();
    assertThat(appointmentList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkWhenIsRequired() throws Exception {
    int databaseSizeBeforeTest = appointmentRepository.findAll().size();
    // set the field null
    appointment.setWhen(null);

    // Create the Appointment, which fails.

    restAppointmentMockMvc.perform(post("/api/appointments").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(appointment)))
        .andExpect(status().isBadRequest());

    List<Appointment> appointmentList = appointmentRepository.findAll();
    assertThat(appointmentList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkIsRecurringIsRequired() throws Exception {
    int databaseSizeBeforeTest = appointmentRepository.findAll().size();
    // set the field null
    appointment.setIsRecurring(null);

    // Create the Appointment, which fails.

    restAppointmentMockMvc.perform(post("/api/appointments").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(appointment)))
        .andExpect(status().isBadRequest());

    List<Appointment> appointmentList = appointmentRepository.findAll();
    assertThat(appointmentList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkIsFlexibleIsRequired() throws Exception {
    int databaseSizeBeforeTest = appointmentRepository.findAll().size();
    // set the field null
    appointment.setIsFlexible(null);

    // Create the Appointment, which fails.

    restAppointmentMockMvc.perform(post("/api/appointments").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(appointment)))
        .andExpect(status().isBadRequest());

    List<Appointment> appointmentList = appointmentRepository.findAll();
    assertThat(appointmentList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllAppointments() throws Exception {
    // Initialize the database
    appointmentRepository.saveAndFlush(appointment);

    // Get all the appointmentList
    restAppointmentMockMvc.perform(get("/api/appointments?sort=id,desc")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(appointment.getId().intValue()))).andExpect(jsonPath("$.[*].when").value(hasItem(sameInstant(DEFAULT_WHEN))))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString()))).andExpect(jsonPath("$.[*].isRecurring").value(hasItem(DEFAULT_IS_RECURRING.booleanValue())))
        .andExpect(jsonPath("$.[*].isFlexible").value(hasItem(DEFAULT_IS_FLEXIBLE.booleanValue()))).andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
        .andExpect(jsonPath("$.[*].reminder").value(hasItem(DEFAULT_REMINDER.toString())));
  }

  @Test
  @Transactional
  public void getAppointment() throws Exception {
    // Initialize the database
    appointmentRepository.saveAndFlush(appointment);

    // Get the appointment
    restAppointmentMockMvc.perform(get("/api/appointments/{id}", appointment.getId())).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(jsonPath("$.id").value(appointment.getId().intValue()))
        .andExpect(jsonPath("$.when").value(sameInstant(DEFAULT_WHEN))).andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
        .andExpect(jsonPath("$.isRecurring").value(DEFAULT_IS_RECURRING.booleanValue())).andExpect(jsonPath("$.isFlexible").value(DEFAULT_IS_FLEXIBLE.booleanValue()))
        .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString())).andExpect(jsonPath("$.reminder").value(DEFAULT_REMINDER.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingAppointment() throws Exception {
    // Get the appointment
    restAppointmentMockMvc.perform(get("/api/appointments/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateAppointment() throws Exception {
    // Initialize the database
    appointmentService.save(appointment);

    int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();

    // Update the appointment
    Appointment updatedAppointment = appointmentRepository.findOne(appointment.getId());
    updatedAppointment.when(UPDATED_WHEN).label(UPDATED_LABEL).isRecurring(UPDATED_IS_RECURRING).isFlexible(UPDATED_IS_FLEXIBLE).notes(UPDATED_NOTES).reminder(UPDATED_REMINDER);

    restAppointmentMockMvc.perform(put("/api/appointments").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(updatedAppointment)))
        .andExpect(status().isOk());

    // Validate the Appointment in the database
    List<Appointment> appointmentList = appointmentRepository.findAll();
    assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
    Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
    assertThat(testAppointment.getWhen()).isEqualTo(UPDATED_WHEN);
    assertThat(testAppointment.getLabel()).isEqualTo(UPDATED_LABEL);
    assertThat(testAppointment.isIsRecurring()).isEqualTo(UPDATED_IS_RECURRING);
    assertThat(testAppointment.isIsFlexible()).isEqualTo(UPDATED_IS_FLEXIBLE);
    assertThat(testAppointment.getNotes()).isEqualTo(UPDATED_NOTES);
    assertThat(testAppointment.getReminder()).isEqualTo(UPDATED_REMINDER);

    // Validate the Appointment in Elasticsearch
    Appointment appointmentEs = appointmentSearchRepository.findOne(testAppointment.getId());
    assertThat(appointmentEs).isEqualToComparingFieldByField(testAppointment);
  }

  @Test
  @Transactional
  public void updateNonExistingAppointment() throws Exception {
    int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();

    // Create the Appointment

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restAppointmentMockMvc.perform(put("/api/appointments").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(appointment)))
        .andExpect(status().isCreated());

    // Validate the Appointment in the database
    List<Appointment> appointmentList = appointmentRepository.findAll();
    assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteAppointment() throws Exception {
    // Initialize the database
    appointmentService.save(appointment);

    int databaseSizeBeforeDelete = appointmentRepository.findAll().size();

    // Get the appointment
    restAppointmentMockMvc.perform(delete("/api/appointments/{id}", appointment.getId()).accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

    // Validate Elasticsearch is empty
    boolean appointmentExistsInEs = appointmentSearchRepository.exists(appointment.getId());
    assertThat(appointmentExistsInEs).isFalse();

    // Validate the database is empty
    List<Appointment> appointmentList = appointmentRepository.findAll();
    assertThat(appointmentList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void searchAppointment() throws Exception {
    // Initialize the database
    appointmentService.save(appointment);

    // Search the appointment
    restAppointmentMockMvc.perform(get("/api/_search/appointments?query=id:" + appointment.getId())).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(jsonPath("$.[*].id").value(hasItem(appointment.getId().intValue())))
        .andExpect(jsonPath("$.[*].when").value(hasItem(sameInstant(DEFAULT_WHEN)))).andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
        .andExpect(jsonPath("$.[*].isRecurring").value(hasItem(DEFAULT_IS_RECURRING.booleanValue())))
        .andExpect(jsonPath("$.[*].isFlexible").value(hasItem(DEFAULT_IS_FLEXIBLE.booleanValue()))).andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
        .andExpect(jsonPath("$.[*].reminder").value(hasItem(DEFAULT_REMINDER.toString())));
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Appointment.class);
    Appointment appointment1 = new Appointment();
    appointment1.setId(1L);
    Appointment appointment2 = new Appointment();
    appointment2.setId(appointment1.getId());
    assertThat(appointment1).isEqualTo(appointment2);
    appointment2.setId(2L);
    assertThat(appointment1).isNotEqualTo(appointment2);
    appointment1.setId(null);
    assertThat(appointment1).isNotEqualTo(appointment2);
  }

}
