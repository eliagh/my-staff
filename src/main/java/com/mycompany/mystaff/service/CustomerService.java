package com.mycompany.mystaff.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.mystaff.domain.Customer;
import com.mycompany.mystaff.repository.CustomerRepository;
import com.mycompany.mystaff.repository.search.CustomerSearchRepository;
import com.mycompany.mystaff.service.dto.CustomerDTO;
import com.mycompany.mystaff.service.mapper.CustomerMapper;

/**
 * Service Implementation for managing Customer.
 */
@Service
@Transactional
public class CustomerService {

  private final Logger log = LoggerFactory.getLogger(CustomerService.class);

  private final CustomerRepository customerRepository;

  private final CustomerMapper customerMapper;

  private final CustomerSearchRepository customerSearchRepository;

  public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, CustomerSearchRepository customerSearchRepository) {
    this.customerRepository = customerRepository;
    this.customerMapper = customerMapper;
    this.customerSearchRepository = customerSearchRepository;
  }

  /**
   * Save a customer.
   *
   * @param customerDTO the entity to save
   * @return the persisted entity
   */
  public CustomerDTO save(CustomerDTO customerDTO) {
    log.debug("Request to save Customer : {}", customerDTO);

    Customer customer = customerMapper.toEntity(customerDTO);
    customer = customerRepository.save(customer);
    CustomerDTO result = customerMapper.toDto(customer);
    customerSearchRepository.save(customer);
    return result;
  }

  /**
   * Get all the customers.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public Page<CustomerDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Customers");

    return customerRepository.findAll(pageable).map(customerMapper::toDto);
  }

  /**
   * Get one customer by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public CustomerDTO findOne(Long id) {
    log.debug("Request to get Customer : {}", id);

    Customer customer = customerRepository.findOneWithEagerRelationships(id);
    return customerMapper.toDto(customer);
  }

  /**
   * Delete the customer by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete Customer : {}", id);

    customerRepository.delete(id);
    customerSearchRepository.delete(id);
  }

  /**
   * Search for the customer corresponding to the query.
   *
   * @param query the query of the search
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public Page<CustomerDTO> search(String query, Pageable pageable) {
    log.debug("Request to search for a page of Customers for query {}", query);

    Page<Customer> result = customerSearchRepository.search(queryStringQuery(query), pageable);
    return result.map(customerMapper::toDto);
  }

}
