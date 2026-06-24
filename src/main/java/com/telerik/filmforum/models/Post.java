package com.telerik.filmforum.models;

import java.time.LocalDateTime;

public class Post {
    private int id;
    private String title;
    private String content;
    private int authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
