package com.mycompany.mystaff.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.mycompany.mystaff.domain.File;

/**
 * Spring Data Elasticsearch repository for the File entity.
 */
public interface FileSearchRepository extends ElasticsearchRepository<File, Long> {

}
