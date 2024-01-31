package com.example.questappbackend.services;

import com.example.questappbackend.entities.Like;
import com.example.questappbackend.entities.Post;
import com.example.questappbackend.entities.User;
import com.example.questappbackend.repos.LikeRepository;
import com.example.questappbackend.requests.LikeCreateRequest;
import com.example.questappbackend.responses.LikeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public LikeService(LikeRepository likeRepository, UserService userService,
                     @Lazy  PostService postService) {
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.postService = postService;
    }

    public List<LikeResponse> getAllLikesWithParam(Optional<Long> userId, Optional<Long> postId) {
        List<Like> list;
        if(userId.isPresent() && postId.isPresent()) {
            list = likeRepository.findByUserIdAndPostId(userId.get(), postId.get());
        }else if(userId.isPresent()) {
            list = likeRepository.findByUserId(userId.get());
        }else if(postId.isPresent()) {
            list = likeRepository.findByPostId(postId.get());
        }else
            list = likeRepository.findAll();
        return list.stream().map(LikeResponse::new).collect(Collectors.toList());
    }

    public Like getOneLikeById(Long likeId) {
        return likeRepository.findById(likeId).orElse(null);
    }

    public Like createOneLike(LikeCreateRequest request) {
        User user = userService.getOneUserById(request.getUserId());
        Post post = postService.getOnePostById(request.getPostId());
        if(user != null && post != null) {
            Like likeToSave = new Like();
            likeToSave.setId(request.getId());
            likeToSave.setPost(post);
            likeToSave.setUser(user);
            return likeRepository.save(likeToSave);
        }else
            return null;
    }

    public void deleteOneLikeById(Long likeId) {
        likeRepository.deleteById(likeId);
    }
}