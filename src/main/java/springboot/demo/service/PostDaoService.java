package springboot.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.demo.dto.PostDto;
import springboot.demo.model.dao.PostDao;
import springboot.demo.model.entity.Post;

@Service
public class PostDaoService {
    @Autowired
    private PostDao postDao;

    public Post create(PostDto postDto) {
        Post post = new Post();
        post.setAuthorId(postDto.getAuthorId());
        post.setContent(postDto.getContent());
        return postDao.save(post);
    }
}
