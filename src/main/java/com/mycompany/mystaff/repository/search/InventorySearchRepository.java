package com.mycompany.mystaff.repository.search;

import com.mycompany.mystaff.domain.Inventory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Inventory entity.
 */
public interface InventorySearchRepository extends ElasticsearchRepository<Inventory, Long> {
}
