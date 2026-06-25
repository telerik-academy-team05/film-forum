package com.telerik.filmforum.models;


import java.time.LocalDateTime;

public class Comment {
    private int id;
    private String content;
    private int authorId;
    private int postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
