package com.mycompany.mystaff.repository.search;

import com.mycompany.mystaff.domain.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Item entity.
 */
public interface ItemSearchRepository extends ElasticsearchRepository<Item, Long> {
}
