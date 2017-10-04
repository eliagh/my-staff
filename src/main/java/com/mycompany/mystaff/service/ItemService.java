package com.mycompany.mystaff.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.mystaff.domain.Item;
import com.mycompany.mystaff.repository.ItemRepository;
import com.mycompany.mystaff.repository.search.ItemSearchRepository;
import com.mycompany.mystaff.service.dto.ItemDTO;
import com.mycompany.mystaff.service.mapper.ItemMapper;

/**
 * Service Implementation for managing Item.
 */
@Service
@Transactional
public class ItemService {

  private final Logger log = LoggerFactory.getLogger(ItemService.class);

  private final ItemRepository itemRepository;

  private final ItemMapper itemMapper;

  private final ItemSearchRepository itemSearchRepository;

  public ItemService(ItemRepository itemRepository, ItemMapper itemMapper, ItemSearchRepository itemSearchRepository) {
    this.itemRepository = itemRepository;
    this.itemMapper = itemMapper;
    this.itemSearchRepository = itemSearchRepository;
  }

  /**
   * Save a item.
   *
   * @param itemDTO the entity to save
   * @return the persisted entity
   */
  public ItemDTO save(ItemDTO itemDTO, Long companyId) {
    log.debug("Request to save Item : {}, companyId : {}", itemDTO, companyId);

    Item item = itemMapper.toEntity(itemDTO);
    item.setCompanyId(companyId);
    item = itemRepository.save(item);
    itemSearchRepository.save(item);

    return itemMapper.toDto(item);
  }

  /**
   * Get all the items.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public Page<ItemDTO> findByCompanyId(Pageable pageable, Long companyId) {
    log.debug("Request to get all Items by CompanyId : {}", companyId);

    return itemRepository.findAByCompanyId(pageable, companyId).map(itemMapper::toDto);
  }

  /**
   * Get one item by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public ItemDTO findByIdAndCompanyId(Long id, Long companyId) {
    log.debug("Request to get Item : {}, CompanyId : {}", id, companyId);

    Item item = itemRepository.findByIdAndCompanyId(id, companyId);
    return itemMapper.toDto(item);
  }

  /**
   * Delete the item by id.
   *
   * @param id the id of the entity
   */
  public void deleteByIdAndCompanyId(Long id, Long companyId) {
    log.debug("Request to delete Item : {}, CompanyId : {}", id, companyId);

    itemRepository.deleteByIdAndCompanyId(id, companyId);
    itemSearchRepository.deleteByIdAndCompanyId(id, companyId);
  }

  /**
   * Search for the item corresponding to the query.
   *
   * @param query the query of the search
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public Page<ItemDTO> search(String query, Pageable pageable, Long companyId) {
    log.debug("Request to search for a page of Items for query {}", query);

    Page<Item> result = itemSearchRepository.search(queryStringQuery(query), pageable);
    return result.map(itemMapper::toDto);
  }

}
