package com.mycompany.mystaff.repository;

import com.mycompany.mystaff.domain.Activity;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Activity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    @Query("select distinct activity from Activity activity left join fetch activity.locations left join fetch activity.categories")
    List<Activity> findAllWithEagerRelationships();

    @Query("select activity from Activity activity left join fetch activity.locations left join fetch activity.categories where activity.id =:id")
    Activity findOneWithEagerRelationships(@Param("id") Long id);

}
