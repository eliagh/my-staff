package com.mycompany.mystaff.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.mycompany.mystaff.domain.Activity;

/**
 * Spring Data Elasticsearch repository for the Activity entity.
 */
public interface ActivitySearchRepository extends ElasticsearchRepository<Activity, Long> {

}
