package com.agricycle.app.service.mapper;

import com.agricycle.app.domain.Media;
import com.agricycle.app.domain.Post;
import com.agricycle.app.service.dto.MediaDTO;
import com.agricycle.app.service.dto.PostDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Media} and its DTO {@link MediaDTO}.
 */
@Mapper(componentModel = "spring")
public interface MediaMapper extends EntityMapper<MediaDTO, Media> {
    @Mapping(target = "post", source = "post", qualifiedByName = "postId")
    MediaDTO toDto(Media s);

    @Named("postId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PostDTO toDtoPostId(Post post);
}
