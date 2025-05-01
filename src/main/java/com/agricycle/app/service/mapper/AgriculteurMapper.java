package com.agricycle.app.service.mapper;

import com.agricycle.app.domain.Agriculteur;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.service.dto.AgriculteurDTO;
import com.agricycle.app.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Agriculteur} and its DTO {@link AgriculteurDTO}.
 */
@Mapper(componentModel = "spring")
public interface AgriculteurMapper extends EntityMapper<AgriculteurDTO, Agriculteur> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    AgriculteurDTO toDto(Agriculteur s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
