package com.agricycle.app.service.mapper;

import com.agricycle.app.domain.Post;
import com.agricycle.app.domain.Signalement;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.service.dto.PostDTO;
import com.agricycle.app.service.dto.SignalementDTO;
import com.agricycle.app.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Signalement} and its DTO {@link SignalementDTO}.
 */
@Mapper(componentModel = "spring")
public interface SignalementMapper extends EntityMapper<SignalementDTO, Signalement> {
    @Mapping(target = "auteur", source = "auteur", qualifiedByName = "utilisateurId")
    @Mapping(target = "ciblePost", source = "ciblePost", qualifiedByName = "postId")
    SignalementDTO toDto(Signalement s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);

    @Named("postId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PostDTO toDtoPostId(Post post);
}
