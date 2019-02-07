package com.hsyou.wagupost.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsyou.wagupost.model.CommentDTO;
import com.hsyou.wagupost.model.Post;
import com.hsyou.wagupost.model.PostDTO;
import com.hsyou.wagupost.repository.PostRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/*
    Business logic 처리
 */
@Service
public class PostService {


    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private KafkaProvider kafkaProvider;

    public PostDTO savePost(PostDTO postDTO, long accountId){
        try {
            postDTO.setAccountId(accountId);
            PostDTO rst = postRepository.save(postDTO.toEntity()).toDTO();
            kafkaProvider.sendPostMessage(rst);
            return rst;
        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DB Error");
        }
    }

    public Page<PostDTO> listPost (Pageable pageable){
        return postRepository.findAll(pageable).map(p -> p.toDTO());
    }

    public PostDTO getPost(long id){
        Optional<Post> optPost = postRepository.findById(id);
        if(!optPost.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post Not Found.");
        }
        PostDTO postDTO = optPost.get().toDTO();
        List<CommentDTO> comments = commentService.requestComment(id);
        comments.stream().forEach(x -> System.out.println(x.getContents()));
        postDTO.setComments(comments);
        return postDTO;
    }

    public PostDTO removePost(long postId, long accountId){

        Optional<Post> optPost = postRepository.findById(postId);
        if(optPost.isPresent()){
            if(optPost.get().getAccountId() != accountId){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized, Not a Writer");
            }
            optPost.get().setRemoved(true);
            kafkaProvider.sendPostMessage(optPost.get().toDTO());
            return postRepository.save(optPost.get()).toDTO();
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post Not Found.");
        }
    }
}
