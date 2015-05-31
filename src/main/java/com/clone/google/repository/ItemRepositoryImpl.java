package com.clone.google.repository;

import com.clone.google.entity.Item;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.FacetedPageImpl;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemRepositoryImpl implements CustomItemRepository {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public Page<Item> findForQuery(String query, Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(QueryBuilders.matchQuery("body", query))
                .withHighlightFields(
                        new HighlightBuilder.Field("body").preTags("<mark>").postTags("</mark>"),
                        new HighlightBuilder.Field("title").preTags("<mark>").postTags("</mark>")
                        )
                .build();

        return this.elasticsearchTemplate.queryForPage(searchQuery,Item.class, new SearchResultMapper() {

            @Override
            public <T> FacetedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<Item> chunk = new ArrayList<>();
                for (SearchHit searchHit : response.getHits()) {
                    if (response.getHits().getHits().length <= 0) {
                        return null;
                    }
                    Item item = new Item();
                    Map<String, Object> source = searchHit.getSource();
                    Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();

                    item.setUrl((String) source.get("url"));
                    item.setTitle((String) source.get("title"));
                    item.setBody(highlightFields.get("body").fragments()[0].toString());

                    HighlightField title = highlightFields.get("title");
                    if (title != null) {
                        item.setTitle(title.fragments()[0].toString());
                    }
                    chunk.add(item);
                }
                return new FacetedPageImpl<>((List<T>) chunk);
            }
        });
    }
}
