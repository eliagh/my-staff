package com.mycompany.mystaff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.mystaff.domain.Inventory;

/**
 * Spring Data JPA repository for the Inventory entity.
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

}
