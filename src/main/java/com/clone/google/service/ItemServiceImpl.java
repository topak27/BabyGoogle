package com.clone.google.service;

import com.clone.google.entity.Item;
import com.clone.google.repository.ItemRepository;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class ItemServiceImpl implements ItemService {

    private final static int processorCount = Runtime.getRuntime().availableProcessors()*8;
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
    public Item index(String url, int maxLevel) throws IOException {
        Set<String> uniqueSet = new HashSet<>();
        return this.process(uniqueSet, url, maxLevel);
    }

    private Item process (Set<String> uniqueSet, String link, int level) throws IOException {
        if (!uniqueSet.add(link)) return null;

        Document doc = Jsoup.connect(link)
                .userAgent("Mozilla")
                .get();

        String title = doc.title();
        String body = doc.body().text();
        Item item = this.save(new Item(link, title, body));

        if (--level > 0) {
            Elements links = doc.select("a[href]");
            executorService.execute(new Task(uniqueSet, links, level));
        }

        return item;
    }

    private Item save(Item item) {
        this.itemRepository.save(item);
        logger.info("Indexed URL: " + item.getUrl());
        return item;
    }

    private class Task implements Runnable {
        private Set<String> uniqueSet;
        private Elements subList;
        private int level;

        public Task(Set<String> uniqueSet, Elements links, int level) {
            this.uniqueSet = uniqueSet;
            this.subList = links;
            this.level = level;
        }

        @Override
        public void run() {
            for (Element link : this.subList) {
                String url = link.attr("abs:href");
                if (url.length() < 10) continue;

                try {
                    ItemServiceImpl.this.process(this.uniqueSet, link.attr("abs:href"), this.level);

                } catch (Exception e) {
                    logger.error("Error while indexing \"" + link.attr("abs:href") + "\": " + e.getMessage());
                }
            }
        }
    }
}
