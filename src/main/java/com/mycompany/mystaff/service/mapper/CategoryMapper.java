package com.mycompany.mystaff.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mycompany.mystaff.domain.Category;
import com.mycompany.mystaff.service.dto.CategoryDTO;

/**
 * Mapper for the entity Category and its DTO CategoryDTO.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class, })
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {

  @Mapping(source = "company.id", target = "companyId")
  @Mapping(source = "company.name", target = "companyName")
  CategoryDTO toDto(Category category);

  @Mapping(source = "companyId", target = "company")
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
