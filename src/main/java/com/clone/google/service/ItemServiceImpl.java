package com.clone.google.service;

import com.clone.google.entity.Item;
import com.clone.google.repository.ItemRepository;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    final static Logger logger = Logger.getLogger(ItemServiceImpl.class);

    @Autowired
    ItemRepository itemRepository;

    @Override
    public void index(Item item) {
        this.itemRepository.save(item);
        logger.info("Indexed URL: " + item.getUrl());
    }

    @Override
    public Item index(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        String title = doc.title();
        String body = doc.body().text();
        Item item = new Item(url, title, body);
        this.index(item);
        return item;
    }

    @Override
    public List<Item> findForQuery(String query) {
        return null;
    }
}
