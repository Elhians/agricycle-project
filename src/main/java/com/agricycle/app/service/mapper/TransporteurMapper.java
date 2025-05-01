package com.agricycle.app.service.mapper;

import com.agricycle.app.domain.Transporteur;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.service.dto.TransporteurDTO;
import com.agricycle.app.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transporteur} and its DTO {@link TransporteurDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransporteurMapper extends EntityMapper<TransporteurDTO, Transporteur> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    TransporteurDTO toDto(Transporteur s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
