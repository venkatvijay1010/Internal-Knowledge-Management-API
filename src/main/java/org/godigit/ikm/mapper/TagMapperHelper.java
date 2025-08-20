package org.godigit.ikm.mapper;



import org.godigit.ikm.entities.Tag;

import java.util.Set;
import java.util.stream.Collectors;

public class TagMapperHelper {
    public static Set<String> mapTags(Set<Tag> tags) {
        return tags.stream().map(Tag::getName).collect(Collectors.toSet());
    }
}
