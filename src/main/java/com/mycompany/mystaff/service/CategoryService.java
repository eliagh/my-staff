package com.mycompany.mystaff.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.mystaff.domain.Category;
import com.mycompany.mystaff.repository.CategoryRepository;
import com.mycompany.mystaff.repository.search.CategorySearchRepository;

/**
 * Service Implementation for managing Category.
 */
@Service
@Transactional
public class CategoryService {

  private final Logger log = LoggerFactory.getLogger(CategoryService.class);

  private final CategoryRepository categoryRepository;

  private final CategorySearchRepository categorySearchRepository;

  public CategoryService(CategoryRepository categoryRepository, CategorySearchRepository categorySearchRepository) {
    this.categoryRepository = categoryRepository;
    this.categorySearchRepository = categorySearchRepository;
  }

  /**
   * Save a category.
   *
   * @param category the entity to save
   * @return the persisted entity
   */
  public Category save(Category category) {
    log.debug("Request to save Category : {}", category);
    Category result = categoryRepository.save(category);
    categorySearchRepository.save(result);
    return result;
  }

  /**
   * Get all the categories.
   *
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<Category> findAll() {
    log.debug("Request to get all Categories");
    return categoryRepository.findAll();
  }

  /**
   * Get one category by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public Category findOne(Long id) {
    log.debug("Request to get Category : {}", id);
    return categoryRepository.findOne(id);
  }

  /**
   * Delete the category by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete Category : {}", id);
    categoryRepository.delete(id);
    categorySearchRepository.delete(id);
  }

  /**
   * Search for the category corresponding to the query.
   *
   * @param query the query of the search
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<Category> search(String query) {
    log.debug("Request to search Categories for query {}", query);
    return StreamSupport.stream(categorySearchRepository.search(queryStringQuery(query)).spliterator(), false).collect(Collectors.toList());
  }

}
