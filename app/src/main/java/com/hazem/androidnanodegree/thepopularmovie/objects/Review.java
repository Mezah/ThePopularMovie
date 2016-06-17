package com.hazem.androidnanodegree.thepopularmovie.objects;

/**
 * Created by Mezah on 6/11/2016.
 */
public class Review {

    private String id;
    private String author;
    private String content;

    public Review(String id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }


    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

}
