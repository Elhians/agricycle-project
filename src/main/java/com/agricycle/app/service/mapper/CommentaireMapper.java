package com.agricycle.app.service.mapper;

import com.agricycle.app.domain.Commentaire;
import com.agricycle.app.domain.Post;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.service.dto.CommentaireDTO;
import com.agricycle.app.service.dto.PostDTO;
import com.agricycle.app.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Commentaire} and its DTO {@link CommentaireDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommentaireMapper extends EntityMapper<CommentaireDTO, Commentaire> {
    @Mapping(target = "post", source = "post", qualifiedByName = "postId")
    @Mapping(target = "auteur", source = "auteur", qualifiedByName = "utilisateurId")
    CommentaireDTO toDto(Commentaire s);

    @Named("postId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PostDTO toDtoPostId(Post post);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
