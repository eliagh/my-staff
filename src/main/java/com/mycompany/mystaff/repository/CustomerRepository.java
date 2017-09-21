package com.mycompany.mystaff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mycompany.mystaff.domain.Customer;

/**
 * Spring Data JPA repository for the Customer entity.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  @Query("select distinct customer from Customer customer left join fetch customer.companies")
  List<Customer> findAllWithEagerRelationships();

  @Query("select customer from Customer customer left join fetch customer.companies where customer.id =:id")
  Customer findOneWithEagerRelationships(@Param("id") Long id);

}
