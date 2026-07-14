package com.telerik.filmforum.services;


import com.telerik.filmforum.exceptions.AuthorizationException;
import com.telerik.filmforum.helpers.AuthorizationHelper;
import com.telerik.filmforum.models.Post;
import com.telerik.filmforum.models.PostFilters;
import com.telerik.filmforum.models.User;
import com.telerik.filmforum.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final AuthorizationHelper authorizationHelper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, AuthorizationHelper authorizationHelper) {
        this.postRepository = postRepository;
        this.authorizationHelper = authorizationHelper;
    }

    @Override
    public Post getPostById(int id) {
        return postRepository.getPostById(id);
    }

    @Override
    public List<Post> getPostsByAuthor(User user) {
        return postRepository.getPostsByAuthor(user);
    }

    @Override
    public List<Post> getAllPosts(PostFilters postFilters) {
        return postRepository.getAllPosts(postFilters);
    }

    @Override
    public void createPost(Post post, User user) {
        if (user.isBlocked()){
            throw new AuthorizationException("You are not allowed to create post");
        }
        post.setAuthor(user);
        post.setCreatedAt(LocalDateTime.now());
        postRepository.createPost(post);
    }

    @Override
    public void updatePost(Post post, User user) {
        if (user.isBlocked()){
            throw new AuthorizationException("You are not allowed to update post");
        }
        getPostById(post.getId());
        authorizationHelper.isAuthor(post.getAuthor(), user);
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.updatePost(post);

    }

    @Override
    public void deletePost(int id, User user) {
        User author = getPostById(id).getAuthor();
        authorizationHelper.checkAccessPermissions(author.getId(), user);
        postRepository.deletePost(id);
    }

    @Override
    public long getPostsCount() {
        return postRepository.getPostsCount();
    }

    //gets top 10 most commented posts
    public List<Post> getMostCommentedPosts(){
        return postRepository.getMostCommentedPosts();
    }

    //gets top 10 most recent posts
    public List<Post> getMostRecentPosts(){
        return postRepository.getMostRecentPosts();
    }
}
