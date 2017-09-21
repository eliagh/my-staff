package com.mycompany.mystaff.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
import com.mycompany.mystaff.domain.Appointment;
import com.mycompany.mystaff.service.AppointmentService;
import com.mycompany.mystaff.web.rest.util.HeaderUtil;
import com.mycompany.mystaff.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Appointment.
 */
@RestController
@RequestMapping("/api")
public class AppointmentResource {

  private final Logger log = LoggerFactory.getLogger(AppointmentResource.class);

  private static final String ENTITY_NAME = "appointment";

  private final AppointmentService appointmentService;

  public AppointmentResource(AppointmentService appointmentService) {
    this.appointmentService = appointmentService;
  }

  /**
   * POST /appointments : Create a new appointment.
   *
   * @param appointment the appointment to create
   * @return the ResponseEntity with status 201 (Created) and with body the new appointment, or with
   *         status 400 (Bad Request) if the appointment has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/appointments")
  @Timed
  public ResponseEntity<Appointment> createAppointment(@Valid @RequestBody Appointment appointment) throws URISyntaxException {
    log.debug("REST request to save Appointment : {}", appointment);
    if (appointment.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new appointment cannot already have an ID")).body(null);
    }
    Appointment result = appointmentService.save(appointment);
    return ResponseEntity.created(new URI("/api/appointments/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT /appointments : Updates an existing appointment.
   *
   * @param appointment the appointment to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated appointment, or with
   *         status 400 (Bad Request) if the appointment is not valid, or with status 500 (Internal
   *         Server Error) if the appointment couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/appointments")
  @Timed
  public ResponseEntity<Appointment> updateAppointment(@Valid @RequestBody Appointment appointment) throws URISyntaxException {
    log.debug("REST request to update Appointment : {}", appointment);
    if (appointment.getId() == null) {
      return createAppointment(appointment);
    }
    Appointment result = appointmentService.save(appointment);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, appointment.getId().toString())).body(result);
  }

  /**
   * GET /appointments : get all the appointments.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of appointments in body
   */
  @GetMapping("/appointments")
  @Timed
  public ResponseEntity<List<Appointment>> getAllAppointments(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of Appointments");
    Page<Appointment> page = appointmentService.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/appointments");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET /appointments/:id : get the "id" appointment.
   *
   * @param id the id of the appointment to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the appointment, or with status
   *         404 (Not Found)
   */
  @GetMapping("/appointments/{id}")
  @Timed
  public ResponseEntity<Appointment> getAppointment(@PathVariable Long id) {
    log.debug("REST request to get Appointment : {}", id);
    Appointment appointment = appointmentService.findOne(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(appointment));
  }

  /**
   * DELETE /appointments/:id : delete the "id" appointment.
   *
   * @param id the id of the appointment to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/appointments/{id}")
  @Timed
  public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
    log.debug("REST request to delete Appointment : {}", id);
    appointmentService.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * SEARCH /_search/appointments?query=:query : search for the appointment corresponding to the
   * query.
   *
   * @param query the query of the appointment search
   * @param pageable the pagination information
   * @return the result of the search
   */
  @GetMapping("/_search/appointments")
  @Timed
  public ResponseEntity<List<Appointment>> searchAppointments(@RequestParam String query, @ApiParam Pageable pageable) {
    log.debug("REST request to search for a page of Appointments for query {}", query);
    Page<Appointment> page = appointmentService.search(query, pageable);
    HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/appointments");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

}
