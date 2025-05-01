package com.agricycle.app.service.mapper;

import com.agricycle.app.domain.Paiement;
import com.agricycle.app.domain.Transaction;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.service.dto.PaiementDTO;
import com.agricycle.app.service.dto.TransactionDTO;
import com.agricycle.app.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Paiement} and its DTO {@link PaiementDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaiementMapper extends EntityMapper<PaiementDTO, Paiement> {
    @Mapping(target = "transaction", source = "transaction", qualifiedByName = "transactionId")
    @Mapping(target = "acheteur", source = "acheteur", qualifiedByName = "utilisateurId")
    PaiementDTO toDto(Paiement s);

    @Named("transactionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransactionDTO toDtoTransactionId(Transaction transaction);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
