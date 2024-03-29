package com.example.questappbackend.services;

import com.example.questappbackend.entities.Post;
import com.example.questappbackend.entities.User;
import com.example.questappbackend.repos.PostRepository;
import com.example.questappbackend.requests.PostCreateRequest;
import com.example.questappbackend.requests.PostUpdateRequest;
import com.example.questappbackend.responses.LikeResponse;
import com.example.questappbackend.responses.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final LikeService likeService;
    private final UserService userService;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService, @Lazy LikeService likeService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.likeService = likeService;
    }

    public List<PostResponse> getAllPosts(Optional<Long> userId) {
        List<Post> list;
        if(userId.isPresent()){
            list = postRepository.findByUserId(userId.get());
        } else {
            list = postRepository.findAll();
        }
        return list.stream().map(p  -> {
            List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.empty(), Optional.ofNullable(p.getId()));
            return new PostResponse(p, likes);
        }).collect(Collectors.toList());
    }

    public Post getOnePostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public Post createOnePost(PostCreateRequest newPostRequest) {
        User user = userService.getOneUserById(newPostRequest.getUserId());
        if(user == null){
            return null;
        }
        Post toSave = new Post();
        toSave.setId(newPostRequest.getId());
        toSave.setText(newPostRequest.getText());
        toSave.setTitle(newPostRequest.getTitle());
        toSave.setUser(user);
        return postRepository.save(toSave);
    }

    public Post updateOnePostById(Long postId, PostUpdateRequest updateRequest) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()){
            Post toUpdate = post.get();
            toUpdate.setText(updateRequest.getText());
            toUpdate.setTitle(updateRequest.getTitle());
            return postRepository.save(toUpdate);
        }
        return null;
    }

    public void deleteOnePostById(Long postId) {
        postRepository.deleteById(postId);
    }


}
