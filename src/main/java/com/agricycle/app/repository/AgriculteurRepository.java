package com.agricycle.app.repository;

import com.agricycle.app.domain.Agriculteur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Agriculteur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgriculteurRepository extends JpaRepository<Agriculteur, Long>, JpaSpecificationExecutor<Agriculteur> {}
