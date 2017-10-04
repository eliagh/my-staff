package com.mycompany.mystaff.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.LinkedList;
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
import com.mycompany.mystaff.service.dto.CategoryDTO;
import com.mycompany.mystaff.service.mapper.CategoryMapper;

/**
 * Service Implementation for managing Category.
 */
@Service
@Transactional
public class CategoryService {

  private final Logger log = LoggerFactory.getLogger(CategoryService.class);

  private final CategoryRepository categoryRepository;

  private final CategoryMapper categoryMapper;

  private final CategorySearchRepository categorySearchRepository;

  public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper, CategorySearchRepository categorySearchRepository) {
    this.categoryRepository = categoryRepository;
    this.categoryMapper = categoryMapper;
    this.categorySearchRepository = categorySearchRepository;
  }

  /**
   * Save a category.
   *
   * @param categoryDTO the entity to save
   * @return the persisted entity
   */
  public CategoryDTO save(CategoryDTO categoryDTO, Long companyId) {
    log.debug("Request to save Category : {}, companyId : {}", categoryDTO, companyId);

    Category category = categoryMapper.toEntity(categoryDTO);
    category = categoryRepository.save(category);
    category.setCompanyId(companyId);
    CategoryDTO result = categoryMapper.toDto(category);
    categorySearchRepository.save(category);
    return result;
  }

  /**
   * Get all the categories.
   *
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<CategoryDTO> findByCompanyId(Long companyId) {
    log.debug("Request to get all Categories by CompanyId : {}", companyId);

    return categoryRepository.findByCompanyId(companyId).stream().map(categoryMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one category by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public CategoryDTO findByIdAndCompanyID(Long id, Long companyId) {
    log.debug("Request to get CategoryId : {}, CompanyId : {}", id, companyId);

    Category category = categoryRepository.findByIdAndCompanyID(id, companyId);
    return categoryMapper.toDto(category);
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
  public List<CategoryDTO> search(String query, Long companyId) {
    log.debug("Request to search Categories for query {}", query);

    return StreamSupport.stream(categorySearchRepository.search(queryStringQuery(query)).spliterator(), false).map(categoryMapper::toDto).collect(Collectors.toList());
  }

}
