package com.clone.google.repository;

import com.clone.google.entity.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ItemRepository extends ElasticsearchRepository<Item, String> {
}
