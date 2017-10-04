package com.mycompany.mystaff.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.mystaff.domain.Activity;
import com.mycompany.mystaff.repository.ActivityRepository;
import com.mycompany.mystaff.repository.search.ActivitySearchRepository;

/**
 * Service Implementation for managing Activity.
 */
@Service
@Transactional
public class ActivityService {

  private final Logger log = LoggerFactory.getLogger(ActivityService.class);

  private final ActivityRepository activityRepository;

  private final ActivitySearchRepository activitySearchRepository;

  public ActivityService(ActivityRepository activityRepository, ActivitySearchRepository activitySearchRepository) {
    this.activityRepository = activityRepository;
    this.activitySearchRepository = activitySearchRepository;
  }

  /**
   * Save a activity.
   *
   * @param activity the entity to save
   * @return the persisted entity
   */
  public Activity save(Activity activity) {
    log.debug("Request to save Activity : {}", activity);

    Activity result = activityRepository.save(activity);
    activitySearchRepository.save(result);

    return result;
  }

  /**
   * Get all the activities.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public Page<Activity> findAll(Pageable pageable) {
    log.debug("Request to get all Activities");

    return activityRepository.findAll(pageable);
  }

  /**
   * Get one activity by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public Activity findOne(Long id) {
    log.debug("Request to get Activity : {}", id);

    return activityRepository.findOneWithEagerRelationships(id);
  }

  /**
   * Delete the activity by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete Activity : {}", id);

    activityRepository.delete(id);
    activitySearchRepository.delete(id);
  }

  /**
   * Search for the activity corresponding to the query.
   *
   * @param query the query of the search
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public Page<Activity> search(String query, Pageable pageable) {
    log.debug("Request to search for a page of Activities for query {}", query);

    Page<Activity> result = activitySearchRepository.search(queryStringQuery(query), pageable);
    return result;
  }

}
