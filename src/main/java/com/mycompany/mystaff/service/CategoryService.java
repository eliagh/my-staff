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
    public List<Category> findByCompanyId(Long companyId) {
        log.debug("Request to get all Categories by CompanyId : {}", companyId);

        return categoryRepository.findByCompanyId(companyId);
  }

  /**
   * Get one category by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
    public Category findByIdAndCompanyID(Long id, Long companyId) {
        log.debug("Request to get CategoryId : {}, CompanyId : {}", id, companyId);

        return categoryRepository.findByIdAndCompanyID(id, companyId);
  }

  /**
   * Delete the category by id.
   *
   * @param id the id of the entity
   */
    public void deleteByIdAndCompanyId(Long id, Long companyId) {
        log.debug("Request to delete CategoryId : {}, CompanyId : {}", id, companyId);

        categoryRepository.deleteByIdAndCompanyId(id, companyId);
        categorySearchRepository.deleteByIdAndCompanyId(id, companyId);
  }

  /**
   * Search for the category corresponding to the query.
   *
   * @param query the query of the search
   * @return the list of entities
   */
  @Transactional(readOnly = true)
    public List<Category> search(String query, Long companyId) {
    log.debug("Request to search Categories for query {}", query);

    return StreamSupport.stream(categorySearchRepository.search(queryStringQuery(query)).spliterator(), false).collect(Collectors.toList());
  }

}
