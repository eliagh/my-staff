package com.mycompany.mystaff.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.mycompany.mystaff.domain.Location;

/**
 * Spring Data Elasticsearch repository for the Location entity.
 */
public interface LocationSearchRepository extends ElasticsearchRepository<Location, Long> {

    void deleteByIdAndCompanyId(Long id, Long companyId);

}
