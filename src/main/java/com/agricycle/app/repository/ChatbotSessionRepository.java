package com.agricycle.app.repository;

import com.agricycle.app.domain.ChatbotSession;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ChatbotSession entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatbotSessionRepository extends JpaRepository<ChatbotSession, Long>, JpaSpecificationExecutor<ChatbotSession> {}
