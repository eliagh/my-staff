package com.mycompany.mystaff.service.mapper;

import org.mapstruct.Mapper;

import com.mycompany.mystaff.domain.Category;
import com.mycompany.mystaff.service.dto.CategoryDTO;

/**
 * Mapper for the entity Category and its DTO CategoryDTO.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class, })
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {

  CategoryDTO toDto(Category category);

  Category toEntity(CategoryDTO categoryDTO);

  default Category fromId(Long id) {
    if (id == null) {
      return null;
    }
    Category category = new Category();
    category.setId(id);
    return category;
  }

}
