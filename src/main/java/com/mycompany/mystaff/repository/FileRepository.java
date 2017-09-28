package com.mycompany.mystaff.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.mystaff.domain.File;

/**
 * Spring Data JPA repository for the File entity.
 */
@Repository
public interface FileRepository extends JpaRepository<File, Long> {

  @Query("SELECT f FROM File f WHERE (company_id = :companyId)")
  Page<File> findByCompanyId(Pageable pageable, @Param("companyId") Long companyId);

  @Query("SELECT f FROM File f WHERE (id = :id) AND (company_id = :companyId)")
  File findByIdAndCompanyId(@Param("id") Long id, @Param("companyId") Long companyId);

  @Modifying
  @Transactional
  @Query("DELETE FROM File f WHERE (id = :id) AND (company_id = :companyId)")
  void deleteByIdAndCompanyId(@Param("id") Long id, @Param("companyId") Long companyId);

}
