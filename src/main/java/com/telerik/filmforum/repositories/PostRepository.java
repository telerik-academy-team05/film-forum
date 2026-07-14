package com.telerik.filmforum.repositories;

import com.telerik.filmforum.models.Post;
import com.telerik.filmforum.models.PostFilters;
import com.telerik.filmforum.models.User;

import java.util.List;

public interface PostRepository {

    Post getPostById(int id);

    List<Post> getPostsByAuthor (User user);

    List<Post> getAllPosts(PostFilters postFilters);

    void createPost(Post post);

    void updatePost(Post post);

    void deletePost(int id);

    long getPostsCount();

    List<Post> getMostCommentedPosts();

    List<Post> getMostRecentPosts();
}
