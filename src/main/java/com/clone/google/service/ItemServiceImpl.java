package com.clone.google.service;

import com.clone.google.entity.Item;
import com.clone.google.repository.ItemRepository;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class ItemServiceImpl implements ItemService {

    private final static int processorCount = Runtime.getRuntime().availableProcessors();
    private final static Logger logger = Logger.getLogger(ItemServiceImpl.class);
    private final static ExecutorService executorService = new ThreadPoolExecutor(
            processorCount, processorCount, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>()
    );

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public List<Item> findForQuery(String query, int page, int limit) {
        return this.itemRepository.findForQuery(query, new PageRequest(page, limit)).getContent();
    }

    @Override
    public Item index(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        String title = doc.title();
        String body = doc.body().text();
        Item item = new Item(url, title, body);
        this.save(item);
        return item;
    }

    private void process(String url) {

    }

    private void save(Item item) {
        this.itemRepository.save(item);
        logger.info("Indexed URL: " + item.getUrl());
    }
}
