package org.godigit.ikm.service.SearchServiceTest;


import org.godigit.ikm.dto.article.ArticleResponse;
import org.godigit.ikm.dto.search.SearchRequest;
import org.godigit.ikm.dto.search.SearchResult;
import org.godigit.ikm.entities.Article;
import org.godigit.ikm.mapper.SearchMapper;
import org.godigit.ikm.repository.ArticleRepository;
import org.godigit.ikm.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchServiceTest {

    private ArticleRepository articleRepository;
    private SearchMapper searchMapper;
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        articleRepository = mock(ArticleRepository.class);
        searchMapper = mock(SearchMapper.class);
        searchService = new SearchService(articleRepository, searchMapper);
    }

    private Article article(Long id, String title) {
        return Article.builder()
                .id(id)
                .title(title)
                .body("body")
                .currentVersion(1)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }

    private ArticleResponse resp(Long id, String title) {
        return new ArticleResponse(id, title, "body", null, 1,
                OffsetDateTime.now(), OffsetDateTime.now(), Set.of());
    }

    @Test
    void search_buildsPageableAndMapsResults() {
        // GIVEN
        Article a1 = article(1L, "T1");
        Article a2 = article(2L, "T2");
        Page<Article> page = new PageImpl<>(List.of(a1, a2), PageRequest.of(1, 5), 12);
        when(articleRepository.search(
                eq("k"), eq("DPT"), eq(Set.of("t1","t2")), eq("Ti"), eq(99L), any(Pageable.class)
        )).thenReturn(page);

        // mapstruct mapper mock
        when(searchMapper.toResponse(a1)).thenReturn(resp(1L, "T1"));
        when(searchMapper.toResponse(a2)).thenReturn(resp(2L, "T2"));

        SearchRequest req = new SearchRequest(99L, "k", "Ti", "DPT", Set.of("t1","t2"), 1, 5);

        // WHEN
        SearchResult<ArticleResponse> out = searchService.search(req);

        // THEN
        assertThat(out.total()).isEqualTo(12);
        assertThat(out.page()).isEqualTo(1);
        assertThat(out.size()).isEqualTo(5);
        assertThat(out.items()).extracting(ArticleResponse::title).containsExactly("T1", "T2");
    }

    @Test
    void search_defaultsPageAndSize_whenNullOrInvalid() {
        when(articleRepository.search(any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenAnswer((Answer<Page<Article>>) inv ->
                        new PageImpl<>(List.of(), inv.getArgument(5), 0));

        SearchRequest req = new SearchRequest(null, null, null, null, null, -1, 0);

        SearchResult<ArticleResponse> out = searchService.search(req);
        assertThat(out.page()).isEqualTo(0);
        assertThat(out.size()).isEqualTo(10);
        assertThat(out.total()).isZero();
    }

    @Test
    void searchById_returnsMappedResponse_orThrows() {
        Article a = article(7L, "Hello");
        when(articleRepository.findById(7L)).thenReturn(Optional.of(a));
        when(searchMapper.toResponse(a)).thenReturn(resp(7L, "Hello"));

        ArticleResponse r = searchService.searchById(7L);
        assertThat(r.id()).isEqualTo(7L);

        when(articleRepository.findById(8L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> searchService.searchById(8L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("8");
    }

    @Test
    void searchByTitle_delegatesToRepoAndMaps() {
        Article a = article(1L, "abc");
        when(articleRepository.findByTitleContainingIgnoreCase("abc")).thenReturn(List.of(a));
        when(searchMapper.toResponse(a)).thenReturn(resp(1L, "abc"));
        List<ArticleResponse> out = searchService.searchByTitle("abc");
        assertThat(out).hasSize(1);
        assertThat(out.get(0).title()).isEqualTo("abc");
    }

    @Test
    void searchByTags_delegatesToRepoAndMaps() {
        Article a = article(2L, "titled");
        when(articleRepository.findByTagNames(Set.of("x","y"))).thenReturn(List.of(a));
        when(searchMapper.toResponse(a)).thenReturn(resp(2L, "titled"));
        List<ArticleResponse> out = searchService.searchByTags(Set.of("x","y"));
        assertThat(out).hasSize(1);
        assertThat(out.get(0).id()).isEqualTo(2L);
    }

    @Test
    void searchByDepartment_delegatesToRepoAndMaps() {
        Article a = article(3L, "dpt");
        when(articleRepository.findByDepartmentCode("IT")).thenReturn(List.of(a));
        when(searchMapper.toResponse(a)).thenReturn(resp(3L, "dpt"));
        List<ArticleResponse> out = searchService.searchByDepartment("IT");
        assertThat(out).hasSize(1);
        assertThat(out.get(0).id()).isEqualTo(3L);
    }

    @Test
    void searchByKeyword_delegatesToRepoAndMaps() {
        Article a = article(4L, "kw");
        when(articleRepository.findByBodyContainingIgnoreCase("boot")).thenReturn(List.of(a));
        when(searchMapper.toResponse(a)).thenReturn(resp(4L, "kw"));
        List<ArticleResponse> out = searchService.searchByKeyword("boot");
        assertThat(out).hasSize(1);
        assertThat(out.get(0).id()).isEqualTo(4L);
    }
}

