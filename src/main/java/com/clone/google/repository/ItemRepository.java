package com.clone.google.repository;

import com.clone.google.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ItemRepository extends ElasticsearchRepository<Item, String>, CustomItemRepository {
//    @Query("{\"match\" : {\"body\" : \"?0\"}}")
//    Page<Item> findByBody(String query, Pageable pageable);
}
