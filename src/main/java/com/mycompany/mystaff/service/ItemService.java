package com.mycompany.mystaff.service;

import com.mycompany.mystaff.domain.Item;
import com.mycompany.mystaff.repository.ItemRepository;
import com.mycompany.mystaff.repository.search.ItemSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Item.
 */
@Service
@Transactional
public class ItemService {

    private final Logger log = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepository;

    private final ItemSearchRepository itemSearchRepository;

    public ItemService(ItemRepository itemRepository, ItemSearchRepository itemSearchRepository) {
        this.itemRepository = itemRepository;
        this.itemSearchRepository = itemSearchRepository;
    }

    /**
     * Save a item.
     *
     * @param item the entity to save
     * @return the persisted entity
     */
    public Item save(Item item) {
        log.debug("Request to save Item : {}", item);
        Item result = itemRepository.save(item);
        itemSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the items.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Item> findAll(Pageable pageable) {
        log.debug("Request to get all Items");
        return itemRepository.findAll(pageable);
    }

    /**
     *  Get one item by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Item findOne(Long id) {
        log.debug("Request to get Item : {}", id);
        return itemRepository.findOne(id);
    }

    /**
     *  Delete the  item by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Item : {}", id);
        itemRepository.delete(id);
        itemSearchRepository.delete(id);
    }

    /**
     * Search for the item corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Item> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Items for query {}", query);
        Page<Item> result = itemSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
