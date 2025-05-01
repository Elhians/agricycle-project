package com.agricycle.app.service.mapper;

import com.agricycle.app.domain.ContenuEducatif;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.service.dto.ContenuEducatifDTO;
import com.agricycle.app.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContenuEducatif} and its DTO {@link ContenuEducatifDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContenuEducatifMapper extends EntityMapper<ContenuEducatifDTO, ContenuEducatif> {
    @Mapping(target = "auteur", source = "auteur", qualifiedByName = "utilisateurId")
    ContenuEducatifDTO toDto(ContenuEducatif s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
