package com.mycompany.mystaff.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mycompany.mystaff.domain.Authority;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {

  @Query("SELECT a FROM Authority a WHERE (name = :role1) OR (name = :role2) OR (name = :role3) OR (name = :role4)")
  Set<Authority> retrieveUserRegistrationRoles(@Param("role1") String role1, @Param("role2") String role2, @Param("role3") String role3, @Param("role4") String role4);

}
