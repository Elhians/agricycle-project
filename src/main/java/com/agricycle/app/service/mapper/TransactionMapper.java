package com.agricycle.app.service.mapper;

import com.agricycle.app.domain.Produit;
import com.agricycle.app.domain.Transaction;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.service.dto.ProduitDTO;
import com.agricycle.app.service.dto.TransactionDTO;
import com.agricycle.app.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transaction} and its DTO {@link TransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {
    @Mapping(target = "produit", source = "produit", qualifiedByName = "produitId")
    @Mapping(target = "acheteur", source = "acheteur", qualifiedByName = "utilisateurId")
    TransactionDTO toDto(Transaction s);

    @Named("produitId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProduitDTO toDtoProduitId(Produit produit);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
