package org.godigit.ikm.service.ArticleServiceTest;

import org.godigit.ikm.dto.article.ArticleCreateRequest;
import org.godigit.ikm.dto.article.ArticleResponse;
import org.godigit.ikm.dto.article.ArticleUpdateRequest;
import org.godigit.ikm.entities.Article;
import org.godigit.ikm.entities.ArticleVersion;
import org.godigit.ikm.entities.Department;
import org.godigit.ikm.entities.Tag;
import org.godigit.ikm.mapper.ArticleMapper;
import org.godigit.ikm.repository.ArticleRepository;
import org.godigit.ikm.repository.ArticleVersionRepository;
import org.godigit.ikm.repository.DepartmentRepository;
import org.godigit.ikm.service.ArticleService;
import org.godigit.ikm.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock private ArticleRepository articleRepository;
    @Mock private ArticleVersionRepository articleVersionRepository;
    @Mock private DepartmentRepository departmentRepository;
    @Mock private TagService tagService;
    @Mock private ArticleMapper articleMapper;

    private ArticleService service;

    @BeforeEach
    void setUp() {
        service = new ArticleService(
                articleRepository,
                articleVersionRepository,
                departmentRepository,
                tagService,
                articleMapper
        );
    }

    // -------- create --------

    @Test
    void create_savesArticle_setsTags_createsVersion_returnsMapperOutput() {
        Long deptId = 10L;
        Department dept = Department.builder().id(deptId).code("ENG").name("Engineering").build();

        ArticleCreateRequest req = new ArticleCreateRequest(
                "Title", "Body", deptId, Set.of("java", "spring")
        );

        when(departmentRepository.findById(deptId)).thenReturn(Optional.of(dept));

        Set<Tag> ensured = Set.of(
                Tag.builder().id(1L).name("java").build(),
                Tag.builder().id(2L).name("spring").build()
        );
        when(tagService.ensureTags(req.tags())).thenReturn(ensured);

        Article saved = Article.builder()
                .id(111L)
                .title(req.title())
                .body(req.body())
                .department(dept)
                .currentVersion(1)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        saved.setTags(ensured);

        when(articleRepository.saveAndFlush(any(Article.class))).thenReturn(saved);

        ArticleResponse mapped = mock(ArticleResponse.class);
        when(articleMapper.toResponse(saved)).thenReturn(mapped);

        // Act
        ArticleResponse out = service.create(req);

        // Assert
        assertThat(out).isSameAs(mapped);

        verify(departmentRepository).findById(deptId);
        verify(tagService).ensureTags(req.tags());
        verify(articleRepository).saveAndFlush(any(Article.class));
        verify(articleVersionRepository).save(any(ArticleVersion.class));
        verify(articleMapper).toResponse(saved);

        ArgumentCaptor<ArticleVersion> verCap = ArgumentCaptor.forClass(ArticleVersion.class);
        verify(articleVersionRepository, atLeastOnce()).save(verCap.capture());
        ArticleVersion v = verCap.getValue();
        assertThat(v.getArticle().getId()).isEqualTo(111L);
        assertThat(v.getVersion()).isEqualTo(1);
        assertThat(v.getTitle()).isEqualTo("Title");
        assertThat(v.getBody()).isEqualTo("Body");
    }

    @Test
    void create_throws_whenDepartmentNotFound() {
        ArticleCreateRequest req = new ArticleCreateRequest("T","B", 77L, Set.of());
        when(departmentRepository.findById(77L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Department not found");

        verifyNoInteractions(tagService, articleRepository, articleVersionRepository, articleMapper);
    }

    // -------- update --------

    @Test
    void update_updatesFields_incrementsVersion_setsTags_persistsVersion_returnsMapperOutput() {
        Long id = 5L;
        Article existing = Article.builder()
                .id(id).title("Old").body("OldBody")
                .currentVersion(2)
                .department(Department.builder().id(1L).code("ENG").build())
                .createdAt(OffsetDateTime.now().minusDays(1))
                .updatedAt(OffsetDateTime.now().minusDays(1))
                .build();

        when(articleRepository.findById(id)).thenReturn(Optional.of(existing));

        ArticleUpdateRequest req = new ArticleUpdateRequest("NewTitle", "NewBody", Set.of("kotlin"));
        Set<Tag> ensured = Set.of(Tag.builder().id(3L).name("kotlin").build());
        when(tagService.ensureTags(req.tags())).thenReturn(ensured);

        Article updated = Article.builder()
                .id(id)
                .title("NewTitle")
                .body("NewBody")
                .currentVersion(3)
                .department(existing.getDepartment())
                .createdAt(existing.getCreatedAt())
                .updatedAt(OffsetDateTime.now())
                .build();
        updated.setTags(ensured);

        when(articleRepository.saveAndFlush(any(Article.class))).thenReturn(updated);

        ArticleResponse mapped = mock(ArticleResponse.class);
        when(articleMapper.toResponse(updated)).thenReturn(mapped);

        // Act
        ArticleResponse out = service.update(id, req);

        // Assert
        assertThat(out).isSameAs(mapped);

        verify(articleRepository).findById(id);
        verify(tagService).ensureTags(req.tags());
        verify(articleRepository).saveAndFlush(any(Article.class));
        verify(articleVersionRepository).save(any(ArticleVersion.class));
        verify(articleMapper).toResponse(updated);

        ArgumentCaptor<Article> artCap = ArgumentCaptor.forClass(Article.class);
        verify(articleRepository).saveAndFlush(artCap.capture());
        Article savedArg = artCap.getValue();
        assertThat(savedArg.getCurrentVersion()).isEqualTo(3);
        assertThat(savedArg.getTitle()).isEqualTo("NewTitle");
        assertThat(savedArg.getBody()).isEqualTo("NewBody");

        ArgumentCaptor<ArticleVersion> verCap = ArgumentCaptor.forClass(ArticleVersion.class);
        verify(articleVersionRepository).save(verCap.capture());
        ArticleVersion v = verCap.getValue();
        assertThat(v.getVersion()).isEqualTo(3);
        assertThat(v.getTitle()).isEqualTo("NewTitle");
        assertThat(v.getBody()).isEqualTo("NewBody");
        assertThat(v.getArticle().getId()).isEqualTo(id);
    }

    @Test
    void update_throws_whenArticleNotFound() {
        when(articleRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(404L, new ArticleUpdateRequest("t","b", Set.of())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Article not found");

        verifyNoMoreInteractions(articleRepository);
        verifyNoInteractions(tagService, articleVersionRepository, articleMapper);
    }

    // -------- get --------

    @Test
    void get_returnsMapperOutput_whenFound() {
        Article a = Article.builder().id(9L).title("T").body("B").currentVersion(1).build();
        when(articleRepository.findById(9L)).thenReturn(Optional.of(a));

        ArticleResponse mapped = mock(ArticleResponse.class);
        when(articleMapper.toResponse(a)).thenReturn(mapped);

        ArticleResponse out = service.get(9L);
        assertThat(out).isSameAs(mapped);

        verify(articleRepository).findById(9L);
        verify(articleMapper).toResponse(a);
    }

    @Test
    void get_throws_whenNotFound() {
        when(articleRepository.findById(9L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(9L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Article not found");
    }

    // -------- list --------

    @Test
    void list_mapsAll_andReturnsMapperOutputsInOrder() {
        Article a1 = Article.builder().id(1L).title("A").body("B").currentVersion(1).build();
        Article a2 = Article.builder().id(2L).title("C").body("D").currentVersion(1).build();
        when(articleRepository.findAll()).thenReturn(List.of(a1, a2));

        ArticleResponse r1 = mock(ArticleResponse.class);
        ArticleResponse r2 = mock(ArticleResponse.class);
        when(articleMapper.toResponse(a1)).thenReturn(r1);
        when(articleMapper.toResponse(a2)).thenReturn(r2);

        List<ArticleResponse> out = service.list();
        assertThat(out).containsExactly(r1, r2);

        verify(articleRepository).findAll();
        verify(articleMapper).toResponse(a1);
        verify(articleMapper).toResponse(a2);
    }

    // -------- delete --------

    @Test
    void delete_callsRepository() {
        service.delete(123L);
        verify(articleRepository, times(1)).deleteById(123L);
        verifyNoMoreInteractions(articleRepository);
    }
}
