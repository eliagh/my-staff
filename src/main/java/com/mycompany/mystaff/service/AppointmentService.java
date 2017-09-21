package com.mycompany.mystaff.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.mystaff.domain.Appointment;
import com.mycompany.mystaff.repository.AppointmentRepository;
import com.mycompany.mystaff.repository.search.AppointmentSearchRepository;

/**
 * Service Implementation for managing Appointment.
 */
@Service
@Transactional
public class AppointmentService {

  private final Logger log = LoggerFactory.getLogger(AppointmentService.class);

  private final AppointmentRepository appointmentRepository;

  private final AppointmentSearchRepository appointmentSearchRepository;

  public AppointmentService(AppointmentRepository appointmentRepository, AppointmentSearchRepository appointmentSearchRepository) {
    this.appointmentRepository = appointmentRepository;
    this.appointmentSearchRepository = appointmentSearchRepository;
  }

  /**
   * Save a appointment.
   *
   * @param appointment the entity to save
   * @return the persisted entity
   */
  public Appointment save(Appointment appointment) {
    log.debug("Request to save Appointment : {}", appointment);
    Appointment result = appointmentRepository.save(appointment);
    appointmentSearchRepository.save(result);
    return result;
  }

  /**
   * Get all the appointments.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public Page<Appointment> findAll(Pageable pageable) {
    log.debug("Request to get all Appointments");
    return appointmentRepository.findAll(pageable);
  }

  /**
   * Get one appointment by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public Appointment findOne(Long id) {
    log.debug("Request to get Appointment : {}", id);
    return appointmentRepository.findOne(id);
  }

  /**
   * Delete the appointment by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete Appointment : {}", id);
    appointmentRepository.delete(id);
    appointmentSearchRepository.delete(id);
  }

  /**
   * Search for the appointment corresponding to the query.
   *
   * @param query the query of the search
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public Page<Appointment> search(String query, Pageable pageable) {
    log.debug("Request to search for a page of Appointments for query {}", query);
    Page<Appointment> result = appointmentSearchRepository.search(queryStringQuery(query), pageable);
    return result;
  }

}
