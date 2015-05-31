package com.clone.google.service;

import com.clone.google.entity.Item;

import java.io.IOException;
import java.util.List;

public interface ItemService {

    Item index(String url, int maxLevel) throws IOException;
    List<Item> findForQuery(String query, int page, int limit);
}
