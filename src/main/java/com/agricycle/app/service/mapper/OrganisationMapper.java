package com.agricycle.app.service.mapper;

import com.agricycle.app.domain.Organisation;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.service.dto.OrganisationDTO;
import com.agricycle.app.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Organisation} and its DTO {@link OrganisationDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrganisationMapper extends EntityMapper<OrganisationDTO, Organisation> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    OrganisationDTO toDto(Organisation s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
