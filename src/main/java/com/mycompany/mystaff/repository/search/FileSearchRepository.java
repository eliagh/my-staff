package com.mycompany.mystaff.repository.search;

import com.mycompany.mystaff.domain.File;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the File entity.
 */
public interface FileSearchRepository extends ElasticsearchRepository<File, Long> {
}
