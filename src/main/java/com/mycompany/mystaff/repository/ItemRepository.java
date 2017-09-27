package com.mycompany.mystaff.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.mystaff.domain.Item;

/**
 * Spring Data JPA repository for the Item entity.
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAByCompanyId(Pageable pageable, Long companyId);

    Item findByIdAndCompanyId(Long id, Long companyId);

    void deleteByIdAndCompanyId(Long id, Long companyId);

}
