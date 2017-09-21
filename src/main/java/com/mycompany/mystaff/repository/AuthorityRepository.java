package com.mycompany.mystaff.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycompany.mystaff.domain.Authority;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {

}
