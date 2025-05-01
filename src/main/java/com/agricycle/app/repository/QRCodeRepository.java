package com.agricycle.app.repository;

import com.agricycle.app.domain.QRCode;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QRCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QRCodeRepository extends JpaRepository<QRCode, Long>, JpaSpecificationExecutor<QRCode> {}
