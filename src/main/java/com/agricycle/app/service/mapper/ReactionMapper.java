package com.agricycle.app.service.mapper;

import com.agricycle.app.domain.Post;
import com.agricycle.app.domain.Reaction;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.service.dto.PostDTO;
import com.agricycle.app.service.dto.ReactionDTO;
import com.agricycle.app.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reaction} and its DTO {@link ReactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReactionMapper extends EntityMapper<ReactionDTO, Reaction> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    @Mapping(target = "post", source = "post", qualifiedByName = "postId")
    ReactionDTO toDto(Reaction s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);

    @Named("postId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PostDTO toDtoPostId(Post post);
}
