package org.godigit.ikm.service.TagServiceTest;

import org.godigit.ikm.entities.Tag;
import org.godigit.ikm.repository.TagRepository;
import org.godigit.ikm.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TagServiceTest {

    private TagRepository tagRepository;
    private TagService tagService;

    @BeforeEach
    void setUp() {
        tagRepository = mock(TagRepository.class);
        tagService = new TagService(tagRepository);
    }

    @Test
    void ensureTags_returnsEmptySet_whenInputNullOrEmpty() {
        assertThat(tagService.ensureTags(null)).isEmpty();
        assertThat(tagService.ensureTags(Set.of())).isEmpty();
    }


}
