package com.mycompany.mystaff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.mystaff.domain.Category;

/**
 * Spring Data JPA repository for the Category entity.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  List<Category> findByCompanyId(Long companyId);

  @Query("SELECT c FROM Category c WHERE (id = :id) AND (company_id = :companyId)")
  Category findByIdAndCompanyID(@Param("id") Long id, @Param("companyId") Long companyId);

  @Modifying
  @Transactional
  @Query("DELETE FROM Category c WHERE (id = :id) AND (company_id = :companyId)")
  void deleteByIdAndCompanyId(@Param("id") Long id, @Param("companyId") Long companyId);

}
