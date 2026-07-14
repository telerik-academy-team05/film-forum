package com.telerik.filmforum.helpers;

import com.telerik.filmforum.models.Post;
import com.telerik.filmforum.models.PostDto;
import com.telerik.filmforum.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    private final PostService postService;

    @Autowired
    public PostMapper(PostService postService) {
        this.postService = postService;
    }

    public Post fromDto(PostDto postDto) {
        Post post = new Post();
        return mapCommonFields(postDto, post);
    }

    public Post fromDto(int id, PostDto postDto) {
        Post post = postService.getPostById(id);
        return mapCommonFields(postDto, post);
    }

    private Post mapCommonFields(PostDto postDto, Post post) {
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        return post;
    }

    public PostDto toDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());

        return postDto;
    }
}
