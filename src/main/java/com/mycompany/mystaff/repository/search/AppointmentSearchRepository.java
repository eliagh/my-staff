package com.mycompany.mystaff.repository.search;

import com.mycompany.mystaff.domain.Appointment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Appointment entity.
 */
public interface AppointmentSearchRepository extends ElasticsearchRepository<Appointment, Long> {
}
