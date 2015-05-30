package com.clone.google.repository;

import com.clone.google.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomItemRepository {
    Page<Item> findByBody(String query, Pageable pageable);
}
