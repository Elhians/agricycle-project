package com.agricycle.app.repository;

import com.agricycle.app.domain.Transporteur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Transporteur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransporteurRepository extends JpaRepository<Transporteur, Long>, JpaSpecificationExecutor<Transporteur> {}
