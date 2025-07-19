package com.agricycle.app.service.mapper;

import com.agricycle.app.domain.User;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.service.dto.UserDTO;
import com.agricycle.app.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Utilisateur} and its DTO {@link UtilisateurDTO}.
 */
@Mapper(componentModel = "spring")
public interface UtilisateurMapper extends EntityMapper<UtilisateurDTO, Utilisateur> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    UtilisateurDTO toDto(Utilisateur s);

    @Mapping(target = "user.createdBy", ignore = true)
    @Mapping(target = "user.lastModifiedBy", ignore = true)
    @Mapping(target = "user.lastModifiedDate", ignore = true)
    @Mapping(target = "user.password", ignore = true)
    @Mapping(target = "user.activated", ignore = true)
    @Mapping(target = "user.activationKey", ignore = true)
    @Mapping(target = "user.resetKey", ignore = true)
    @Mapping(target = "user.resetDate", ignore = true)
    @Mapping(target = "user.langKey", ignore = true)
    @Mapping(target = "user.authorities", ignore = true)
    @Mapping(target = "agriculteur", ignore = true)
    @Mapping(target = "commercant", ignore = true)
    @Mapping(target = "consommateur", ignore = true)
    @Mapping(target = "entreprise", ignore = true)
    @Mapping(target = "organisation", ignore = true)
    @Mapping(target = "transporteur", ignore = true)
    Utilisateur toEntity(UtilisateurDTO utilisateurDTO);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user.createdBy", ignore = true)
    @Mapping(target = "user.lastModifiedBy", ignore = true)
    @Mapping(target = "user.lastModifiedDate", ignore = true)
    @Mapping(target = "user.password", ignore = true)
    @Mapping(target = "user.activated", ignore = true)
    @Mapping(target = "user.activationKey", ignore = true)
    @Mapping(target = "user.resetKey", ignore = true)
    @Mapping(target = "user.resetDate", ignore = true)
    @Mapping(target = "user.langKey", ignore = true)
    @Mapping(target = "user.authorities", ignore = true)
    @Mapping(target = "agriculteur", ignore = true)
    @Mapping(target = "commercant", ignore = true)
    @Mapping(target = "consommateur", ignore = true)
    @Mapping(target = "entreprise", ignore = true)
    @Mapping(target = "organisation", ignore = true)
    @Mapping(target = "transporteur", ignore = true)
    void partialUpdate(@MappingTarget Utilisateur entity, UtilisateurDTO dto);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "imageUrl", source = "imageUrl")
    UserDTO toDtoUserId(User user);
}
