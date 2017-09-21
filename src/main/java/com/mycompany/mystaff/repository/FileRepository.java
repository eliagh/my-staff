package com.mycompany.mystaff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.mystaff.domain.File;

/**
 * Spring Data JPA repository for the File entity.
 */
@Repository
public interface FileRepository extends JpaRepository<File, Long> {

}
