package com.agricycle.app.service.mapper;

import com.agricycle.app.domain.ChatbotSession;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.service.dto.ChatbotSessionDTO;
import com.agricycle.app.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatbotSession} and its DTO {@link ChatbotSessionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChatbotSessionMapper extends EntityMapper<ChatbotSessionDTO, ChatbotSession> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    ChatbotSessionDTO toDto(ChatbotSession s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
