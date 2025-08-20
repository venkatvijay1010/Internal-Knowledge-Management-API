package org.godigit.ikm.mapper;

import org.godigit.ikm.dto.article.ArticleResponse;
import org.godigit.ikm.entity.Article;
import org.godigit.ikm.entity.Tag;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SearchMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "body", source = "body")
    @Mapping(target = "departmentCode", source = "department.code")
    @Mapping(target = "currentVersion", source = "currentVersion")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "tags", qualifiedByName = "tagsToStrings")
    ArticleResponse toResponse(Article article);

    @Named("tagsToStrings")
    static Set<String> tagsToStrings(Set<Tag> tags) {
        if (tags == null) return Set.of();
        return tags.stream().map(Tag::getName).collect(Collectors.toSet());
    }
}
