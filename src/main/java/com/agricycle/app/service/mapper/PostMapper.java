package com.agricycle.app.service.mapper;

import com.agricycle.app.domain.Post;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.service.dto.PostDTO;
import com.agricycle.app.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Post} and its DTO {@link PostDTO}.
 */
@Mapper(componentModel = "spring")
public interface PostMapper extends EntityMapper<PostDTO, Post> {
    @Mapping(target = "auteur", source = "auteur", qualifiedByName = "utilisateurId")
    PostDTO toDto(Post s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
