package com.mycompany.mystaff.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.mycompany.mystaff.domain.Inventory;

/**
 * Spring Data Elasticsearch repository for the Inventory entity.
 */
public interface InventorySearchRepository extends ElasticsearchRepository<Inventory, Long> {

}
