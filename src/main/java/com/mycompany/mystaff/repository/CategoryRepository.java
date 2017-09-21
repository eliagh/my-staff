package com.mycompany.mystaff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.mystaff.domain.Category;

/**
 * Spring Data JPA repository for the Category entity.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
