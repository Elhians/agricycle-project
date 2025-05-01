package com.agricycle.app.service.mapper;

import com.agricycle.app.domain.Commercant;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.service.dto.CommercantDTO;
import com.agricycle.app.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Commercant} and its DTO {@link CommercantDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommercantMapper extends EntityMapper<CommercantDTO, Commercant> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    CommercantDTO toDto(Commercant s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
