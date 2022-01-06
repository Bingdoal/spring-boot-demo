package springboot.demo.graphql.post;

import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springboot.demo.model.dao.PostDao;
import springboot.demo.model.entity.Post;

import java.util.List;

@Component
@Slf4j
public class PostQuery implements GraphQLQueryResolver {
    @Autowired
    private PostDao postDao;

    public List<Post> getPostList() {
        return postDao.findAll();
    }

    public Post getPost(Long id) {
        return postDao.findById(id).orElse(null);
    }

}
