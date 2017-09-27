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
import com.mycompany.mystaff.domain.Activity;
import com.mycompany.mystaff.repository.ActivityRepository;
import com.mycompany.mystaff.repository.search.ActivitySearchRepository;
import com.mycompany.mystaff.service.ActivityService;
import com.mycompany.mystaff.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the ActivityResource REST controller.
 *
 * @see ActivityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MystaffApp.class)
public class ActivityResourceIntTest {

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
  private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(2, "1");
  private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
  private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  private static final Double DEFAULT_PRICE = 0D;
  private static final Double UPDATED_PRICE = 1D;

  private static final Integer DEFAULT_DURATION_MINUTES = 1;
  private static final Integer UPDATED_DURATION_MINUTES = 2;

  private static final Integer DEFAULT_PRE_DURATION_MINUTES = 1;
  private static final Integer UPDATED_PRE_DURATION_MINUTES = 2;

  private static final Integer DEFAULT_POST_DURATION_MINUTES = 1;
  private static final Integer UPDATED_POST_DURATION_MINUTES = 2;

  private static final Boolean DEFAULT_IS_PRIVATE = false;
  private static final Boolean UPDATED_IS_PRIVATE = true;

  private static final String DEFAULT_COLOR_CODE = "AAAAAAAAAA";
  private static final String UPDATED_COLOR_CODE = "BBBBBBBBBB";

  private static final Long DEFAULT_CANCELLATION_TIME = 1L;
  private static final Long UPDATED_CANCELLATION_TIME = 2L;

  @Autowired
  private ActivityRepository activityRepository;

  @Autowired
  private ActivityService activityService;

  @Autowired
  private ActivitySearchRepository activitySearchRepository;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restActivityMockMvc;

  private Activity activity;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ActivityResource activityResource = new ActivityResource(activityService);
    this.restActivityMockMvc = MockMvcBuilders.standaloneSetup(activityResource).setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static Activity createEntity(EntityManager em) {
    Activity activity = new Activity().name(DEFAULT_NAME).logo(DEFAULT_LOGO).logoContentType(DEFAULT_LOGO_CONTENT_TYPE).description(DEFAULT_DESCRIPTION).price(DEFAULT_PRICE)
        .durationMinutes(DEFAULT_DURATION_MINUTES).preDurationMinutes(DEFAULT_PRE_DURATION_MINUTES).postDurationMinutes(DEFAULT_POST_DURATION_MINUTES).isPrivate(DEFAULT_IS_PRIVATE)
        .colorCode(DEFAULT_COLOR_CODE).cancellationTime(DEFAULT_CANCELLATION_TIME);
    return activity;
  }

  @Before
  public void initTest() {
    activitySearchRepository.deleteAll();
    activity = createEntity(em);
  }

  @Test
  @Transactional
  public void createActivity() throws Exception {
    int databaseSizeBeforeCreate = activityRepository.findAll().size();

    // Create the Activity
    restActivityMockMvc.perform(post("/api/activities").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(activity)))
        .andExpect(status().isCreated());

    // Validate the Activity in the database
    List<Activity> activityList = activityRepository.findAll();
    assertThat(activityList).hasSize(databaseSizeBeforeCreate + 1);
    Activity testActivity = activityList.get(activityList.size() - 1);
    assertThat(testActivity.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testActivity.getLogo()).isEqualTo(DEFAULT_LOGO);
    assertThat(testActivity.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
    assertThat(testActivity.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testActivity.getPrice()).isEqualTo(DEFAULT_PRICE);
    assertThat(testActivity.getDurationMinutes()).isEqualTo(DEFAULT_DURATION_MINUTES);
    assertThat(testActivity.getPreDurationMinutes()).isEqualTo(DEFAULT_PRE_DURATION_MINUTES);
    assertThat(testActivity.getPostDurationMinutes()).isEqualTo(DEFAULT_POST_DURATION_MINUTES);
    assertThat(testActivity.isIsPrivate()).isEqualTo(DEFAULT_IS_PRIVATE);
    assertThat(testActivity.getColorCode()).isEqualTo(DEFAULT_COLOR_CODE);
    assertThat(testActivity.getCancellationTime()).isEqualTo(DEFAULT_CANCELLATION_TIME);

    // Validate the Activity in Elasticsearch
    Activity activityEs = activitySearchRepository.findOne(testActivity.getId());
    assertThat(activityEs).isEqualToComparingFieldByField(testActivity);
  }

  @Test
  @Transactional
  public void createActivityWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = activityRepository.findAll().size();

    // Create the Activity with an existing ID
    activity.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restActivityMockMvc.perform(post("/api/activities").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(activity)))
        .andExpect(status().isBadRequest());

    // Validate the Activity in the database
    List<Activity> activityList = activityRepository.findAll();
    assertThat(activityList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = activityRepository.findAll().size();
    // set the field null
    activity.setName(null);

    // Create the Activity, which fails.

    restActivityMockMvc.perform(post("/api/activities").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(activity)))
        .andExpect(status().isBadRequest());

    List<Activity> activityList = activityRepository.findAll();
    assertThat(activityList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkDurationMinutesIsRequired() throws Exception {
    int databaseSizeBeforeTest = activityRepository.findAll().size();
    // set the field null
    activity.setDurationMinutes(null);

    // Create the Activity, which fails.

    restActivityMockMvc.perform(post("/api/activities").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(activity)))
        .andExpect(status().isBadRequest());

    List<Activity> activityList = activityRepository.findAll();
    assertThat(activityList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkIsPrivateIsRequired() throws Exception {
    int databaseSizeBeforeTest = activityRepository.findAll().size();
    // set the field null
    activity.setIsPrivate(null);

    // Create the Activity, which fails.

    restActivityMockMvc.perform(post("/api/activities").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(activity)))
        .andExpect(status().isBadRequest());

    List<Activity> activityList = activityRepository.findAll();
    assertThat(activityList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkColorCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = activityRepository.findAll().size();
    // set the field null
    activity.setColorCode(null);

    // Create the Activity, which fails.

    restActivityMockMvc.perform(post("/api/activities").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(activity)))
        .andExpect(status().isBadRequest());

    List<Activity> activityList = activityRepository.findAll();
    assertThat(activityList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllActivities() throws Exception {
    // Initialize the database
    activityRepository.saveAndFlush(activity);

    // Get all the activityList
    restActivityMockMvc.perform(get("/api/activities?sort=id,desc")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(activity.getId().intValue()))).andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
        .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
        .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString()))).andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
        .andExpect(jsonPath("$.[*].durationMinutes").value(hasItem(DEFAULT_DURATION_MINUTES)))
        .andExpect(jsonPath("$.[*].preDurationMinutes").value(hasItem(DEFAULT_PRE_DURATION_MINUTES)))
        .andExpect(jsonPath("$.[*].postDurationMinutes").value(hasItem(DEFAULT_POST_DURATION_MINUTES)))
        .andExpect(jsonPath("$.[*].isPrivate").value(hasItem(DEFAULT_IS_PRIVATE.booleanValue())))
        .andExpect(jsonPath("$.[*].colorCode").value(hasItem(DEFAULT_COLOR_CODE.toString())))
        .andExpect(jsonPath("$.[*].cancellationTime").value(hasItem(DEFAULT_CANCELLATION_TIME.intValue())));
  }

  @Test
  @Transactional
  public void getActivity() throws Exception {
    // Initialize the database
    activityRepository.saveAndFlush(activity);

    // Get the activity
    restActivityMockMvc.perform(get("/api/activities/{id}", activity.getId())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(activity.getId().intValue())).andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
        .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE)).andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)))
        .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString())).andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
        .andExpect(jsonPath("$.durationMinutes").value(DEFAULT_DURATION_MINUTES)).andExpect(jsonPath("$.preDurationMinutes").value(DEFAULT_PRE_DURATION_MINUTES))
        .andExpect(jsonPath("$.postDurationMinutes").value(DEFAULT_POST_DURATION_MINUTES)).andExpect(jsonPath("$.isPrivate").value(DEFAULT_IS_PRIVATE.booleanValue()))
        .andExpect(jsonPath("$.colorCode").value(DEFAULT_COLOR_CODE.toString())).andExpect(jsonPath("$.cancellationTime").value(DEFAULT_CANCELLATION_TIME.intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingActivity() throws Exception {
    // Get the activity
    restActivityMockMvc.perform(get("/api/activities/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateActivity() throws Exception {
    // Initialize the database
    activityService.save(activity);

    int databaseSizeBeforeUpdate = activityRepository.findAll().size();

    // Update the activity
    Activity updatedActivity = activityRepository.findOne(activity.getId());
    updatedActivity.name(UPDATED_NAME).logo(UPDATED_LOGO).logoContentType(UPDATED_LOGO_CONTENT_TYPE).description(UPDATED_DESCRIPTION).price(UPDATED_PRICE)
        .durationMinutes(UPDATED_DURATION_MINUTES).preDurationMinutes(UPDATED_PRE_DURATION_MINUTES).postDurationMinutes(UPDATED_POST_DURATION_MINUTES).isPrivate(UPDATED_IS_PRIVATE)
        .colorCode(UPDATED_COLOR_CODE).cancellationTime(UPDATED_CANCELLATION_TIME);

    restActivityMockMvc.perform(put("/api/activities").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(updatedActivity)))
        .andExpect(status().isOk());

    // Validate the Activity in the database
    List<Activity> activityList = activityRepository.findAll();
    assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
    Activity testActivity = activityList.get(activityList.size() - 1);
    assertThat(testActivity.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testActivity.getLogo()).isEqualTo(UPDATED_LOGO);
    assertThat(testActivity.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
    assertThat(testActivity.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    assertThat(testActivity.getPrice()).isEqualTo(UPDATED_PRICE);
    assertThat(testActivity.getDurationMinutes()).isEqualTo(UPDATED_DURATION_MINUTES);
    assertThat(testActivity.getPreDurationMinutes()).isEqualTo(UPDATED_PRE_DURATION_MINUTES);
    assertThat(testActivity.getPostDurationMinutes()).isEqualTo(UPDATED_POST_DURATION_MINUTES);
    assertThat(testActivity.isIsPrivate()).isEqualTo(UPDATED_IS_PRIVATE);
    assertThat(testActivity.getColorCode()).isEqualTo(UPDATED_COLOR_CODE);
    assertThat(testActivity.getCancellationTime()).isEqualTo(UPDATED_CANCELLATION_TIME);

    // Validate the Activity in Elasticsearch
    Activity activityEs = activitySearchRepository.findOne(testActivity.getId());
    assertThat(activityEs).isEqualToComparingFieldByField(testActivity);
  }

  @Test
  @Transactional
  public void updateNonExistingActivity() throws Exception {
    int databaseSizeBeforeUpdate = activityRepository.findAll().size();

    // Create the Activity

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restActivityMockMvc.perform(put("/api/activities").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(activity)))
        .andExpect(status().isCreated());

    // Validate the Activity in the database
    List<Activity> activityList = activityRepository.findAll();
    assertThat(activityList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteActivity() throws Exception {
    // Initialize the database
    activityService.save(activity);

    int databaseSizeBeforeDelete = activityRepository.findAll().size();

    // Get the activity
    restActivityMockMvc.perform(delete("/api/activities/{id}", activity.getId()).accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

    // Validate Elasticsearch is empty
    boolean activityExistsInEs = activitySearchRepository.exists(activity.getId());
    assertThat(activityExistsInEs).isFalse();

    // Validate the database is empty
    List<Activity> activityList = activityRepository.findAll();
    assertThat(activityList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void searchActivity() throws Exception {
    // Initialize the database
    activityService.save(activity);

    // Search the activity
    restActivityMockMvc.perform(get("/api/_search/activities?query=id:" + activity.getId())).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(jsonPath("$.[*].id").value(hasItem(activity.getId().intValue())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString()))).andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
        .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
        .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString()))).andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
        .andExpect(jsonPath("$.[*].durationMinutes").value(hasItem(DEFAULT_DURATION_MINUTES)))
        .andExpect(jsonPath("$.[*].preDurationMinutes").value(hasItem(DEFAULT_PRE_DURATION_MINUTES)))
        .andExpect(jsonPath("$.[*].postDurationMinutes").value(hasItem(DEFAULT_POST_DURATION_MINUTES)))
        .andExpect(jsonPath("$.[*].isPrivate").value(hasItem(DEFAULT_IS_PRIVATE.booleanValue())))
        .andExpect(jsonPath("$.[*].colorCode").value(hasItem(DEFAULT_COLOR_CODE.toString())))
        .andExpect(jsonPath("$.[*].cancellationTime").value(hasItem(DEFAULT_CANCELLATION_TIME.intValue())));
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Activity.class);
    Activity activity1 = new Activity();
    activity1.setId(1L);
    Activity activity2 = new Activity();
    activity2.setId(activity1.getId());
    assertThat(activity1).isEqualTo(activity2);
    activity2.setId(2L);
    assertThat(activity1).isNotEqualTo(activity2);
    activity1.setId(null);
    assertThat(activity1).isNotEqualTo(activity2);
  }

}
