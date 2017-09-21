package com.mycompany.mystaff.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.mycompany.mystaff.domain.Item;

/**
 * Spring Data Elasticsearch repository for the Item entity.
 */
public interface ItemSearchRepository extends ElasticsearchRepository<Item, Long> {

}
