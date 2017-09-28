package com.mycompany.mystaff.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.mystaff.domain.File;

/**
 * Spring Data Elasticsearch repository for the File entity.
 */
public interface FileSearchRepository extends ElasticsearchRepository<File, Long> {

  @Modifying
  @Transactional
  @Query("DELETE FROM Category c WHERE (id = :id) AND (company_id = :companyId)")
  void deleteByIdAndCompanyId(@Param("id") Long id, @Param("companyId") Long companyId);

}
