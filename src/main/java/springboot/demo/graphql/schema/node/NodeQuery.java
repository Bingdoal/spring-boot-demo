package springboot.demo.graphql.schema.node;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springboot.demo.model.dao.PostDao;
import springboot.demo.model.dao.UserDao;
import springboot.demo.model.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class NodeQuery implements GraphQLQueryResolver {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PostDao postDao;

    public Object target(Source source, long id) {
        switch (source) {
            case POST:
                Optional<Post> postOpt = postDao.findOne(QPost.post.id.eq(id));
                return postOpt.orElse(null);
            case USER:
                Optional<User> userOpt = userDao.findOne(QUser.user.id.eq(id));
                return userOpt.orElse(null);
        }
        return null;
    }

    public List<EntityBase> nodeList(Source source, List<Long> ids) {
        switch (source) {
            case POST:
                return new ArrayList<>(postDao.findAll());
            case USER:
                return new ArrayList<>(userDao.findAll());
        }
        return new ArrayList<>();
    }

    public enum Source {
        USER,
        POST
    }
}
