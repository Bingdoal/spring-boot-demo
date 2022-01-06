package springboot.demo.graphql.post;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;
import springboot.demo.graphql.dataloader.AuthorDataLoader;
import springboot.demo.model.entity.Post;
import springboot.demo.model.entity.User;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class PostResolver implements GraphQLResolver<Post> {

    public CompletableFuture<User> getAuthor(Post post, DataFetchingEnvironment environment) {
        DataLoader<Long, User> dataLoader = environment.getDataLoader(AuthorDataLoader.KEY);
        return dataLoader.load(post.getAuthorId());
    }
}
