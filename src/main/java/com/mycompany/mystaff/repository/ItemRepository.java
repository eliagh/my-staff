package com.mycompany.mystaff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.mystaff.domain.Item;

/**
 * Spring Data JPA repository for the Item entity.
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}
