package com.agricycle.app.service.mapper;

import com.agricycle.app.domain.Consommateur;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.service.dto.ConsommateurDTO;
import com.agricycle.app.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Consommateur} and its DTO {@link ConsommateurDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConsommateurMapper extends EntityMapper<ConsommateurDTO, Consommateur> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    ConsommateurDTO toDto(Consommateur s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
