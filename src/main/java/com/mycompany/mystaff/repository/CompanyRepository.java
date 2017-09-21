package com.mycompany.mystaff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.mystaff.domain.Company;

/**
 * Spring Data JPA repository for the Company entity.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

}
