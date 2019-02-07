package com.hsyou.wagupost;

import com.hsyou.wagupost.model.Post;
import com.hsyou.wagupost.model.PostDTO;
import com.hsyou.wagupost.repository.PostRepository;
import com.hsyou.wagupost.service.PostService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;
    @Before
    public void 포스트넣기(){
        Post post = Post.builder()
                .id(1L)
                .accountId(1L)
                .contents("test")
                .build();
        postRepository.save(post);


    }

    @Test
    public void 포스트가져오기(){

        PostDTO post = postService.getPost(1L);

    }

}
