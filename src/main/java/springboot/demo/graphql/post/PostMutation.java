package springboot.demo.graphql.post;

import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springboot.demo.dto.PostDto;
import springboot.demo.model.entity.Post;
import springboot.demo.service.PostDaoService;

@Component
@Slf4j
public class PostMutation implements GraphQLMutationResolver {
    @Autowired
    private PostDaoService postDaoService;

    public Post createPost(String content, Long authorId) {
        PostDto postDto = new PostDto();
        postDto.setContent(content);
        postDto.setAuthorId(authorId);
        return postDaoService.create(postDto);
    }
}
