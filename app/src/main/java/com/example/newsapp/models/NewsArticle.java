package com.example.newsapp.models;


public class NewsArticle {

    private String title;
    private String description;
    private String content;
    private String author;
    private String publishedAt;
    private String urlToImage;
    private String url;
    private String sourceName;

    public NewsArticle(String title, String description, String content,
                       String author, String publishedAt,
                       String urlToImage, String url, String sourceName) {
        this.title       = title;
        this.description = description;
        this.content     = content;
        this.author      = author;
        this.publishedAt = publishedAt;
        this.urlToImage  = urlToImage;
        this.url         = url;
        this.sourceName  = sourceName;
    }

    public String getTitle()       { return title; }
    public String getDescription() { return description; }
    public String getContent()     { return content; }
    public String getAuthor()      { return author; }
    public String getPublishedAt() { return publishedAt; }
    public String getUrlToImage()  { return urlToImage; }
    public String getUrl()         { return url; }
    public String getSourceName()  { return sourceName; }
}
