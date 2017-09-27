package com.mycompany.mystaff.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.mystaff.domain.Location;

/**
 * Spring Data JPA repository for the Location entity.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    Page<Location> findByCompanyId(Pageable pageable, Long companyId);

    Location findByIdAndCompanyId(Long id, Long companyId);

    void deleteByIdAndCompanyId(Long id, Long companyId);

}
