package com.mycompany.mystaff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mycompany.mystaff.domain.Activity;

/**
 * Spring Data JPA repository for the Activity entity.
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

  @Query("select distinct activity from Activity activity left join fetch activity.locations left join fetch activity.categories")
  List<Activity> findAllWithEagerRelationships();

  @Query("select activity from Activity activity left join fetch activity.locations left join fetch activity.categories where activity.id =:id")
  Activity findOneWithEagerRelationships(@Param("id") Long id);

}
