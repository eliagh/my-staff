package com.mycompany.mystaff.service.mapper;

import org.mapstruct.Mapper;

import com.mycompany.mystaff.domain.Customer;
import com.mycompany.mystaff.service.dto.CustomerDTO;

/**
 * Mapper for the entity Customer and its DTO CustomerDTO.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class, })
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {

  default Customer fromId(Long id) {
    if (id == null) {
      return null;
    }
    Customer customer = new Customer();
    customer.setId(id);
    return customer;
  }

}
