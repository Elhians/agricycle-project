package com.agricycle.app.repository;

import com.agricycle.app.domain.Consommateur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Consommateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsommateurRepository extends JpaRepository<Consommateur, Long>, JpaSpecificationExecutor<Consommateur> {}
