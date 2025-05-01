package com.agricycle.app.repository;

import com.agricycle.app.domain.ContenuEducatif;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContenuEducatif entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContenuEducatifRepository extends JpaRepository<ContenuEducatif, Long>, JpaSpecificationExecutor<ContenuEducatif> {}
