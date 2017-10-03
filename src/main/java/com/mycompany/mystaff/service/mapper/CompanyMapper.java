package com.mycompany.mystaff.service.mapper;

import org.mapstruct.Mapper;

import com.mycompany.mystaff.domain.Company;
import com.mycompany.mystaff.service.dto.CompanyDTO;

/**
 * Mapper for the entity Company and its DTO CompanyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {

  default Company fromId(Long id) {
    if (id == null) {
      return null;
    }
    Company company = new Company();
    company.setId(id);
    return company;
  }

}
