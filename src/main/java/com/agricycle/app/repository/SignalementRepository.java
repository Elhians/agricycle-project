package com.agricycle.app.repository;

import com.agricycle.app.domain.Signalement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Signalement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SignalementRepository extends JpaRepository<Signalement, Long>, JpaSpecificationExecutor<Signalement> {}
