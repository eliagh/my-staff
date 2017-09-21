package com.mycompany.mystaff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.mystaff.domain.Appointment;

/**
 * Spring Data JPA repository for the Appointment entity.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

}
