package com.mycompany.mystaff.repository.search;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.mycompany.mystaff.domain.Location;

/**
 * Spring Data Elasticsearch repository for the Location entity.
 */
public interface LocationSearchRepository extends ElasticsearchRepository<Location, Long> {

	@Query("{" + 
			"	\"query\": {" + 
			"		\"bool\": {" + 
			"			\"should\": [{" + 
			"				\"term\": {" + 
			"					\"name\": \"?0\"" + 
			"				}" + 
			"			}, {" + 
			"				\"nested\": {" + 
			"					\"path\": \"company\"," + 
			"					\"query\": {" + 
			"						\"bool\": {" + 
			"							\"must\": [{" + 
			"								\"match\": {" + 
			"									\"company.id\": \"?1\"" + 
			"								}" + 
			"							}]" + 
			"						}" + 
			"					}" + 
			"				}" + 
			"			}]" + 
			"		}" + 
			"	}" + 
			"}")
	List<Location> findByCompanyId(String term, Long companyId);
	
    void deleteByIdAndCompanyId(Long id, Long companyId);

}
