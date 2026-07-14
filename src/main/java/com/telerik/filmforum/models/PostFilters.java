package com.telerik.filmforum.models;

import java.util.Optional;

public class PostFilters {

    private Optional<String> title;
    private Optional<String> author;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public PostFilters(
            String title,
            String author,
            String sortBy,
            String sortOrder
    ){
        this.title = Optional.ofNullable(title);
        this.author = Optional.ofNullable(author);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getTitle() {

        return title;
    }

    public Optional<String> getAuthor() {
        return author;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
