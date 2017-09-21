package com.mycompany.mystaff.repository.search;

import com.mycompany.mystaff.domain.Activity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Activity entity.
 */
public interface ActivitySearchRepository extends ElasticsearchRepository<Activity, Long> {
}
