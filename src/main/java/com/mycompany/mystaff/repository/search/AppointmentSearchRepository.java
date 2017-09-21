package com.mycompany.mystaff.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.mycompany.mystaff.domain.Appointment;

/**
 * Spring Data Elasticsearch repository for the Appointment entity.
 */
public interface AppointmentSearchRepository extends ElasticsearchRepository<Appointment, Long> {

}
