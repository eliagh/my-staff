package com.mycompany.mystaff.repository;

import com.mycompany.mystaff.domain.Customer;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("select distinct customer from Customer customer left join fetch customer.companies")
    List<Customer> findAllWithEagerRelationships();

    @Query("select customer from Customer customer left join fetch customer.companies where customer.id =:id")
    Customer findOneWithEagerRelationships(@Param("id") Long id);

}
