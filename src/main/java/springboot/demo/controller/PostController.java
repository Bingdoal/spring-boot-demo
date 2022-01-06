package springboot.demo.controller;

import com.querydsl.core.types.Predicate;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springboot.demo.dto.PostDto;
import springboot.demo.dto.basic.PageResultDto;
import springboot.demo.middleware.exception.StatusException;
import springboot.demo.model.dao.PostDao;
import springboot.demo.model.entity.Post;
import springboot.demo.service.PostDaoService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Api("Post")
@Slf4j
@RestController
@RequestMapping("/v1/post")
public class PostController {
    @Autowired
    private PostDao postDao;
    @Autowired
    private PostDaoService postDaoService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public PageResultDto<Post> fetchAllPost(@QuerydslPredicate(root = Post.class) Predicate predicate,
                                            final Pageable pageable) {
        return new PageResultDto<>(postDao.findAll(predicate, pageable));
    }

    @GetMapping("/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    public List<Post> fetchAllPostByUsername(@PathVariable("username") String username) {
        return postDaoService.findByUsername(username);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Post createPost(@RequestBody @Valid PostDto postDto) {
        return postDaoService.create(postDto);
    }

    @PutMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Post modifyPost(@PathVariable("postId") Long postId,
                           @RequestBody @Validated(PostDto.Update.class) PostDto postDto) throws StatusException {
        Optional<Post> postOpt = postDao.findById(postId);
        if (postOpt.isEmpty()) {
            throw new StatusException(404, "Post id(" + postId + ") not found.");
        }
        Post post = postOpt.get();
        post.setContent(postDto.getContent());
        return postDao.save(post);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable("postId") Long postId) throws StatusException {
        if (!postDao.existsById(postId)) {
            throw new StatusException(404, "Post id(" + postId + ") not found.");
        }
        postDao.deleteById(postId);
    }
}
