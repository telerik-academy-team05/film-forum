package com.telerik.filmforum.models;

import org.springframework.format.annotation.DateTimeFormat;

public class Post {
    private int id;
    private String title;
    private String content;
    private int authorId;
    private DateTimeFormat createdAt;
    private DateTimeFormat updatedAt;
}
