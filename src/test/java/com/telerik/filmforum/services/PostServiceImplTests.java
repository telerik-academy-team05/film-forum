package com.telerik.filmforum.services;

import com.telerik.filmforum.Helpers;
import com.telerik.filmforum.exceptions.AuthorizationException;
import com.telerik.filmforum.helpers.AuthorizationHelper;
import com.telerik.filmforum.models.Post;
import com.telerik.filmforum.models.PostFilters;
import com.telerik.filmforum.models.User;
import com.telerik.filmforum.repositories.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTests {

    @Mock
    PostRepository postRepository;

    @Mock
    AuthorizationHelper authorizationHelper;

    @InjectMocks
    PostServiceImpl postService;

    @Test
    public void getPostById_should_returnPost() {
        Post mockPost = Helpers.createMockPost();
        when(postRepository.getPostById(1)).thenReturn(mockPost);

        Post result = postService.getPostById(1);

        assertEquals(mockPost, result);
    }

    @Test
    public void getAllPosts_should_returnPosts() {
        Post mockPost = Helpers.createMockPost();
        PostFilters filters = new PostFilters(null, null, null, null);
        when(postRepository.getAllPosts(filters)).thenReturn(List.of(mockPost));

        List<Post> result = postService.getAllPosts(filters);

        assertEquals(1, result.size());
    }

    @Test
    public void createPost_should_callRepository_when_userIsNotBlocked() {
        User user = Helpers.createMockUser();
        Post post = Helpers.createMockPost();

        postService.createPost(post, user);

        verify(postRepository, times(1)).createPost(post);
    }

    @Test
    public void createPost_should_setAuthor() {
        User user = Helpers.createMockUser();
        Post post = Helpers.createMockPost();

        postService.createPost(post, user);

        assertEquals(user, post.getAuthor());
    }

    @Test
    public void createPost_should_throw_when_userIsBlocked() {
        User user = Helpers.createMockUser();
        user.setBlocked(true);
        Post post = Helpers.createMockPost();

        assertThrows(AuthorizationException.class, () -> postService.createPost(post, user));
        verify(postRepository, never()).createPost(any(Post.class));
    }

    @Test
    public void updatePost_should_callRepository_when_userIsAuthor() {
        User user = Helpers.createMockUser();
        Post post = Helpers.createMockPost();
        post.setAuthor(user);
        when(postRepository.getPostById(post.getId())).thenReturn(post);

        postService.updatePost(post, user);

        verify(postRepository, times(1)).updatePost(post);
    }

    @Test
    public void updatePost_should_throw_when_userIsBlocked() {
        User user = Helpers.createMockUser();
        user.setBlocked(true);
        Post post = Helpers.createMockPost();

        assertThrows(AuthorizationException.class, () -> postService.updatePost(post, user));
        verify(postRepository, never()).updatePost(any(Post.class));
    }

    @Test
    public void deletePost_should_callRepository() {
        User user = Helpers.createMockUser();
        Post post = Helpers.createMockPost();
        post.setAuthor(user);
        when(postRepository.getPostById(1)).thenReturn(post);

        postService.deletePost(1, user);

        verify(postRepository, times(1)).deletePost(1);
    }

    @Test
    public void getPostsCount_should_returnCount() {
        when(postRepository.getPostsCount()).thenReturn(5L);

        long result = postService.getPostsCount();

        assertEquals(5L, result);
    }

    @Test
    public void getMostRecentPosts_should_returnPosts() {
        Post mockPost = Helpers.createMockPost();
        when(postRepository.getMostRecentPosts()).thenReturn(List.of(mockPost));

        List<Post> result = postService.getMostRecentPosts();

        assertEquals(1, result.size());
    }

    @Test
    public void getMostCommentedPosts_should_returnPosts() {
        Post mockPost = Helpers.createMockPost();
        when(postRepository.getMostCommentedPosts()).thenReturn(List.of(mockPost));

        List<Post> result = postService.getMostCommentedPosts();

        assertEquals(1, result.size());
    }
}
