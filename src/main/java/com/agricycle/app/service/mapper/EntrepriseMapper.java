package com.agricycle.app.service.mapper;

import com.agricycle.app.domain.Entreprise;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.service.dto.EntrepriseDTO;
import com.agricycle.app.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Entreprise} and its DTO {@link EntrepriseDTO}.
 */
@Mapper(componentModel = "spring")
public interface EntrepriseMapper extends EntityMapper<EntrepriseDTO, Entreprise> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    EntrepriseDTO toDto(Entreprise s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
