package springboot.demo.graphql.post;

import graphql.kickstart.tools.GraphQLResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springboot.demo.model.dao.UserDao;
import springboot.demo.model.entity.Post;
import springboot.demo.model.entity.User;

import java.time.ZoneId;

@Slf4j
@Component
public class PostResolver implements GraphQLResolver<Post> {
    @Autowired
    private UserDao userDao;

    public User getAuthor(Post post) {
        return userDao.findById(post.getAuthorId()).get();
    }
}
