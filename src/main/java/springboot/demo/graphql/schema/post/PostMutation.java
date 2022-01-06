package springboot.demo.graphql.schema.post;

import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import springboot.demo.dto.PostDto;
import springboot.demo.model.entity.Post;
import springboot.demo.service.PostDaoService;

import javax.validation.Valid;

@Component
@Slf4j
@Validated
public class PostMutation implements GraphQLMutationResolver {
    @Autowired
    private PostDaoService postDaoService;

    public Post createPost(@Valid PostDto postCreateInput) {
        return postDaoService.create(postCreateInput);
    }
}
