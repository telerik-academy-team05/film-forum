package com.telerik.filmforum.services;

import com.telerik.filmforum.models.Post;
import com.telerik.filmforum.models.PostFilters;
import com.telerik.filmforum.models.User;

import java.util.List;

public interface PostService {

    Post getPostById(int id);

    List<Post> getPostsByAuthor (User user);

    List<Post> getAllPosts(PostFilters postFilters);

    void createPost(Post post, User user);

    void updatePost(Post post, User user);

    void deletePost(int id, User user);

    long getPostsCount();

    List<Post> getMostCommentedPosts();

    List<Post> getMostRecentPosts();
}
