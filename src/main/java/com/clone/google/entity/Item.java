package com.clone.google.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Document(indexName = "clone", type = "item", shards = 1, replicas = 0, refreshInterval = "-1")
public class Item {

    @Id
    private String url;

    private String title;

    @Field(
            searchAnalyzer = "standard",
            indexAnalyzer = "standard",
            store = true
    )
    private String body;

    public Item() {};

    public Item(String url, String title, String body) {
        this.url = url;
        this.title = title;
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
