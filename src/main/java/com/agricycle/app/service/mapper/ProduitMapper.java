package com.agricycle.app.service.mapper;

import com.agricycle.app.domain.Produit;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.service.dto.ProduitDTO;
import com.agricycle.app.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Produit} and its DTO {@link ProduitDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProduitMapper extends EntityMapper<ProduitDTO, Produit> {
    @Mapping(target = "vendeur", source = "vendeur", qualifiedByName = "utilisateurId")
    @Mapping(target = "acheteur", source = "acheteur", qualifiedByName = "utilisateurId")
    ProduitDTO toDto(Produit s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
