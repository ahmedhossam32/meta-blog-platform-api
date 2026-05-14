package com.meta.blogapi.service;

import com.meta.blogapi.dto.request.PostRequest;
import com.meta.blogapi.dto.response.PostResponse;
import com.meta.blogapi.entity.Post;
import com.meta.blogapi.entity.User;
import com.meta.blogapi.exception.ResourceNotFoundException;
import com.meta.blogapi.exception.UnauthorizedException;
import com.meta.blogapi.repository.PostRepository;
import com.meta.blogapi.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Page<PostResponse> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(this::toResponse);
    }

    public PostResponse createPost(PostRequest request, String email) {
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .build();

        return toResponse(postRepository.save(post));
    }

    public PostResponse updatePost(Long id, PostRequest request, String email) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        if (!post.getAuthor().getEmail().equals(email)) {
            throw new UnauthorizedException("Access denied: you are not the owner of this post");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        return toResponse(postRepository.save(post));
    }

    public void deletePost(Long id, String email) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        if (!post.getAuthor().getEmail().equals(email)) {
            throw new UnauthorizedException("Access denied: you are not the owner of this post");
        }

        postRepository.delete(post);
    }

    private PostResponse toResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorName(post.getAuthor().getName())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
