package com.clone.google.service;

import com.clone.google.entity.Item;

import java.io.IOException;
import java.util.List;

public interface ItemService {

    void index(Item item);
    Item index(String url) throws IOException;
    List<Item> findForQuery(String query);
}
